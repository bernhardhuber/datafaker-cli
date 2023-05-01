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
 * Adopting faker-api.
 *
 * @author berni3
 */
public class Adapters {

    private Adapters() {
    }

    /**
     * Factory methods for creating a {@link Faker} instance.
     */
    public static class FakerFactory {

        /**
         * Create faker instance from language tag.
         * <p>
         * A language tag has the format language[-country].
         * <p>
         * Examples: en, de, de-AT.
         *
         * @param languageTag
         * @return a {@link Faker} instance
         */
        public static Faker createFakerFromLocale(String languageTag) {
            Locale locale = Locale.forLanguageTag(languageTag);
            return createFakerFromLocale(locale);
        }

        /**
         * Create a faker instance for a given {@link  Locale} instance.
         *
         * @param locale
         * @return a {@link Faker} instance
         */
        public static Faker createFakerFromLocale(Locale locale) {
            return new Faker(locale);
        }

        private FakerFactory() {
        }

    }

    /**
     * Suppliers providing an instance of {@link Locale}.
     */
    public static class Locales {

        /**
         * Return all available locales
         *
         * @return
         * @see Locale#getAvailableLocales()
         */
        public static Supplier<List<Locale>> availableLocales() {
            return () -> Arrays.asList(Locale.getAvailableLocales());
        }

        /**
         * Return current locale
         *
         * @return
         * @see Locale#getDefault()
         */
        public static Supplier<Locale> defaultLocale() {
            return () -> Locale.getDefault();
        }

        /**
         * Return string representation of {@link Locale} instance.
         *
         * @return
         * @see Locale#toString()
         */
        public static Function<Locale, String> localeToString() {
            return locale -> locale.toString();
        }

        private Locales() {
        }

    }
}
