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

import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

/**
 *
 * @author berni3
 * @since 1.0
 */
public class LeistungserbringerNummerProvider extends AbstractProvider<BaseProviders> {

    /**
     * Create a new instance.
     *
     * @param faker
     * @see Faker
     */
    public LeistungserbringerNummerProvider(BaseProviders faker) {
        super(faker);
    }

    /**
     * Return a vpnr number.
     * <p>
     * Return vpnr is a string having length 6, consisting only of digits,
     * checksum digit is *not* guaranteed.
     *
     * @return
     */
    public String vpnr() {
        // TODO check well-define length, and alphabet
        String result = faker.numerify("######");
        return result;
    }

    /**
     * Return a lenr number.
     * <p>
     * Return vpnr is a string having length 8, consisting only of digits,
     * checksum digit is *not* guaranteed.
     *
     * @return
     */
    public String lenr() {
        // TODO check well-define length, and alphabet
        String result = faker.numerify("########");
        return result;
    }
}
