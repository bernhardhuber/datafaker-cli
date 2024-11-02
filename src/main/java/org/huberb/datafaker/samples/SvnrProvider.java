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
package org.huberb.datafaker.samples;

import java.util.function.Predicate;
import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

/**
 * Berechnung österreichischer Sozialversicherungsnummer (SVNR).
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
 * <br>
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
 * @since 1.0
 */
public class SvnrProvider extends AbstractProvider<BaseProviders> {

    private final Predicate<String> allDigitsPredicate = s -> {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    };
    private final int[] mulLaufnummer = {3, 7, 9};
    private final int[] mulGeburtsdatum = {5, 8, 4, 2, 1, 6};

    /**
     * Create a new instance.
     *
     * @param faker
     * @see Faker
     */
    public SvnrProvider(BaseProviders faker) {
        super(faker);
    }

    /**
     * Create a svnr for a random birthday.
     * <p>
     * The generated random birthday is 1 to 99 years in the past.
     *
     * @return
     */
    public String svnr() {
        String geburtsdatum = genGeburtsdatum();
        return calcSvnr(geburtsdatum);
    }

    /**
     * Create a svnr for a given birthday.
     * <p>
     * Birthday shall have the format 'yyMMdd'.
     *
     * @param geburtsdatum
     * @return
     */
    public String svnrFor(String geburtsdatum) {
        return calcSvnr(geburtsdatum);
    }

    private String calcSvnr(String geburtsdatum) {
        int laufendeNummerInt = genLaufendeNummer();
        for (int i = 1; i < 10; i++) {
            String result = calcSvnrWithPruefziffer(geburtsdatum, laufendeNummerInt);
            if (result == null) {
                laufendeNummerInt += 1;
                if (laufendeNummerInt >= 1000) {
                    laufendeNummerInt = 100;
                }
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
                && laufendeNummerInt < 1000;
        if (!validLaufendenummerP.test(laufendeNummerInt)) {
            throw new RuntimeException(String.format("Laufendenummer invalid: %d", laufendeNummerInt));
        }

        String laufendeNummer = "" + laufendeNummerInt;
        int pruefnummer = 0;
        {
            int laufendeNummer_0 = Character.getNumericValue(laufendeNummer.charAt(0));
            int laufendeNummer_1 = Character.getNumericValue(laufendeNummer.charAt(1));
            int laufendeNummer_2 = Character.getNumericValue(laufendeNummer.charAt(2));

            pruefnummer += laufendeNummer_0 * mulLaufnummer[0];
            pruefnummer += laufendeNummer_1 * mulLaufnummer[1];
            pruefnummer += laufendeNummer_2 * mulLaufnummer[2];
        }
        {
            int geburtsdatum_0 = Character.getNumericValue(geburtsdatum.charAt(0));
            int geburtsdatum_1 = Character.getNumericValue(geburtsdatum.charAt(1));
            int geburtsdatum_2 = Character.getNumericValue(geburtsdatum.charAt(2));
            int geburtsdatum_3 = Character.getNumericValue(geburtsdatum.charAt(3));
            int geburtsdatum_4 = Character.getNumericValue(geburtsdatum.charAt(4));
            int geburtsdatum_5 = Character.getNumericValue(geburtsdatum.charAt(5));

            pruefnummer += geburtsdatum_0 * mulGeburtsdatum[0];
            pruefnummer += geburtsdatum_1 * mulGeburtsdatum[1];
            pruefnummer += geburtsdatum_2 * mulGeburtsdatum[2];
            pruefnummer += geburtsdatum_3 * mulGeburtsdatum[3];
            pruefnummer += geburtsdatum_4 * mulGeburtsdatum[4];
            pruefnummer += geburtsdatum_5 * mulGeburtsdatum[5];
        }
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
        return faker.random().nextInt(100, 999);
    }

}
