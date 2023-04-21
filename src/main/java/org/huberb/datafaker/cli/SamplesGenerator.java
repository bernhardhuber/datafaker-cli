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
import net.datafaker.transformations.CsvTransformer;
import static net.datafaker.transformations.Field.field;
import net.datafaker.transformations.JsonTransformer;
import net.datafaker.transformations.Schema;

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
        Schema<String, String> schema = Schema.of(
                field("fullName", () -> faker.name().fullName()),
                field("fullAddress", () -> faker.address().fullAddress()));
        CsvTransformer<String> transformer = CsvTransformer.<String>builder().header(true).separator(",").build();
        String resultCsv = transformer.generate(schema, limit);
        return resultCsv;
    }

    String sampleJson(Faker faker, int limit) {
        int mode = 0;
        if (mode == 0) {
            Schema<String, String> schema = Schema.of(
                    field("fullName", () -> faker.name().fullName()),
                    field("fullAddress", () -> faker.address().fullAddress()));
            JsonTransformer<String> transformer = JsonTransformer.<String>builder().build();
            String resultJson = transformer.generate(schema, limit);
            return resultJson;
        } else if (mode == 1) {
            String fieldExpressions1_n = "fullName";
            String fieldExpressions1_v = faker.name().fullName();
            String fieldExpressions2_n = "fullAddress";
            String fieldExpressions2_v = faker.address().fullAddress();
            // mod 2: field-name, value
            String resultJson = faker.json(
                    fieldExpressions1_n, fieldExpressions1_v,
                    fieldExpressions2_n, fieldExpressions2_v);
            return resultJson;
        } else {
            return "";
        }
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
