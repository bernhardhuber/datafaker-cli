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

import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.Function;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

/**
 *
 * @author berni3
 */
public class Icd3Diseases extends AbstractProvider<BaseProviders> {

    private static final String KEY = "diseases";
    private Icd3CodesDiseasesData icd3CodesDiseases;

    private Function<String, String[]> splitt = (l) -> {
        String[] splitted = l.split("@@");
        return splitted;
    };

    public Icd3Diseases(BaseProviders faker) {
        super(faker);
        //faker.addPath(Locale.ENGLISH, Paths.get("src/main/resources/icd3_diagnoses.yml"));
        faker.addPath(Locale.GERMAN, Paths.get("src/main/resources/icd3_diagnoses.yml"));
        icd3CodesDiseases = new Icd3CodesDiseasesData();
    }

    //----
    public String icdCodeYml() {
        String line = faker.resolve(KEY + ".codes_diseases");
        String icd3Code = splitt.apply(line)[0];
        return icd3Code;
    }

    public String diseaseYml() {
        String line = faker.resolve(KEY + ".codes_diseases");
        String disease = splitt.apply(line)[1];
        return disease;
    }
    //----

    public String icdCode() {
        String[] icd3Codes = icd3CodesDiseases.icd3Codes();
        final int l = icd3Codes.length;
        final int mapIndex = faker.random().nextInt(l);
        final String key = icd3Codes[mapIndex];
        return key;
    }

    public String disease() {
        String[] diseases = icd3CodesDiseases.diseases();
        final int l = diseases.length;
        final int mapIndex = faker.random().nextInt(l);
        final String key = diseases[mapIndex];
        return key;
    }
}
