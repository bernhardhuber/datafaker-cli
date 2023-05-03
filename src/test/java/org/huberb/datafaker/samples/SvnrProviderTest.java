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

import java.util.Locale;
import java.util.function.Predicate;
import net.datafaker.Faker;
import org.huberb.datafaker.cli.Adapters;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

/**
 *
 * @author berni3
 */
public class SvnrProviderTest {

    private static Faker faker;
    private static Predicate<String> allDigitsPredicate = s -> {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    };

    @BeforeAll
    public static void setUpAll() {
        faker = Adapters.FakerFactory.createFakerFromLocale(Locale.getDefault());
    }

    private SvnrProvider instance;

    @BeforeEach
    public void setUpEach() {
        instance = Faker.getProvider(SvnrProvider.class, SvnrProvider::new, faker);
    }

    /**
     * Test of svnr method, of class SvnrProvider.
     */
    @RepeatedTest(value = 100)
    public void testSvnr() {
        String result = instance.svnr();

        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.length() == 3 + 1 + 6, m),
                () -> assertTrue(allDigitsPredicate.test(result), m)
        );
        //System.out.format("testSvnr %s%n", result);
    }

    /**
     * Test of svnrFor method, of class SvnrProvider.
     */
    @RepeatedTest(value = 100)
    public void testSvnrFor() {
        String geburtsdatum = "220422";
        String result = instance.svnrFor(geburtsdatum);

        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.length() == 3 + 1 + 6, m),
                () -> assertTrue(allDigitsPredicate.test(result), m),
                () -> assertTrue(result.endsWith(geburtsdatum), m)
        );
        //System.out.format("testSvnrFor %s%n", result);
    }

}
