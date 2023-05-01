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
package org.huberb.datafaker.samples;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class Icd3CodesDiseasesDataTest {

    private Icd3CodesDiseasesData instance;

    @BeforeEach
    public void setUp() {
        this.instance = new Icd3CodesDiseasesData();
    }

    /**
     * Test of icd3Codes method, of class Icd3CodesDiseasesData.
     */
    @Test
    public void testIcd3Codes() {
        String[] result = instance.icd3Codes();
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.length > 0)
        );
    }

    /**
     * Test of diseases method, of class Icd3CodesDiseasesData.
     */
    @Test
    public void testDiseases() {
        String[] result = instance.diseases();
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.length > 0)
        );
    }

    /**
     * Test of diseaseByCode method, of class Icd3CodesDiseasesData.
     */
    @Test
    public void testDiseaseByCode() {
        String result = instance.diseaseByCode("D10");
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.length() > 0)
        );
    }

    /**
     * Test of diseaseByCode method, of class Icd3CodesDiseasesData.
     */
    @Test
    public void testDiseaseByCode_code_does_not_exist() {
        String result = instance.diseaseByCode("code-does-not-exists");
        assertAll(
                () -> assertNull(result)
        );

    }

}
