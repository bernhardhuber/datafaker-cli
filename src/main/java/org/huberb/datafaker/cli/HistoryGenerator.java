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
    private final Set<String> ignoreArgs = new HashSet(Arrays.asList("-V", "--version", "-h", "--help", "--generate-history"));
    
    public HistoryGenerator(CommandSpec spec) {
        this.spec = spec;
        loggingSystem = new LoggingSystem(spec);
    }
    
    public void writeHistoryFile() {
        try {
            writeAtFile(argsWithoutGenerateAtFileOption());
        } catch (IOException ex) {
            LOGGER.warn(String.format("Cannot write history file %s%n", historyFile.getAbsolutePath()), ex);
        }
    }
    
    private List<String> argsWithoutGenerateAtFileOption() {
        final List<String> result = new ArrayList<>();
        
        final List<String> args = spec.commandLine().getParseResult().originalArgs();
        args.stream()
                .filter(s -> !ignoreArgs.contains(s))
                .forEach(result::add);
        return result;
    }
    
    private void writeAtFile(List<String> args) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(historyFile, true))) {
            pw.printf("%n# generated for %s on %s%n", spec.qualifiedName(), new Date());
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(quoteAndEscapeBackslashes(arg)).append(' ');
            }
            pw.println(sb.toString());
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
