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

import java.util.HashMap;
import java.util.Map;
import net.datafaker.transformations.sql.SqlDialect;
import org.huberb.datafaker.cli.DataFormatProcessor.FormatParameters.FormatterCsv;
import org.huberb.datafaker.cli.DataFormatProcessor.FormatParameters.FormatterSql;
import org.huberb.datafaker.cli.DataFormatProcessor.FormatParameters.FormatterTsv;
import org.huberb.datafaker.cli.DataFormatProcessor.FormatParameters.FormatterXml;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class FormatParametersTest {

    @Test
    public void testFormatterCsv() {

        Map<String, String> m = new HashMap<String, String>() {
            {
                put("header", "false");
                put("quote", "\'");
                put("separator", "@@");
            }
        };

        FormatterCsv formatterCsv = FormatterCsv.withMap(m);
        assertAll(
                () -> assertEquals(false, formatterCsv.header),
                () -> assertEquals('\'', formatterCsv.quote),
                () -> assertEquals("@@", formatterCsv.separator)
        );
    }

    @Test
    public void testFormatterTsv() {

        Map<String, String> m = new HashMap<String, String>() {
            {
                put("header", "false");
                put("quote", "\'");
                put("separator", "@@");
            }
        };

        FormatterTsv formatterTsv = FormatterTsv.withMap(m);
        assertAll(
                () -> assertEquals(false, formatterTsv.header),
                () -> assertEquals('\'', formatterTsv.quote),
                () -> assertEquals("@@", formatterTsv.separator)
        );
    }

    @Test
    public void testFormatterSql() {

        Map<String, String> m = new HashMap<String, String>() {
            {
                put("batch", "10");
                put("sqlDialect", "ANSI");
                put("tableName", "SOME_OTHER_TABLE_NAME");
            }
        };

        FormatterSql formatterSql = FormatterSql.withMap(m);
        assertAll(
                () -> assertEquals(10, formatterSql.batch),
                () -> assertEquals(SqlDialect.ANSI, formatterSql.sqlDialect),
                () -> assertEquals("SOME_OTHER_TABLE_NAME", formatterSql.tableName)
        );
    }

    @Test
    public void testFormatterXml() {
        Map<String, String> m = new HashMap<String, String>() {
            {
                put("pretty", "false");
                put("rootTag", "some-root-tag");
            }
        };

        FormatterXml formatterXml = FormatterXml.withMap(m);
        assertAll(
                () -> assertEquals(false, formatterXml.pretty),
                () -> assertEquals("some-root-tag", formatterXml.rootTag)
        );
    }
}
