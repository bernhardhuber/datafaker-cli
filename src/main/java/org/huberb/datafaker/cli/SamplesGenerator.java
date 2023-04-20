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

import net.datafaker.Faker;

/**
 *
 * @author berni3
 */
class SamplesGenerator {

    String sampleExpression(Faker faker) {
        String singleExpression = "#{Name.fullName}; #{Address.fullAddress}";
        String resultExpression = faker.expression(singleExpression);
        return resultExpression;
    }

    String sampleCsv(Faker faker, int limit) {
        String columnExpressions1_n = "fullName";
        String columnExpressions1_v = "#{Name.fullName}";
        String columnExpressions2_n = "fullAddress";
        String columnExpressions2_v = "#{Address.fullAddress}";
        // mod 2: column-name, value
        String resultCsv = faker.csv(
                limit,
                columnExpressions1_n, columnExpressions1_v,
                columnExpressions2_n, columnExpressions2_v);
        return resultCsv;
    }

    String sampleCsv(Faker faker, int limit, String separator, char quote, boolean withHeader) {
        String columnExpressions1_n = "fullName";
        String columnExpressions1_v = "#{Name.fullName}";
        String columnExpressions2_n = "fullAddress";
        String columnExpressions2_v = "#{Address.fullAddress}";
        // mod 2: column-name, value
        String resultCsv = faker.csv(separator, quote, withHeader,
                limit,
                columnExpressions1_n, columnExpressions1_v,
                columnExpressions2_n, columnExpressions2_v);
        return resultCsv;
    }

    String sampleJson(Faker faker) {
        String fieldExpressions1_n = "fullName";
        String fieldExpressions1_v = "#{Name.fullName}";
        String fieldExpressions2_n = "fullAddress";
        String fieldExpressions2_v = "#{Address.fullAddress}";
        // mod 2: field-name, value
        String resultJson = faker.json(
                fieldExpressions1_n, fieldExpressions1_v,
                fieldExpressions2_n, fieldExpressions2_v);
        return resultJson;
    }

    String sampleJsona(Faker faker, int limit) {
        String limitAsString = "" + limit;
        String fieldExpressions1_n = "fullName";
        String fieldExpressions1_v = "#{Name.fullName}";
        String fieldExpressions2_n = "fullAddress";
        String fieldExpressions2_v = "#{Address.fullAddress}";
        // json array mod 3: length, name, value
        String resultJsona = faker.jsona(
                limitAsString, fieldExpressions1_n, fieldExpressions1_v,
                limitAsString, fieldExpressions2_n, fieldExpressions2_v);
        return resultJsona;
    }

}
