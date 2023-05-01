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
import java.util.regex.Pattern;
import net.datafaker.Faker;
import org.huberb.datafaker.cli.Adapters;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class Icd3DiseasesTest {

    private static Faker faker;
    private static Predicate<String> icdCodePredicate = s -> {
        return Pattern.matches("[A-Z]\\d\\d", s);
    };

    @BeforeAll
    public static void setUpAll() {
        faker = Adapters.FakerFactory.createFakerFromLocale(Locale.GERMAN);
    }
    private Icd3Diseases instance;

    @BeforeEach
    public void setUp() {
        instance = faker.getProvider(Icd3Diseases.class, Icd3Diseases::new, faker);
    }

    @Test
    public void testIcdCode() {
        String result = instance.icdCode();
        assertNotNull(result);
        assertEquals(3, result.length(), "" + result);
        assertTrue(icdCodePredicate.test(result), "" + result);
        assertTrue(!result.isBlank(), "" + result);
    }

    @Test
    public void testDisease() {
        String result = instance.disease();
        assertNotNull(result);
        assertTrue(result.length() > 1, "" + result);
        assertTrue(!result.isBlank(), "" + result);
    }

}
