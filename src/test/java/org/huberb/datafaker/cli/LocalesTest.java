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
import java.util.Locale;
import org.huberb.datafaker.cli.Adapters.Locales;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class LocalesTest {

    @Test
    public void testAvailableLocales() {
        List<Locale> localeList = Locales.availableLocales().get();
        assertAll(
                () -> assertNotNull(localeList),
                () -> assertTrue(!localeList.isEmpty())
        );
    }

    @Test
    public void testDefaultLocale() {
        Locale result = Locales.defaultLocale().get();
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.toString().isBlank())
        );
    }

    @Test
    public void testLocaleToString() {
        String result = Locales.localeToString().apply(Locale.GERMAN);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isBlank())
        );
    }
}
