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

import java.util.List;
import java.util.Map;
import org.huberb.datafaker.cli.ParameterParser.CharLexingCtx;
import org.huberb.datafaker.cli.ParameterParser.Parameter;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author berni3
 */
public class ParameterParserTest {

    ParameterParser instance;

    @BeforeEach
    public void setUp() {
        this.instance = new ParameterParser();
    }

    /**
     * Test of parseToListOfParameters method, of class ParameterParser.
     */
    @Test
    public void testParseToListOfParameters() {
        {
            List<Parameter> result = instance.parseToListOfParameters("a=A1");
            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals("a", result.get(0).name),
                    () -> assertEquals("A1", result.get(0).value)
            );
        }
        {
            List<Parameter> result = instance.parseToListOfParameters("a=A1,b=B1");
            assertAll(
                    () -> assertEquals(2, result.size()),
                    () -> assertEquals("a", result.get(0).name),
                    () -> assertEquals("A1", result.get(0).value),
                    () -> assertEquals("b", result.get(1).name),
                    () -> assertEquals("B1", result.get(1).value)
            );
        }
    }

    /**
     * Test of parseToMap method, of class ParameterParser.
     */
    @Test
    public void testParseToMap() {
        {
            Map<String, String> result = instance.parseToMap("a=A1");
            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals("A1", result.get("a"))
            );
        }
        {
            Map<String, String> result = instance.parseToMap("a=A1,b=B1");
            assertAll(
                    () -> assertEquals(2, result.size()),
                    () -> assertEquals("A1", result.get("a")),
                    () -> assertEquals("B1", result.get("b"))
            );
        }
    }

    /**
     * Test of parse method, of class ParameterParser.
     */
    @Test
    public void testParse() {
        {
            CharLexingCtx charLexingCtx = new CharLexingCtx("a=A1");
            Parameter parameter = instance.parse(charLexingCtx);
            assertNotNull(parameter);
            assertEquals("a", parameter.name);
            assertEquals("A1", parameter.value);

        }
        {
            CharLexingCtx charLexingCtx = new CharLexingCtx("a=A\"1");
            Parameter parameter = instance.parse(charLexingCtx);
            assertNotNull(parameter);
            assertEquals("a", parameter.name);
            assertEquals("A\"1", parameter.value);

        }
        {
            CharLexingCtx charLexingCtx = new CharLexingCtx("a=A\\\"1");
            Parameter parameter = instance.parse(charLexingCtx);
            assertNotNull(parameter);
            assertEquals("a", parameter.name);
            assertEquals("A\"1", parameter.value);

        }
    }

    /**
     * Test of parse method, of class ParameterParser.
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "=X"})
    public void testParse_parameter_null(String s) {
        {
            CharLexingCtx charLexingCtx = new CharLexingCtx(s);
            Parameter parameter = instance.parse(charLexingCtx);
            assertNull(parameter);
        }
    }

}
