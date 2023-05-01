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

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import org.huberb.datafaker.cli.DataFormatProcessor.ExpressionInternal;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class SamplesGeneratorTest {

    private static Faker faker;

    @BeforeAll
    public static void setUpAll() {
        faker = Adapters.FakerFactory.createFakerFromLocale(Locale.getDefault());
    }
    private SamplesGenerator instance;

    @BeforeEach
    public void setUpEach() {
        this.instance = new SamplesGenerator();
    }

    /**
     * Test of sampleExpressions method, of class SamplesGenerator.
     */
    @Test
    public void testSampleExpressions() {
        List<ExpressionInternal> result = instance.sampleExpressions(faker);
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.size())
        );
    }

    @Test
    public void testSampleExpressions_List_String() {
        List<String> result = instance.sampleExpressions();
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.size())
        );
    }

    /**
     * Test of sampleProviders method, of class SamplesGenerator.
     */
    @Test
    public void test_sampleProviderAsExpressionInternalList1() {
        /*
        Sample
         */
        List<ExpressionInternal> result = instance.sampleProviderAsExpressionInternalList1(faker, "*");
        List<String> fieldNames = result.stream().map(ei -> ei.fieldname).collect(Collectors.toList());
        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                //() -> assertEquals("", result),
                () -> assertFalse(result.isEmpty(), m),
                () -> assertTrue(fieldNames.contains("Name-fullName"), m),
                () -> assertTrue(fieldNames.contains("Address-fullAddress"), m)
        );
    }

    /**
     * Test of sampleProviders2 method, of class SamplesGenerator.
     */
    @Test
    public void test_sampleProviderAsExpressionInternalList2() {
        List<String> providerNames = Collections.emptyList();
        List<ExpressionInternal> result = instance.sampleProviderAsExpressionInternalList2(faker, providerNames);
        List<String> fieldNames = result.stream().map(ei -> ei.fieldname).collect(Collectors.toList());
        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                //() -> assertEquals("", result),
                () -> assertFalse(result.isEmpty(), m),
                () -> assertTrue(fieldNames.contains("Name-fullName"), m),
                () -> assertTrue(fieldNames.contains("Address-fullAddress"), m)
        );
    }

}
