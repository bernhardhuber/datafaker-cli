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

import java.util.Locale;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class FakerTest {

    static Faker faker;

    @BeforeAll
    public static void setUpAll() {
        faker = Adapters.FakerFactory.createFakerFromLocale(Locale.getDefault());
    }

    @Test
    public void testRelationships_direct() {
        String result = faker.relationships().direct();
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(!result.isBlank(), "" + result)
        );
    }
}
