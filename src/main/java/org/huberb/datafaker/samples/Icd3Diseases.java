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

import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

/**
 *
 * @author berni3
 */
public class Icd3Diseases extends AbstractProvider<BaseProviders> {

    private final Icd3CodesDiseasesData icd3CodesDiseases;

    /**
     * Create a new instance.
     *
     * @param faker
     * @see Faker
     */
    public Icd3Diseases(BaseProviders faker) {
        super(faker);
        icd3CodesDiseases = new Icd3CodesDiseasesData();
    }

    //----
    /**
     * Return an icd format having length 3.
     *
     * @return
     */
    public String icdCode() {
        String[] icd3Codes = icd3CodesDiseases.icd3Codes();
        final int l = icd3Codes.length;
        final int mapIndex = faker.random().nextInt(l);
        final String key = icd3Codes[mapIndex];
        return key;
    }

    /**
     * Return a disease name.
     *
     * @return
     */
    public String disease() {
        String[] diseases = icd3CodesDiseases.diseases();
        final int l = diseases.length;
        final int mapIndex = faker.random().nextInt(l);
        final String key = diseases[mapIndex];
        return key;
    }
}
