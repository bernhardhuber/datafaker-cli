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
import java.util.Locale;
import net.datafaker.Faker;
import org.huberb.datafaker.cli.DataFormatProcessor.FormatEnum;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class DataFormatProcessorTest {

    static Faker faker;

    @BeforeAll
    static void setUpAll() {
        faker = Adapters.FakerFactory.createFakerFromLocale(Locale.getDefault());
    }
    DataFormatProcessor instance;

    /**
     * Test of expressions method, of class DataFormatProcessor.
     */
    @Test
    public void testExpressions() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        assertEquals(2, instance.getCountOfExpressions());
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatTxt() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result1 = instance.formatTxt();
        assertTrue(!result1.isBlank(), "" + result1);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatCsv() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result2 = instance.formatCsv();
        assertTrue(!result2.isBlank(), "" + result2);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatTsv() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result2 = instance.formatTsv();
        assertTrue(!result2.isBlank(), "" + result2);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatJson() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result3 = instance.formatJson();
        assertTrue(!result3.isBlank(), "" + result3);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormatSql() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        String result4 = instance.formatSql();
        assertTrue(!result4.isBlank(), "" + result4);
    }

    /**
     * Test of format method, of class DataFormatProcessor.
     */
    @Test
    public void testFormat() {
        instance = new DataFormatProcessor(faker);
        instance.addExpressionsFromStringList(Arrays.asList("#{Name.fullName", "#{Address.fullAddress}"));
        {
            String result1 = instance.format(FormatEnum.txt);
            assertTrue(!result1.isBlank(), "" + result1);
        }
        {
            String result2 = instance.format(FormatEnum.csv);
            assertTrue(!result2.isBlank(), "" + result2);
        }
        {
            String result2 = instance.format(FormatEnum.tsv);
            assertTrue(!result2.isBlank(), "" + result2);
        }
        {
            String result3 = instance.format(FormatEnum.json);
            assertTrue(!result3.isBlank(), "" + result3);
        }
        {
            String result4 = instance.format(FormatEnum.sql);
            assertTrue(!result4.isBlank(), "" + result4);
        }
    }

}