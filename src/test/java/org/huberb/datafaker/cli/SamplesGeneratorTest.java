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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import org.junit.jupiter.api.Assertions;
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

    static Faker faker;

    SamplesGenerator instance;

    @BeforeAll
    static void setUpAll() {
        faker = Adapters.FakerFactory.createFakerFromLocale(Locale.getDefault());
    }

    @BeforeEach
    void setUpEach() {
        this.instance = new SamplesGenerator();
    }

    /**
     * Test of sampleExpression method, of class SamplesGenerator.
     */
    @Test
    public void testSampleExpression() {
        String result = instance.sampleExpression(faker);
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isBlank())
        );
    }

    /**
     * Test of sampleCsv method, of class SamplesGenerator.
     */
    @Test
    public void testSampleCsv() {
        /*
        Sample:
        "fullName","fullAddress"
        "Shanta Schulist","Apt. 015 9511 Rey Squares, North Annette, WV 03787"
        "Odilia Mraz","Apt. 791 85219 Krysta Stravenue, New Lemuelstad, NY 71901"
        "Terry Veum","Apt. 362 4216 Giuseppina Square, South Elden, MA 17916"
         */
        String result = instance.sampleCsv(faker, 3);
        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isBlank()),
                //() -> assertEquals("", result),
                () -> assertTrue(result.startsWith("\"fullName\",\"fullAddress\""), m)
        );
    }

    /**
     * Test of sampleJson method, of class SamplesGenerator.
     */
    @Test
    public void testSampleJson() {
        /*
        Sample
        {
        {"fullName": "Salvatore Moore IV", "fullAddress": "2461 Winnie Terrace, Port Ebony, ID 53492"},
        {"fullName": "Helene Schinner", "fullAddress": "3516 Elbert Fall, New Rosalee, CA 46117"},
        {"fullName": "Dr. Gertude Tremblay", "fullAddress": "Apt. 914 5680 Larkin Shore, Maurineside, RI 84485"}
        }
         */
        String result = instance.sampleJson(faker, 3);
        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isBlank()),
                //() -> assertEquals("", result),
                () -> assertTrue(result.contains("\"fullName\":"), m),
                () -> assertTrue(result.contains("\"fullAddress\":"), m),
                () -> assertTrue(result.contains("{"), m),
                () -> assertTrue(result.contains("},"), m),
                () -> assertTrue(result.contains("}"), m)
        );
    }

    /**
     * Test of sampleSql method, of class SamplesGenerator.
     */
    @Test
    public void testSampleSql() {
        /*
        Sample
        INSERT INTO DATAFAKER_NAME_ADDRESS ("fullName", "fullAddress")
        VALUES ('Garfield Spencer', 'Apt. 554 95864 Clifton Flats, Walterfurt, OH 15308'),
               ('Karma Emmerich', 'Suite 318 520 Tyrone Estates, Champlinchester, VT 18054'),
               ('Oscar McLaughlin', 'Suite 877 483 Erdman Common, Lake Jared, NE 19287');
         */
        String result = instance.sampleSql(faker, 3);
        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isBlank()),
                //() -> assertEquals("", result),
                () -> assertTrue(result.startsWith("INSERT INTO DATAFAKER_NAME_ADDRESS (\"fullName\", \"fullAddress\")"), m),
                () -> assertTrue(result.contains("VALUES ("), m),
                () -> assertTrue(result.contains("),"), m),
                () -> assertTrue(result.contains(");"), m)
        );
    }

    /**
     * Test of sampleSql method, of class SamplesGenerator.
     */
    @Test
    public void testSampleProviders() {
        /*
        Sample
         */
        String result = instance.sampleProviders(faker, 3);
        String m = "" + result;
        Assertions.assertAll(
                () -> assertNotNull(result),
                //() -> assertEquals("", result),
                () -> assertFalse(result.isBlank())
        );
    }

    @Test
    public void hello1() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//        Set<String> allowedFakerMetods = new HashSet<>() {
//            {
//                add("address");
//                add("name");
//            }
//        };
        Predicate<Method> mP1 = m -> {

            boolean isMatch = true;
            isMatch = isMatch && m.getParameterCount() == 0;
            isMatch = isMatch && AbstractProvider.class.isAssignableFrom(m.getReturnType());
            isMatch = isMatch && Modifier.isPublic(m.getModifiers());
            //isMatch = isMatch && allowedFakerMetods.contains(m.getName());
            return isMatch;
        };
        Predicate<Method> mP2 = m -> {

            boolean isMatch = true;
            isMatch = isMatch && m.getParameterCount() == 0;
            isMatch = isMatch && m.getReturnType().equals(String.class);
            isMatch = isMatch && Modifier.isPublic(m.getModifiers());
            return isMatch;
        };
        Method[] methods1 = faker.getClass().getMethods();
        List<Method> providerMethodsInFaker = Arrays.asList(methods1).stream()
                .filter(mP1)
                .sorted((m1, m2) -> m1.getDeclaringClass().getName().compareTo(m2.getDeclaringClass().getName()))
                .collect(Collectors.toList());
        for (Method m : providerMethodsInFaker) {
            AbstractProvider ap = (AbstractProvider) m.invoke(faker);
            Arrays.asList(ap.getClass().getDeclaredMethods()).stream()
                    .filter(mP2)
                    .sorted((m1, m2) -> m1.getDeclaringClass().getName().compareTo(m2.getDeclaringClass().getName()))
                    .forEach(m2 -> {
                        try {
                            String result = (String) m2.invoke(ap);
                            System.out.format("ap %s#%s: %s%n", ap.getClass(), m2.getName(), result);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    });
        }
    }
}
