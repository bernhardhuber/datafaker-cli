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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.huberb.datafaker.cli.DatafakerCli.AvailableModes;
import org.huberb.datafaker.cli.DatafakerCli.DataModes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import picocli.CommandLine;

/**
 *
 * @author berni3
 */
public class DatafakerCliTest {

    /**
     * Test of main method, of class DatafakerCli.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testMain_no_args() throws IOException {
        String[] resultsOutErr = new Invocation()
                .instance(new DatafakerCli())
                .args()
                .invoke();
        String swOut = resultsOutErr[0];
        String swErr = resultsOutErr[1];
        /* Sample:
                Hello org.huberb.datafaker.cli.DatafakerCli
                data-format-processor:
                locale: en_US
                fieldname name
                fieldname address

                result
                "name","address"
                "Tobie Goldner","Suite 889 74374 Dean Trail, Lake Wilfredo, LA 43007"
                "Mrs. Lou Kohler","Apt. 059 0867 Cora Lock, North Jacqualinetown, NC 30080"
                "Beaulah Reichert","449 Becker Plains, North Harrisonside, NY 95819"
         */
        assertEquals("", swErr);
        String outResult = swOut;
        String m = "" + outResult;

        String pattern = "\"[A-Za-z0-9 ,.\"]*\",\"[A-Za-z0-9 ,.]*\"";
        Matcher matcher = Pattern.compile(pattern).matcher(outResult);
        assertTrue(matcher.find(), m);

        assertAll(
                () -> assertTrue(outResult.contains("Hello org.huberb.datafaker.cli.DatafakerCli"), m),
                () -> assertTrue(outResult.contains("locale:"), m),
                () -> assertTrue(outResult.contains("fieldname name"), m),
                () -> assertTrue(outResult.contains("fieldname address"), m),
                () -> assertTrue(outResult.contains("\"name\",\"address\""), m)
        );
    }

    /**
     * Test of main method, of class DatafakerCli.
     *
     * @param availableModes
     * @throws java.io.IOException
     */
    @ParameterizedTest
    @EnumSource(value = AvailableModes.class)
    public void testMain_available(AvailableModes availableModes) throws IOException {
        String m = "" + availableModes;
        String[] resultsOutErr = new Invocation()
                .instance(new DatafakerCli())
                .args("--available=" + availableModes.name())
                .invoke();
        String swOut = resultsOutErr[0];
        String swErr = resultsOutErr[1];

        assertAll(
                () -> assertNotNull(swErr, m),
                () -> assertEquals("", swErr, m)
        );
        assertAll(
                () -> assertNotNull(swOut, m),
                () -> assertFalse(swOut.isEmpty(), m)
        );
        assertAll(
                () -> assertFalse(swOut.contains("Usage:"), m),
                () -> assertFalse(swOut.contains("--help"), m),
                () -> assertFalse(swOut.contains("--version"), m),
                () -> assertFalse(swOut.contains("Run datafaker from the command line"), m)
        );
    }

    /**
     * Test of main method, of class DatafakerCli.
     *
     * @param dataModes
     * @throws java.io.IOException
     */
    @ParameterizedTest
    @EnumSource(value = DataModes.class)
    public void testMain_expression(DataModes dataModes) throws IOException {
        String m = "" + dataModes;
        String[] resultsOutErr = new Invocation()
                .instance(new DatafakerCli())
                .args("--expression=" + dataModes.name(),
                        "--count=" + 3,
                        "--locale=en-US"
                )
                .invoke();
        String swOut = resultsOutErr[0];
        String swErr = resultsOutErr[1];

        assertAll(
                () -> assertNotNull(swErr, m),
                () -> assertEquals("", swErr, m)
        );
        assertAll(
                () -> assertNotNull(swOut, m),
                () -> assertFalse(swOut.isEmpty(), m)
        );
        assertAll(
                () -> assertFalse(swOut.contains("Usage:"), m),
                () -> assertFalse(swOut.contains("--help"), m),
                () -> assertFalse(swOut.contains("--version"), m),
                () -> assertFalse(swOut.contains("Run datafaker from the command line"), m)
        );
    }

    /**
     * Test of main method, of class DatafakerCli.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testMain_expression_parameters() throws IOException {
        List<String> parameters = Arrays.asList("#{Name.fullName}",
                "#{Address.fullAddress}"
        );
        String m = "" + parameters;
        String[] resultsOutErr = new Invocation()
                .instance(new DatafakerCli())
                .args("--expression=expression",
                        "--count=" + 3,
                        "--locale=en-US",
                        parameters.get(0),
                        parameters.get(1)
                )
                .invoke();
        String swOut = resultsOutErr[0];
        String swErr = resultsOutErr[1];

        assertAll(
                () -> assertNotNull(swErr, m),
                () -> assertEquals("", swErr, m)
        );
        assertAll(
                () -> assertNotNull(swOut, m),
                () -> assertFalse(swOut.isEmpty(), m)
        );
        assertAll(
                () -> assertFalse(swOut.contains("Usage:"), m),
                () -> assertFalse(swOut.contains("--help"), m),
                () -> assertFalse(swOut.contains("--version"), m),
                () -> assertFalse(swOut.contains("Run datafaker from the command line"), m)
        );
    }

    /**
     * Convenience wrapper invoking {@link DatafakerCli} with command line
     * arguments.
     */
    static class Invocation {

        DatafakerCli instance;
        String[] args;

        Invocation instance(DatafakerCli instance) {
            this.instance = instance;
            return this;
        }

        Invocation args(String... args) {
            this.args = args;
            return this;
        }

        String[] invoke() throws IOException {
            CommandLine cmd = new CommandLine(instance);
            try (StringWriter swErr = new StringWriter(); StringWriter swOut = new StringWriter()) {
                try (PrintWriter err = new PrintWriter(swErr); PrintWriter out = new PrintWriter(swOut)) {
                    cmd.setErr(err);
                    cmd.setOut(out);
                    int exitCode = cmd.execute(args);
                    assertEquals(0, exitCode);
                }
                swErr.flush();
                swOut.flush();
                String[] result = new String[2];
                result[0] = swOut.toString();
                result[1] = swErr.toString();
                return result;
            }
        }

    }
}
