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
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.transformations.CsvTransformer;
import static net.datafaker.transformations.Field.field;
import net.datafaker.transformations.JsonTransformer;
import net.datafaker.transformations.Schema;
import net.datafaker.transformations.sql.SqlDialect;
import net.datafaker.transformations.sql.SqlTransformer;
import org.huberb.datafaker.cli.DatafakerCli.ProvidersQueries;

/**
 *
 * @author berni3
 */
class SamplesGenerator {

    String sampleExpression(Faker faker) {
        String singleExpression = "#{Name.fullName}; #{Address.fullAddress}";
        String resultExpression = faker.expression(singleExpression);
        return resultExpression;
    }

    String sampleCsv(Faker faker, int limit) {
        Schema<String, String> schema = Schema.of(
                field("fullName", () -> faker.name().fullName()),
                field("fullAddress", () -> faker.address().fullAddress()));
        CsvTransformer<String> transformer = CsvTransformer.<String>builder().header(true).separator(",").build();
        String resultCsv = transformer.generate(schema, limit);
        return resultCsv;
    }

    String sampleJson(Faker faker, int limit) {
        int mode = 0;
        if (mode == 0) {
            Schema<String, String> schema = Schema.of(
                    field("fullName", () -> faker.name().fullName()),
                    field("fullAddress", () -> faker.address().fullAddress()));
            JsonTransformer<String> transformer = JsonTransformer.<String>builder().build();
            String resultJson = transformer.generate(schema, limit);
            return resultJson;
        } else if (mode == 1) {
            String fieldExpressions1_n = "fullName";
            String fieldExpressions1_v = faker.name().fullName();
            String fieldExpressions2_n = "fullAddress";
            String fieldExpressions2_v = faker.address().fullAddress();
            // mod 2: field-name, value
            String resultJson = faker.json(
                    fieldExpressions1_n, fieldExpressions1_v,
                    fieldExpressions2_n, fieldExpressions2_v);
            return resultJson;
        } else {
            return "";
        }
    }

    String sampleSql(Faker faker, int limit) {
        Schema<String, String> schema = Schema.of(
                field("fullName", () -> faker.name().fullName()),
                field("fullAddress", () -> faker.address().fullAddress()));
        SqlTransformer<String> transformer = new SqlTransformer.SqlTransformerBuilder<String>()
                .batch(5)
                .tableName("DATAFAKER_NAME_ADDRESS")
                .dialect(SqlDialect.H2)
                .build();
        String resultSql = transformer.generate(schema, limit);
        return resultSql;
    }

    String sampleProviders(Faker faker) {
        StringBuilder sb = new StringBuilder();
        // Retrieve provider methods
        Predicate<Method> p = (m) -> {
            boolean result = true;
            result = result && m.getParameterCount() == 0;
            result = result && m.getReturnType().equals(String.class);
            return result;
        };
        List<Method> methodList = new ProvidersQueries().findAllMathodsClassesExtendingAbstractProvider().stream()
                .filter(p)
                .sorted((m1, m2) -> m1.getClass().getName().compareTo(m2.getClass().getName()))
                .collect(Collectors.toList());

        String lastMClass = "";
        for (Method m : methodList) {

            String mClass = m.getDeclaringClass().getSimpleName();
            String mName = m.getName();
            String expression = String.format("#{%s.%s}", mClass, mName);
            if (!lastMClass.equals(mClass)) {
                sb.append(String.format("---%n"));
                lastMClass = mClass;
            }
            try {
                String result = faker.expression(expression);
                sb.append(String.format("%s: %s%n", expression, result));
            } catch (Exception ex) {
                sb.append(String.format("Failed sample method %s, expression %s%nexception: %s%n", m, expression, ex));
            }

        }
        String resultProviders = sb.toString();
        return resultProviders;
    }

    String sampleProviders2(Faker faker) {
        StringBuilder sb = new StringBuilder();
        Predicate<Method> mP1 = m -> {

            boolean isMatch = true;
            isMatch = isMatch && m.getParameterCount() == 0;
            isMatch = isMatch && AbstractProvider.class.isAssignableFrom(m.getReturnType());
            isMatch = isMatch && Modifier.isPublic(m.getModifiers());
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
            try {
                AbstractProvider ap = (AbstractProvider) m.invoke(faker);
                Arrays.asList(ap.getClass().getDeclaredMethods()).stream()
                        .filter(mP2)
                        .sorted((m1, m2) -> m1.getDeclaringClass().getName().compareTo(m2.getDeclaringClass().getName()))
                        .forEach(mInAbstractProvider -> {
                            try {
                                String result = (String) mInAbstractProvider.invoke(ap);
                                sb.append(String.format("ap %s#%s: %s%n", ap.getClass(), mInAbstractProvider.getName(), result));
                            } catch (Exception ex) {
                                sb.append(String.format("Failed invoking abstract provider instance method %s%n", mInAbstractProvider));
                            }

                        });
            } catch (Exception ex) {
                sb.append(String.format("Failed creating abstract provider instance using method %s%n", m, ex));
            }
        }
        String resultProviders = sb.toString();
        return resultProviders;
    }
}
