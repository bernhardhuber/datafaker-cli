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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import net.datafaker.providers.base.File;
import org.huberb.datafaker.cli.Adapters.FakerFactory;
import org.huberb.datafaker.cli.Adapters.Locales;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

/**
 * A command line program for {@link Faker}.
 *
 * @author berni3
 */
@CommandLine.Command(name = "DatafakerCli",
        mixinStandardHelpOptions = true,
        showAtFileInUsageHelp = true,
        showDefaultValues = true,
        version = "DatafakerCli 0.1-SNAPSHOT",
        description = "Run datafaker from the command line%n"
)
public class DatafakerCli implements Callable<Integer> {

    private static final int DEFAULT_COUNT_OF_RESULT = 3;
    private static final int MAX_COUNT_OF_RESULT = 10000;

    //-------------------------------------------------------------------------
    /**
     * Command line entry point.
     *
     * @param args
     */
    public static void main(String[] args) {
        final int exitCode = new CommandLine(new DatafakerCli()).execute(args);
        System.exit(exitCode);
    }

    @Spec
    private CommandSpec spec;

    @Option(names = {"-l", "--locale"},
            required = false,
            description = "language-tag in format {language}[-{country}]."
            + "eg. en, de, de-AT; see also --available==locales output")
    private String languageTag;

    @Option(names = {"-a", "--available"},
            description = "Valid values: ${COMPLETION-CANDIDATES}")
    private AvailableModes availableModes;

    @Option(names = {"-c", "--count"},
            required = false,
            defaultValue = "" + DEFAULT_COUNT_OF_RESULT,
            description = "Count of results. Minimum value: 0, Maximum value: " + MAX_COUNT_OF_RESULT + ".")
    private int countOfResults;

    @Option(names = {"-e", "--expression"},
            required = false,
            defaultValue = "sample",
            description = "Valid values: ${COMPLETION-CANDIDATES}."
    )
    private DataModes dataModes;

    @Option(names = {"-f", "--format"},
            defaultValue = "csv",
            description = "Valid values: ${COMPLETION-CANDIDATES}.")
    private DataFormatProcessor.FormatEnum formatEnum;

    @Option(names = {"--format-parameter"},
            defaultValue = "",
            description = "Define format parameter")
    private String formatParameters;
    @Option(names = {"-o", "--output-file"},
            defaultValue = "",
            description = "TODO: Write values to a file")
    private File outpuFile;

    @Parameters(index = "0..*", description = "expression arguments.")
    private List<String> expressions;

    //-------------------------------------------------------------------------
    /**
     * Entry point running this application.
     *
     * @return
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        System_out_format("Hello %s%n", this.getClass().getName());

        if (availableModes != null) {
            handleAvailableModes(availableModes);
        } else {
            handleDataFormat();
        }
        return 0;
    }

    private void handleAvailableModes(AvailableModes availableModes) {
        if (availableModes == AvailableModes.locales) {
            System_out_format("default locale: %s%n", Locales.defaultLocale().get().toLanguageTag());
            Locales.availableLocales().get().
                    stream()
                    .sorted((Locale l1, Locale l2) -> {
                        return l1.toLanguageTag().compareTo(l2.toLanguageTag());
                    }).
                    forEach(l -> System_out_format("locale : %s%n", l.toLanguageTag()));
        } else if (availableModes == AvailableModes.providers) {
            new ProvidersQueries()
                    .findAllClassesExtendingAbstractProvider()
                    .forEach(clazz -> System_out_format("%s : %s%n", clazz.getName(), clazz.getSimpleName()));
        } else if (availableModes == AvailableModes.providerMethods1) {
            new ProvidersQueries()
                    .findAllMethodsClassesExtendingAbstractProvider()
                    .forEach(method -> System_out_format("%s%n", method));
        } else if (availableModes == AvailableModes.providerMethods2) {
            new ProvidersQueries()
                    .findAllMethodsClassesExtendingAbstractProvider()
                    .stream()
                    .collect(Collectors.groupingBy(m -> m.getDeclaringClass().getName()))
                    .forEach((String k, List<Method> v) -> {
                        System_out_format("%nClass: %s%n", k);
                        v.forEach(method -> System_out_format("%s.%s, #args %d%n",
                                method.getDeclaringClass().getSimpleName(),
                                method,
                                method.getParameterCount()
                        ));
                    });
        }
    }

    private void handleDataFormat() {
        // step 0: init
        final Faker faker = createTheFaker();
        UnaryOperator<Integer> normalizeCountOfResults = (i) -> {
            if (i == null || i <= 0) {
                return DEFAULT_COUNT_OF_RESULT;
            } else if (i > MAX_COUNT_OF_RESULT) {
                return MAX_COUNT_OF_RESULT;
            }
            return i;
        };
        Predicate<List<String>> isNotEmpty = l -> l != null && !l.isEmpty();

        final DataFormatProcessor dataFormatProcessor = new DataFormatProcessor(faker,
                normalizeCountOfResults.apply(this.countOfResults));

        // step 1: data
        if (this.dataModes == DataModes.sample) {
            SamplesGenerator samplesGenerator = new SamplesGenerator();
            dataFormatProcessor.addExpressionsFromExpressionInternalList(samplesGenerator.sampleExpressions(faker));
        } else if (this.dataModes == DataModes.expression && isNotEmpty.test(expressions)) {
            dataFormatProcessor.addExpressionsFromStringList(expressions);
        } else if (this.dataModes == DataModes.sampleProvider) {
            List<String> providerNames = Collections.emptyList();
            if (isNotEmpty.test(expressions)) {
                providerNames = new ArrayList<>();
                providerNames.addAll(expressions);
            }
            SamplesGenerator sampleGenerator = new SamplesGenerator();
            dataFormatProcessor.addExpressionsFromExpressionInternalList(sampleGenerator.sampleProviderAsExpressionInternalList2(faker, providerNames));
        }

        if (dataFormatProcessor.getCountOfExpressions() == 0) {
            dataFormatProcessor.addExpressionsFromStringList(Arrays.asList(
                    "#{Name.fullName}",
                    "#{Address.fullAddress}"));
        }
        System_out_format("data-format-processor:%n%s%n", dataFormatProcessor.textRepresentation());
        // step 2: format
        ParameterParser parameterParser = new ParameterParser();
        Map<String, String> m = parameterParser.parseToMap(formatParameters);
        String result = dataFormatProcessor.format(formatEnum, m);
        System_out_format("result%n%s%n", result);
    }

    Faker createTheFaker() {
        final String theLanguageTag = Optional.ofNullable(this.languageTag)
                .orElse(Locales.defaultLocale().get().toLanguageTag());
        return FakerFactory.createFakerFromLocale(theLanguageTag);
    }

    private void System_out_format(String format, Object... args) {
        //System.out.format(format, args);
        spec.commandLine().getOut().format(format, args);
    }

    enum AvailableModes {
        locales, providers, providerMethods1, providerMethods2
    }

    enum DataModes {
        expression, sample, sampleProvider
    }

}
