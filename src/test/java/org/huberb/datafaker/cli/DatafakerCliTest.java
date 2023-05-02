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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

/**
 *
 * @author berni3
 */
public class DatafakerCliTest {

    DatafakerCli instance;

    @BeforeEach
    public void setUp() {
        instance = new DatafakerCli();
    }

    /**
     * Test of main method, of class DatafakerCli.
     */
    @Test
    public void testMain_no_args() throws IOException {
        CommandLine cmd = new CommandLine(instance);
        try (StringWriter swErr = new StringWriter(); StringWriter swOut = new StringWriter()) {
            try (PrintWriter err = new PrintWriter(swErr); PrintWriter out = new PrintWriter(swOut)) {
                cmd.setErr(err);
                cmd.setOut(out);
                int exitCode = cmd.execute("");
                assertEquals(0, exitCode);
            }
            swErr.flush();
            swOut.flush();
            /*
                Hello org.huberb.datafaker.cli.DatafakerCli
                expression: locale: en_US
                fieldname name
                fieldname address

                result
                "name","address"
                "Tobie Goldner","Suite 889 74374 Dean Trail, Lake Wilfredo, LA 43007"
                "Mrs. Lou Kohler","Apt. 059 0867 Cora Lock, North Jacqualinetown, NC 30080"
                "Beaulah Reichert","449 Becker Plains, North Harrisonside, NY 95819"
             */
            assertEquals("", swErr.toString());
            String outResult = swOut.toString();
            String m = "" + outResult;

            String pattern = "\"[A-Za-z0-9 ,.\"]*\",\"[A-Za-z0-9 ,.]*\"";
            Matcher matcher = Pattern.compile(pattern).matcher(outResult);
            assertTrue(matcher.find(), m);

            assertAll(
                    () -> assertTrue(outResult.contains("locale:"), m),
                    () -> assertTrue(outResult.contains("fieldname name"), m),
                    () -> assertTrue(outResult.contains("fieldname address"), m),
                    () -> assertTrue(outResult.contains("\"name\",\"address\""), m)
            );
        }
    }

}
