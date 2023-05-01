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
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import net.datafaker.transformations.CsvTransformer;
import net.datafaker.transformations.Field;
import net.datafaker.transformations.JsonTransformer;
import net.datafaker.transformations.JsonTransformer.JsonTransformerBuilder.FormattedAs;
import net.datafaker.transformations.Schema;
import net.datafaker.transformations.SimpleField;
import net.datafaker.transformations.XmlTransformer;
import net.datafaker.transformations.XmlTransformer.XmlTransformerBuilder;
import net.datafaker.transformations.YamlTransformer;
import net.datafaker.transformations.sql.SqlDialect;
import net.datafaker.transformations.sql.SqlTransformer;

/**
 * Processor accepting some data, and formats it.
 * <p>
 * Data may be provided as faker expression, or as pair of field name, and
 * string supplier.
 * <p>
 * Supported formats see {@link FormatEnum}.
 *
 * @author berni3
 */
public class DataFormatProcessor {

    private static final int LIMIT_DEFAULT_VALUE = 3;

    private final Faker faker;
    private final List<ExpressionInternal> expressionInternalList;
    private final int limit;

    Function<String, String> extractFieldname = (s) -> {
        String result = s;
        int lastIndexOfDot = s.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < s.length()) {
            result = s.substring(lastIndexOfDot + 1);
        }
        result = result.replace('{', ' ')
                .replace('}', ' ').trim();
        return result;
    };

    /**
     * Create new instance.
     * <p>
     * Use limit default value 3.
     *
     * @param faker instance
     */
    public DataFormatProcessor(Faker faker) {
        this(faker, LIMIT_DEFAULT_VALUE);
    }

    /**
     * Create new instance
     *
     * @param faker instance
     * @param limit number of data records created. If limit is less than 1 use
     * default value 3.
     */
    public DataFormatProcessor(Faker faker, int limit) {
        this.faker = faker;
        this.expressionInternalList = new ArrayList<>();
        if (limit <= 0) {
            limit = LIMIT_DEFAULT_VALUE;
        }
        this.limit = limit;
    }

    /**
     * Add a data-expression.
     * <p>
     * Data is represented by a {@link ExpressionInternal} instance.
     *
     * @param expressions
     * @return
     */
    public DataFormatProcessor addExpressionsFromExpressionInternalList(List<ExpressionInternal> expressions) {
        expressionInternalList.clear();
        expressionInternalList.addAll(expressions);
        return this;
    }

    /**
     * Add a data-expression.
     * <p>
     * Data is represented by a faker expression.
     *
     * @param expressions
     * @return
     * @see Faker#expression(java.lang.String)
     */
    public DataFormatProcessor addExpressionsFromStringList(List<String> expressions) {
        for (String expression : expressions) {
            String fieldname = extractFieldname.apply(expression);
            Supplier<String> supp = () -> faker.expression(expression);
            expressionInternalList.add(new ExpressionInternal(fieldname, supp));
        }
        return this;
    }

    /**
     * Return number of expressions added.
     *
     * @return
     */
    public int getCountOfExpressions() {
        return this.expressionInternalList.size();
    }

    /**
     * Return a text representation of this instance.
     *
     * @return
     */
    public String textRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("locale: %s%n", faker.getContext().getLocale()));
        expressionInternalList.forEach(ei -> {
            sb.append(String.format("fieldname %s%n", ei.fieldname));
        });
        return sb.toString();
    }

    /**
     * Format data for a given format type.
     *
     * @param fe represents the format type
     * @return
     * @see FormatEnum
     */
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
        } else if (fe == FormatEnum.xml) {
            return formatXml();
        } else if (fe == FormatEnum.yaml) {
            return formatYaml();
        } else {
            throw new RuntimeException(String.format("Unsupported format %s", fe));
        }
    }

    //-------------------------------------------------------------------------
    protected String formatTxt() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            expressionInternalList.forEach(ei -> {
                sb.append(String.format("%s: %s%n", ei.fieldname, ei.expressionSupplier.get()));
            });
        }
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

        String result = transformer.generate(schema, limit);
        return result;

    }

    protected String formatTsv() {
        List<SimpleField<Object, String>> simpleFields = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(simpleFields.toArray(new SimpleField[0]));
        CsvTransformer transformer = CsvTransformer.<String>builder()
                .header(true)
                .separator("\t")
                .build();

        String result = transformer.generate(schema, limit);
        return result;
    }

    protected String formatJson() {
        List<SimpleField<Object, String>> simpleFields = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(simpleFields.toArray(new SimpleField[0]));
        JsonTransformer transformer = JsonTransformer.<String>builder()
                .formattedAs(FormattedAs.JSON_ARRAY)
                .build();
        String result = transformer.generate(schema, limit);
        return result;
    }

    protected String formatSql() {
        List<SimpleField<Object, String>> simpleFields = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(simpleFields.toArray(new SimpleField[0]));
        SqlTransformer transformer = SqlTransformer.<String>builder()
                .batch(5)
                .tableName("DATAFAKER_TABLE")
                .dialect(SqlDialect.H2)
                .build();

        String result = transformer.generate(schema, limit);
        return result;
    }

    protected String formatXml() {
        List<SimpleField<Object, String>> simpleFields = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(simpleFields.toArray(new SimpleField[0]));
        XmlTransformer transformer = new XmlTransformerBuilder<String>()
                .build();

        CharSequence result = transformer.generate(schema, limit);
        StringBuilder sb = new StringBuilder();
        sb.append("<root>")
                .append(System.lineSeparator())
                .append(result)
                .append(System.lineSeparator())
                .append("</root>");
        return sb.toString();
    }

    protected String formatYaml() {
        List<SimpleField<Object, String>> simpleFields = expressionInternalList.stream()
                .map(ei -> Field.field(ei.fieldname, ei.expressionSupplier))
                .collect(Collectors.toList());
        Schema<Object, String> schema = Schema.of(simpleFields.toArray(new SimpleField[0]));
        YamlTransformer transformer = new YamlTransformer<String>();

        String result = transformer.generate(schema, limit);
        return result;
    }

    /**
     * Internal wrapper representing a field name, and its faker-value.
     */
    public static class ExpressionInternal {

        final String fieldname;
        final Supplier<String> expressionSupplier;

        public ExpressionInternal(String fieldname, Supplier<String> expressionSupplier) {
            this.fieldname = fieldname;
            this.expressionSupplier = expressionSupplier;
        }

        @Override
        public int hashCode() {
            int hash = LIMIT_DEFAULT_VALUE;
            hash = 59 * hash + Objects.hashCode(this.fieldname);
            hash = 59 * hash + Objects.hashCode(this.expressionSupplier);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ExpressionInternal other = (ExpressionInternal) obj;
            if (!Objects.equals(this.fieldname, other.fieldname)) {
                return false;
            }
            return Objects.equals(this.expressionSupplier, other.expressionSupplier);
        }

        @Override
        public String toString() {
            return "ExpressionInternal{" + "fieldname=" + fieldname + ", expressionSupplier=" + expressionSupplier + '}';
        }

    }

    /**
     * Enumeration of supported formats
     */
    public enum FormatEnum {
        txt, csv, tsv, json, sql, xml, yaml
    }
}
