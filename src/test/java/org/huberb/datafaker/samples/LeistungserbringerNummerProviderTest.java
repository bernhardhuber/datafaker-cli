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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class LeistungserbringerNummerProviderTest {

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
    private LeistungserbringerNummerProvider instance;

    @BeforeEach
    public void setUp() {
        instance = Faker.getProvider(LeistungserbringerNummerProvider.class, LeistungserbringerNummerProvider::new, faker);
    }

    /**
     * Test of vpnr method, of class LeistungserbringerNummerProvider.
     */
    @Test
    public void testVpnr() {
        String result = instance.vpnr();
        assertAll(
                () -> assertEquals(6, result.length()),
                () -> assertTrue(allDigitsPredicate.test(result), "" + result)
        );
    }

    /**
     * Test of lenr method, of class LeistungserbringerNummerProvider.
     */
    @Test
    public void testLenr() {
        String result = instance.lenr();
        assertAll(
                () -> assertEquals(8, result.length()),
                () -> assertTrue(allDigitsPredicate.test(result), "" + result)
        );
    }
}
