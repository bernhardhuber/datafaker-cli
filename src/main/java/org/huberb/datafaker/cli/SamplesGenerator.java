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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import org.huberb.datafaker.cli.DataFormatProcessor.ExpressionInternal;

/**
 *
 * @author berni3
 */
class SamplesGenerator {

    /**
     * Generate name, and address expressions.
     *
     * @param faker
     * @return
     */
    public List<ExpressionInternal> sampleExpressions(Faker faker) {
        List<ExpressionInternal> result = Arrays.asList(
                new ExpressionInternal("name", () -> faker.name().fullName()),
                new ExpressionInternal("address", () -> faker.address().fullAddress())
        );
        return result;
    }

    /**
     * Generate name, and address expressions.
     *
     * @return
     */
    public List< String> sampleExpressions() {
        return Arrays.asList("#{Name.fullName}", "#{Address.fullAddress}");
    }

    public List<ExpressionInternal> sampleProviderAsExpressionInternalList1(Faker faker, String providerName) {
        Function<String, String> normalizeProviderName = (s) -> {
            if (s == null) {
                return null;
            } else if (s.startsWith("*")) {
                return null;
            } else {
                return s;
            }
        };
        final String normalizedProviderName = normalizeProviderName.apply(providerName);
        final List<ExpressionInternal> resultExpressionInternal = new ArrayList<>();

        final Predicate<Method> methodProviderPredicate = (m) -> {
            boolean result = true;
            if (normalizedProviderName != null) {
                result = result && m.getParameterCount() == 0;
                result = result && m.getDeclaringClass().getSimpleName().equals(normalizedProviderName);
            } else {
                result = true;
            }
            return result;
        };
        final Predicate<Method> methodSignaturePredicate = (m) -> {
            boolean result = true;
            result = result && m.getParameterCount() == 0;
            result = result && m.getReturnType().equals(String.class);
            return result;
        };
        // Retrieve provider methods
        final List<Method> methodList = new ProvidersQueries().findAllMethodsClassesExtendingAbstractProvider().stream()
                .filter(methodProviderPredicate)
                .filter(methodSignaturePredicate)
                .sorted((m1, m2) -> m1.getClass().getName().compareTo(m2.getClass().getName()))
                .collect(Collectors.toList());

        for (Method m : methodList) {

            String mClass = m.getDeclaringClass().getSimpleName();
            String mName = m.getName();
            String expression = String.format("#{%s.%s}", mClass, mName);

            String fieldName = String.format("%s-%s", mClass, mName);
            Supplier<String> supp = () -> {
                try {
                    return faker.expression(expression);
                } catch (Exception ex) {
                    return "";
                }
            };
            ExpressionInternal ei = new ExpressionInternal(fieldName, supp);
            resultExpressionInternal.add(ei);

        }
        return resultExpressionInternal;
    }

    public List<ExpressionInternal> sampleProviderAsExpressionInternalList2(Faker faker, String providerName) {
        Function<String, String> normalizeProviderName = (s) -> {
            if (s == null) {
                return null;
            } else if (s.startsWith("*")) {
                return null;
            } else {
                return s;
            }
        };
        final String normalizedProviderName = normalizeProviderName.apply(providerName);
        final List<ExpressionInternal> resultExpressionInternal = new ArrayList<>();
        final Predicate<Method> methodProviderPredicate = (m) -> {
            boolean result = true;
            if (normalizedProviderName != null) {
                result = result && m.getParameterCount() == 0;
                result = result && m.getDeclaringClass().getSimpleName().equals(normalizedProviderName);
            } else {
                result = true;
            }
            return result;
        };
        final Predicate<Method> methodSignaturePredicate = (m) -> {
            boolean result = true;
            result = result && m.getParameterCount() == 0;
            result = result && m.getReturnType().equals(String.class);
            return result;
        };

        // Retrieve provider methods
        final List<Method> methodList = new ProvidersQueries().findAllMethodsClassesExtendingAbstractProvider().stream()
                .filter(methodProviderPredicate)
                .filter(methodSignaturePredicate)
                .sorted((m1, m2) -> m1.getClass().getName().compareTo(m2.getClass().getName()))
                .collect(Collectors.toList());

        for (Method m : methodList) {

            String mClass = m.getDeclaringClass().getSimpleName();
            String mName = m.getName();
            String expression = String.format("#{%s.%s}", mClass, mName);

            String fieldName = String.format("%s-%s", mClass, mName);
            Supplier<String> supp = () -> {
                try {
                    return faker.expression(expression);
                } catch (Exception ex) {
                    return "";
                }
            };
            ExpressionInternal ei = new ExpressionInternal(fieldName, supp);
            resultExpressionInternal.add(ei);

        }
        return resultExpressionInternal;
    }

}
