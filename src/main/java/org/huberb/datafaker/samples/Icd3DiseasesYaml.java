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

import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.Function;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

/**
 *
 * @author berni3
 */
public class Icd3DiseasesYaml extends AbstractProvider<BaseProviders> {

    private static final String KEY = "diseases";

    private Function<String, String[]> splitt = (l) -> {
        String[] splitted = l.split("@@");
        return splitted;
    };

    /**
     * Create a new instance.
     *
     * @param faker
     * @see Faker
     */
    public Icd3DiseasesYaml(BaseProviders faker) {
        super(faker);
        faker.addPath(Locale.GERMAN, Paths.get("src/main/resources/icd3_diagnoses.yml"));
    }

    //----
    /**
     * Return an icd format having length 3.
     *
     * @return
     */
    public String icdCode() {
        String line = faker.resolve(KEY + ".codes_diseases");
        String icd3Code = splitt.apply(line)[0];
        return icd3Code;
    }

    /**
     * Return a disease name.
     *
     * @return
     */
    public String disease() {
        String line = faker.resolve(KEY + ".codes_diseases");
        String disease = splitt.apply(line)[1];
        return disease;
    }
    //----

}
