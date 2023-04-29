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
package org.huberb.datafaker.cli;

import java.util.Locale;
import net.datafaker.Faker;
import org.huberb.datafaker.cli.Adapters.FakerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author berni3
 */
public class FakerFactoryTest {

    public FakerFactoryTest() {
    }

    @Test
    public void testSomeMethod() {
        Faker faker = FakerFactory.createFakerFromLocale(Locale.getDefault());
        assertNotNull(faker);
    }

    @ParameterizedTest
    @CsvSource({
        "en,    en",
        "de,    de",
        "de-AT, de_AT"
    })
    public void testSomeMethod2(String languageTag, String expectedLocaleToString) {
        Faker faker = FakerFactory.createFakerFromLocale(languageTag);
        assertNotNull(faker);
        Locale fakerLocale = faker.getContext().getLocale();
        assertEquals(expectedLocaleToString, fakerLocale.toString());
    }

}
