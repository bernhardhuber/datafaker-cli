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

import org.huberb.datafaker.cli.DataFormatProcessor.ExpressionInternal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class ExpressionInternalTest {

    @Test
    public void testNameValue() {
        ExpressionInternal instance = new ExpressionInternal("fieldname", () -> "supplier");
        assertEquals("fieldname", instance.fieldname);
        assertEquals("supplier", instance.expressionSupplier.get());
    }

    @Test
    public void testEquals() {
        ExpressionInternal instance1_1 = new ExpressionInternal("fieldname1", () -> "supplier1");
        ExpressionInternal instance1_2 = new ExpressionInternal("fieldname1", () -> "supplier1");
        ExpressionInternal instance2_1 = new ExpressionInternal("fieldname2", () -> "supplier2");

        //---
        assertFalse(instance1_1.equals(null));
        assertNotEquals(instance1_1, null);
        //---
        assertTrue(instance1_1.equals(instance1_1));
        assertTrue(instance1_1.equals(instance1_2));
        assertEquals(instance1_1, instance1_1);
        assertEquals(instance1_1, instance1_2);
        assertSame(instance1_1, instance1_1);
        assertNotSame(instance1_1, instance1_2);
        //---
        assertNotEquals(instance1_1, instance2_1);
        assertNotSame(instance1_1, instance1_2);
    }

    @Test
    public void testHashcode() {
        ExpressionInternal instance1_1 = new ExpressionInternal("fieldname1", () -> "supplier1");
        ExpressionInternal instance1_2 = new ExpressionInternal("fieldname1", () -> "supplier1");
        ExpressionInternal instance2_1 = new ExpressionInternal("fieldname2", () -> "supplier2");

        assertEquals(instance1_1.hashCode(), instance1_1.hashCode());
        assertEquals(instance1_1.hashCode(), instance1_2.hashCode());
        assertNotEquals(instance1_1.hashCode(), instance2_1.hashCode());
    }
}
