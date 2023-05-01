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

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import net.datafaker.providers.base.Address;
import net.datafaker.providers.base.Name;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class ProvidersQueriesTest {

    ProvidersQueries instance;

    @BeforeEach
    public void setUp() {
        instance = new ProvidersQueries();
    }

    /**
     * Test of findAllClassesExtendingAbstractProvider method, of class
     * ProvidersQueries.
     */
    @Test
    public void testFindAllClassesExtendingAbstractProvider() {
        List<Class> result = instance.findAllClassesExtendingAbstractProvider();
        String m = "" + result;
        assertAll(
                () -> assertTrue(result.contains(Name.class), m),
                () -> assertTrue(result.contains(Address.class), m),
                () -> assertFalse(result.contains(String.class), m)
        );
    }

    /**
     * Test of findAllMethodsClassesExtendingAbstractProvider method, of class
     * ProvidersQueries.
     */
    @Test
    public void testFindAllMethodsClassesExtendingAbstractProvider() {
        List<Method> result = instance.findAllMethodsClassesExtendingAbstractProvider();
        List<Class> resultDeclaringClasses = result.stream()
                .map(m -> m.getDeclaringClass())
                .collect(Collectors.toList());
        List<String> resultMethodNames = result.stream()
                .map(m -> m.getName())
                .collect(Collectors.toList());
        String message = "" + result;
        assertAll(
                () -> assertTrue(resultMethodNames.contains("fullName"), message),
                () -> assertTrue(resultMethodNames.contains("fullAddress"), message),
                () -> assertTrue(resultDeclaringClasses.contains(Name.class), message),
                () -> assertTrue(resultDeclaringClasses.contains(Address.class), message),
                () -> assertFalse(resultDeclaringClasses.contains(String.class), message)
        );
    }

}
