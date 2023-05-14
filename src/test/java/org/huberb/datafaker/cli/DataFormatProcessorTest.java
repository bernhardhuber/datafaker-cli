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

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Function;
import net.datafaker.Faker;
import org.huberb.datafaker.cli.DataFormatProcessor.ExpressionInternal;
import org.huberb.datafaker.cli.DataFormatProcessor.FormatEnum;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

/**
 *
 * @author berni3
 */
public class DataFormatProcessorTest {

    static Faker faker;

    @BeforeAll
    public static void setUpAll() {
        faker = Adapters.FakerFactory.createFakerFromLocale(Locale.ENGLISH);
    }
    DataFormatProcessor instance;

    Function<String, String> stripNewLineChars = (String s) -> {
        return s.replaceAll("[\r\n]", " ")
                .replaceAll("\\s{2,}", " ");
    };

    /**
     * Test of addExpressionsFromStringList method, of class
     * DataFormatProcessor.
     */
    @Test
    public void testAddExpressionsFromStringList() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName}", "#{Address.fullAddress}"));
        assertEquals(2, instance.getCountOfExpressions());
        assertEquals(stripNewLineChars.apply("locale: en\n"
                + "fieldname Name_fullName\n"
                + "fieldname Address_fullAddress\n")
                + "", stripNewLineChars.apply(instance.textRepresentation()));
    }

    @Test
    public void testExtractFieldname() {
        instance = new DataFormatProcessor(faker);
        assertEquals("fieldname", instance.extractFieldnameExpression("fieldname:#{Name.fullName")[0]);
        assertEquals("fieldname", instance.extractFieldnameExpression("#{fieldname}")[0]);
        assertEquals("field_name", instance.extractFieldnameExpression("#{field.name}")[0]);
        assertEquals("field_name", instance.extractFieldnameExpression("#{field name}")[0]);
        assertEquals("field_name", instance.extractFieldnameExpression("#{field 'name'}")[0]);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "fieldname, fieldname:#{Name.fullName}",
        "numerify_te_st, #{numerify 'te##st'}",
        "examplify_te_st, #{examplify 'te##st'}",})
    public void testExtractFieldnameCsvValues(String expectedValue, String value) {
        instance = new DataFormatProcessor(faker);
        String[] fieldnameExpression = instance.extractFieldnameExpression(value);
        assertEquals(expectedValue, fieldnameExpression[0]);
    }

    /**
     * Test of addExpressionsFromStringList method, of class
     * DataFormatProcessor.
     */
    @Test
    public void testAddExpressionsFromExpressionInternalList() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromExpressionInternalList(Arrays.asList(
                new ExpressionInternal("fullName", () -> faker.name().fullName()),
                new ExpressionInternal("fullAddress", () -> faker.address().fullAddress())
        ));
        assertEquals(2, instance.getCountOfExpressions());
        assertEquals(stripNewLineChars.apply("locale: en\n"
                + "fieldname fullName\n"
                + "fieldname fullAddress\n")
                + "", stripNewLineChars.apply(instance.textRepresentation()));
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatTxt() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result1 = instance.formatTxt(Collections.emptyMap());
        assertTrue(!result1.isBlank(), "" + result1);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatCsv() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result2 = instance.formatCsv(Collections.emptyMap());
        assertTrue(!result2.isBlank(), "" + result2);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatTsv() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result2 = instance.formatTsv(Collections.emptyMap());
        assertTrue(!result2.isBlank(), "" + result2);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatJson() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result3 = instance.formatJson(Collections.emptyMap());
        assertTrue(!result3.isBlank(), "" + result3);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatSql() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result4 = instance.formatSql(Collections.emptyMap());
        assertTrue(!result4.isBlank(), "" + result4);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatXml() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result5 = instance.formatXml(Collections.emptyMap());
        assertTrue(!result5.isBlank(), "" + result5);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatYaml() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result5 = instance.formatYaml(Collections.emptyMap());
        assertTrue(!result5.isBlank(), "" + result5);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @ParameterizedTest
    @EnumSource()
    public void testFormatAllFormatEnumValues(FormatEnum formatEnum) {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));

        String result1 = instance.format(formatEnum, Collections.emptyMap());
        assertTrue(!result1.isBlank(), "" + result1);
    }

}
