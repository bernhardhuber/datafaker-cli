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
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import net.datafaker.Faker;

/**
 *
 * @author berni3
 */
public class FakerAdapter {

    public static Faker createFakerFromLocale(String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return createFakerFromLocale(locale);
    }

    public static Faker createFakerFromLocale(Locale locale) {
        return new Faker(locale);
    }

    final Faker faker;

    public FakerAdapter(Faker faker) {
        this.faker = faker;
    }

    String expression(String expression) {
        return faker.expression(expression);
    }

    String csv(int limit, String... columnExpressions) {
        return faker.csv(limit, columnExpressions);
    }

    static class Locales {

        public static Supplier<List<Locale>> availableLocales() {
            return () -> Arrays.asList(Locale.getAvailableLocales());
        }

        public static Supplier<Locale> defaultLocale() {
            return () -> Locale.getDefault();
        }

        public static Function<Locale, String> localeToString() {
            return locale -> locale.toString();
        }
    }
}
