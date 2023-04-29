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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import net.datafaker.transformations.CsvTransformer;
import net.datafaker.transformations.Field;
import net.datafaker.transformations.JsonTransformer;
import net.datafaker.transformations.Schema;
import net.datafaker.transformations.SimpleField;
import net.datafaker.transformations.sql.SqlTransformer;

/**
 *
 * @author berni3
 */
public class DataFormatProcessor {

    private final Faker faker;

    public static class ExpressionInternal {

        final String fieldname;
        final Supplier<String> expressionSupplier;

        public ExpressionInternal(String fieldname, Supplier<String> expressionSupplier) {
            this.fieldname = fieldname;
            this.expressionSupplier = expressionSupplier;
        }
    }
    private final List<ExpressionInternal> expressionInternalList;

    public DataFormatProcessor(Faker faker) {
        this.faker = faker;
        this.expressionInternalList = new ArrayList<>();
    }

    Function<String, String> extractFieldname = (s) -> {
        String result = s;
        int lastIndexOfDot = s.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < s.length()) {
            result = s.substring(lastIndexOfDot + 1);
        }
        return result;
    };

    public DataFormatProcessor expressions(List<String> expressions) {
        for (String expression : expressions) {
            String fieldname = extractFieldname.apply(expression);
            Supplier<String> supp = () -> faker.expression(expression);
            expressionInternalList.add(new ExpressionInternal(fieldname, supp));
        }
        return this;
    }

    public int getCountOfExpressions() {
        return this.expressionInternalList.size();
    }

    public enum FormatEnum {
        txt, csv, tsv, json, sql, html, pdf
    }

    public String format(FormatEnum fe) {
        if (fe == FormatEnum.txt) {
            return formatTxt();
        } else if (fe == FormatEnum.csv) {
            return formatCsv();
        } else if (fe == FormatEnum.tsv) {
            return formatTsv();
        } else if (fe == FormatEnum.json) {
            return formatJson();
        } else if (fe == FormatEnum.sql) {
            return formatSql();
        } else {
            throw new RuntimeException(String.format("Unsupported format %s", fe));
        }
    }

    protected String formatTxt() {
        StringBuilder sb = new StringBuilder();
        expressionInternalList.forEach(ei -> {
            sb.append(String.format("%s: %s%n", ei.fieldname, ei.expressionSupplier.get()));
        });
        return sb.toString();

    }

    protected String formatCsv() {
        List<SimpleField<Object, String>> l = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(l.toArray(new SimpleField[0]));
        CsvTransformer transformer = CsvTransformer.<String>builder()
                .header(true)
                .separator(",")
                .build();
        int limit = 5;
        String result = transformer.generate(schema, limit);
        return result;

    }

    protected String formatTsv() {
        List<SimpleField<Object, String>> l = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(l.toArray(new SimpleField[0]));
        CsvTransformer transformer = CsvTransformer.<String>builder()
                .header(true)
                .separator("\t")
                .build();
        int limit = 5;
        String result = transformer.generate(schema, limit);
        return result;
    }

    protected String formatJson() {
        List<SimpleField<Object, String>> l = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(l.toArray(new SimpleField[0]));
        JsonTransformer transformer = JsonTransformer.<String>builder()
                .build();
        int limit = 5;
        String result = transformer.generate(schema, limit);
        return result;
    }

    protected String formatSql() {
        List<SimpleField<Object, String>> l = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(l.toArray(new SimpleField[0]));
        SqlTransformer transformer = SqlTransformer.<String>builder()
                .build();
        int limit = 5;
        String result = transformer.generate(schema, limit);
        return result;
    }
}
