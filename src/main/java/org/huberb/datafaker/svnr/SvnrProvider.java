/*
 * Copyright 2023 berni3.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.huberb.datafaker.svnr;

import java.util.function.Predicate;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

/**
 * Berechnung SVNR.
 * <p>
 * Die Prüfziffer kann wie folgt errechnet werden:[1]
 * <p>
 * Jede einzelne Ziffer der Versicherungsnummer wird mit einer bestimmten Zahl
 * multipliziert:
 * <ul>
 * <li>Laufnummer mit 3, 7, 9
 * <li>Geburtsdatum mit 5, 8, 4, 2, 1, 6
 * </ul>
 * <p>
 * Die Prüfziffer errechnet sich aus dem Divisionsrest der Summe der einzelnen
 * Produkte geteilt durch 11.
 * <br/>
 * Wenn der Divisionsrest 10 ergibt, so wird die Laufnummer um 1 erhöht und eine
 * neue Berechnung durchgeführt.
 * <p>
 * Beispiel:
 * <pre>
 * 123X 010180
 * X = (1×3 + 2×7 + 3×9 + 0×5 + 1×8 + 0×4 + 1×2 + 8×1 + 0×6) mod 11
 * X = 7
 * </pre> Die Sozialversicherungsnummer lautet somit 1237 010180.
 *
 * @see "https://de.wikipedia.org/wiki/Sozialversicherungsnummer"
 * @author berni3
 */
public class SvnrProvider extends AbstractProvider<BaseProviders> {

    private Predicate<String> allDigitsPredicate = s -> {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    };
    private int[] mulLaufnummer = {3, 7, 9};
    private int[] mulGeburtsdatum = {5, 8, 4, 2, 1, 6};

    public SvnrProvider(BaseProviders faker) {
        super(faker);
    }

    public String svnr() {
        String geburtsdatum = genGeburtsdatum();
        return calcSvnr(geburtsdatum);
    }

    public String svnrFor(String geburtsdatum) {
        return calcSvnr(geburtsdatum);
    }

    private String calcSvnr(String geburtsdatum) {
        int laufendeNummerInt = genLaufendeNummer();
        for (int i = 1; i < 10; i++) {
            String result = calcSvnrWithPruefziffer(geburtsdatum, laufendeNummerInt);
            if (result == null) {
                laufendeNummerInt += 1;
            } else {
                return result;
            }
        }
        throw new RuntimeException(String.format("Cannot calculate svnr for geburtsdatum %s", geburtsdatum));
    }

    private String calcSvnrWithPruefziffer(String geburtsdatum, int laufendeNummerInt) {
        Predicate<String> validGeburtsdatumP = s -> geburtsdatum != null
                && geburtsdatum.length() == 6
                && allDigitsPredicate.test(geburtsdatum);
        if (!validGeburtsdatumP.test(geburtsdatum)) {
            throw new RuntimeException(String.format("Geburtsdatum invalid: %s", geburtsdatum));
        }
        Predicate<Integer> validLaufendenummerP = i -> laufendeNummerInt >= 100
                && laufendeNummerInt <= 1000;
        if (!validLaufendenummerP.test(laufendeNummerInt)) {
            throw new RuntimeException(String.format("Laufendenummer invalid: %d", laufendeNummerInt));
        }

        String laufendeNummer = "" + laufendeNummerInt;

        int laufendeNummer_0 = laufendeNummer.charAt(0) - '0';
        int laufendeNummer_1 = laufendeNummer.charAt(1) - '0';
        int laufendeNummer_2 = laufendeNummer.charAt(2) - '0';

        int pruefnummer = 0;
        pruefnummer += laufendeNummer_0 * mulLaufnummer[0];
        pruefnummer += laufendeNummer_1 * mulLaufnummer[1];
        pruefnummer += laufendeNummer_2 * mulLaufnummer[2];

        int geburtsdatum_0 = geburtsdatum.charAt(0) - '0';
        int geburtsdatum_1 = geburtsdatum.charAt(1) - '0';
        int geburtsdatum_2 = geburtsdatum.charAt(2) - '0';
        int geburtsdatum_3 = geburtsdatum.charAt(3) - '0';
        int geburtsdatum_4 = geburtsdatum.charAt(4) - '0';
        int geburtsdatum_5 = geburtsdatum.charAt(5) - '0';

        pruefnummer += geburtsdatum_0 * mulGeburtsdatum[0];
        pruefnummer += geburtsdatum_1 * mulGeburtsdatum[1];
        pruefnummer += geburtsdatum_2 * mulGeburtsdatum[2];
        pruefnummer += geburtsdatum_3 * mulGeburtsdatum[3];
        pruefnummer += geburtsdatum_4 * mulGeburtsdatum[4];
        pruefnummer += geburtsdatum_5 * mulGeburtsdatum[5];

        int pruefziffer = pruefnummer % 11;
        if (pruefziffer == 10) {
            return null;
        }
        return laufendeNummer + ("" + pruefziffer) + geburtsdatum;
    }

    private String genGeburtsdatum() {
        return faker.date().birthday(1, 99, "yyMMdd");
    }

    private int genLaufendeNummer() {
        int result = faker.random().nextInt(100, 999);
        System.out.format("genLaufendeNummer %d%n", result);
        return result;
    }

}
