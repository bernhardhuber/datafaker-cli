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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.huberb.datafaker.cli.DatafakerCli.LoggingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Model.CommandSpec;

/**
 *
 * @author berni3
 */
public class HistoryGenerator {
   private static final Logger LOGGER = LoggerFactory.getLogger(HistoryGenerator.class);
    private final File historyDir = new File(System.getProperty("user.dir"));
    private final File historyFile = new File(historyDir, ".datfaker-cli-history");
    private final CommandSpec spec;
    private final LoggingSystem loggingSystem;
 

    public HistoryGenerator(CommandSpec spec) {
        this.spec = spec;
        loggingSystem = new LoggingSystem(spec);
    }

    public void writeHistoryFile() {
        try {
            writeAtFile(argsWithoutGenerateAtFileOption());
        } catch (IOException ex) {
            LOGGER.warn(String.format("Cannot write history file %s%n", historyFile.getAbsolutePath()), ex);
            loggingSystem.System_out_format("Cannot write history file %s%n"
                    + "Caused by %s",
                    historyFile.getAbsolutePath(),
                    ex.getMessage()
            );
        }
    }

    private List<String> argsWithoutGenerateAtFileOption() {
        List<String> args = spec.commandLine().getParseResult().originalArgs();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            Set<String> ignoreArgs = new HashSet(Arrays.asList("-V", "--version", "-h", "--help"));
            if (ignoreArgs.contains(arg)) {
                continue;
            }
            result.add(arg);
        }
        return result;
    }

    private void writeAtFile(List<String> args) throws IOException {
        try (FileWriter fw = new FileWriter(historyFile, true)) {
            try (PrintWriter pw = new PrintWriter(fw)) {
                pw.printf("# @%s argument file generated for %s on %s%n", historyFile, spec.qualifiedName(), new Date());
                for (String arg : args) {
                    pw.println(quoteAndEscapeBackslashes(arg));
                }
            }
        }
    }

    private String quoteAndEscapeBackslashes(String original) {
        String result = original;
        boolean needsQuotes = result.startsWith("#");
        int c;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length(); i += Character.charCount(c)) {
            c = result.codePointAt(i);
            if (Character.isWhitespace(c)) {
                needsQuotes = true;
            }
            if (c == '\\') {
                sb.append('\\'); // escape any backslashes
            }
            sb.appendCodePoint(c);
        }
        if (needsQuotes) {
            sb.insert(0, '\"').append('\"'); // quote the result
            result = sb.toString();
        }
        return result;
    }

}
