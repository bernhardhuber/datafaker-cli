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

import org.huberb.datafaker.cli.ParameterParser.Parameter;
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
public class ParameterTest {

    @Test
    public void testNameValue() {
        Parameter instance = new Parameter("name", "value");
        assertEquals("name", instance.name);
        assertEquals("value", instance.value);
    }

    @Test
    public void testEquals() {
        Parameter instance1_1 = new Parameter("name1", "value1");
        Parameter instance1_2 = new Parameter("name1", "value1");
        Parameter instance2_1 = new Parameter("name2", "value2");

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
        Parameter instance1_1 = new Parameter("name1", "value1");
        Parameter instance1_2 = new Parameter("name1", "value1");
        Parameter instance2_1 = new Parameter("name2", "value2");

        assertEquals(instance1_1.hashCode(), instance1_1.hashCode());
        assertEquals(instance1_1.hashCode(), instance1_2.hashCode());
        assertNotEquals(instance1_1.hashCode(), instance2_1.hashCode());
    }

    @Test
    public void testToString() {
        Parameter instance1_1 = new Parameter("name1", "value1");
        Parameter instance1_2 = new Parameter("name1", "value1");
        Parameter instance2_1 = new Parameter("name2", "value2");

        assertEquals(instance1_1.toString(), instance1_1.toString());
        assertEquals(instance1_1.toString(), instance1_2.toString());
        assertNotEquals(instance1_1.toString(), instance2_1.toString());
    }
}
