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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Parses parameters.
 *
 * @author berni3
 */
public class ParameterParser {

    /**
     * Wrapper for parameter-name, and parameter-value.
     *
     */
    public static class Parameter {

        final String name;
        final String value;

        public Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 13 * hash + Objects.hashCode(this.name);
            hash = 13 * hash + Objects.hashCode(this.value);
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
            final Parameter other = (Parameter) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return Objects.equals(this.value, other.value);
        }

        @Override
        public String toString() {
            return "Parameter{" + "name=" + name + ", value=" + value + '}';
        }

    }

    /**
     * Parses parameters to list of {@link Parameter}.
     *
     * @param s
     * @return
     */
    public List<Parameter> parseToListOfParameters(String s) {
        List<Parameter> l = new ArrayList<>();

        for (CharLexingCtx charLexingCtx = new CharLexingCtx(s); !charLexingCtx.isEof();) {
            Parameter parameter = parse(charLexingCtx);
            l.add(parameter);
        }
        return l;
    }

    /**
     * Parses parameters to map of parameter-name, and parameter-value.
     *
     * @param s
     * @return
     */
    public Map<String, String> parseToMap(String s) {
        Map<String, String> m = new HashMap<>();
        for (CharLexingCtx charLexingCtx = new CharLexingCtx(s); !charLexingCtx.isEof();) {
            Parameter parameter = parse(charLexingCtx);
            m.put(parameter.name, parameter.value);
        }
        return m;
    }

    /**
     * Parses a parameter-name, and it's parameter-value.
     *
     * @param charLexingCtx
     * @return
     */
    Parameter parse(CharLexingCtx charLexingCtx) {
        if (!charLexingCtx.isEof()) {
            // parse parameter-name
            String parametername = charLexingCtx.parseParametername();
            // parse parameter-value
            String parametervalue = charLexingCtx.parseParametervalue();
            if (!parametername.isBlank()) {
                return new Parameter(parametername, parametervalue);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Wrap current lexing state.
     */
    static class CharLexingCtx {

        final String escChar = "\\";
        final String eqCharsPattern = ":=";
        final String sepCharPattern = ",";

        final String s;
        final int sLength;
        int offset;

        public CharLexingCtx(String s) {
            this.s = s;
            this.sLength = Optional.ofNullable(s).map(t -> t.length()).orElse(0);
            this.offset = 0;
        }

        boolean isEof() {
            return offset >= sLength;
        }

        String parseParametername() {
            StringBuilder sb = new StringBuilder();
            for (String underTest = nextChar(); !underTest.isEmpty(); underTest = nextChar()) {
                if (eqCharsPattern.contains(underTest)) {
                    break;
                }
                sb.append(underTest);
            }
            return sb.toString();
        }

        String parseParametervalue() {
            StringBuilder sb = new StringBuilder();
            for (String underTest = nextChar(); !underTest.isEmpty(); underTest = nextChar()) {
                if (sepCharPattern.contains(underTest)) {
                    break;
                }
                if (escChar.equals(underTest)) {
                    String nextChar = nextChar();
                    if (!nextChar.isEmpty()) {
                        sb.append(nextChar);
                    }
                } else {
                    sb.append(underTest);
                }
            }
            return sb.toString();
        }

        String nextChar() {
            if (offset >= sLength) {
                return "";
            }
            String result = s.substring(offset, offset + 1);
            offset += 1;
            return result;
        }

    }
}
