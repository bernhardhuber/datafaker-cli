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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import org.huberb.datafaker.cli.Adapters.FakerFactory;
import org.huberb.datafaker.cli.Adapters.Locales;
import org.reflections.Reflections;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
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

    @Option(names = {"-l", "--locale"},
            required = false,
            description = "language-tag in format {language}_{country}.")
    private String languageTag;
    @Option(names = {"-c", "--count"},
            required = false,
            defaultValue = "3",
            description = "count of results.")
    private int countOfResults;

    @Option(names = {"--expression"},
            required = false,
            description = "TODO describe me.")
    private boolean expressionOption;

    @Parameters(index = "0..*", description = "expression arguments.")
    private List<String> expressions;

    enum AvailableModes {
        locales, providers, providerMethods1, providerMethods2
    }
    @Option(names = {"--available"}, description = "Valid values: ${COMPLETION-CANDIDATES}")
    AvailableModes availableModes;

    enum SampleModes {
        expression, csv, json, sql, providers
    }
    @Option(names = {"--samples"}, description = "Valid values: ${COMPLETION-CANDIDATES}")
    SampleModes sampleModes;

    public static void main(String[] args) {
        final int exitCode = new CommandLine(new DatafakerCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        System_out_format("Hello %s%n", this.getClass().getName());
        if (countOfResults < 0) {
            countOfResults = 1;
        }

        if (availableModes != null) {
            handleAvailableModes(availableModes);
        } else if (sampleModes != null) {
            handleSampleModes(sampleModes);
        } else {
            String expression = "fullName: #{Name.fullName}, fullAddress: #{Address.fullAddress}";
            if (expressionOption && expressions != null && !expressions.isEmpty()) {
                expression = expressions.get(0);
            }
            System_out_format("expression: %s%n", expression);
            Faker faker = createTheFaker();
            for (int i = 0; i < countOfResults; i++) {
                String result = faker.expression(expression);
                System_out_format("%d result: %s%n", i, result);
            }
        }
        return 0;
    }

    Faker createTheFaker() {
        final String theLanguageTag = Optional.ofNullable(this.languageTag).orElse(Locales.defaultLocale().get().toLanguageTag());
        Faker faker = FakerFactory.createFakerFromLocale(theLanguageTag);
        return faker;
    }

    private void System_out_format(String format, Object... args) {
        System.out.format(format, args);
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
                    .findAllMathodsClassesExtendingAbstractProvider()
                    .forEach(method -> System_out_format("%s%n", method));
        } else if (availableModes == AvailableModes.providerMethods2) {
            new ProvidersQueries()
                    .findAllMathodsClassesExtendingAbstractProvider()
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

    private void handleSampleModes(SampleModes sampleModes) {
        SamplesGenerator samplesGenerator = new SamplesGenerator();
        Faker faker = createTheFaker();
        if (sampleModes == SampleModes.expression) {
            String sampleResult = samplesGenerator.sampleExpression(faker);
            System_out_format("sample expression%n%s%n", sampleResult);
        } else if (sampleModes == SampleModes.csv) {
            String sampleResult = samplesGenerator.sampleCsv(faker, this.countOfResults);
            System_out_format("sample csv%n%s%n", sampleResult);
        } else if (sampleModes == SampleModes.json) {
            String sampleResult = samplesGenerator.sampleJson(faker, this.countOfResults);
            System_out_format("sample json%n%s%n", sampleResult);
        } else if (sampleModes == SampleModes.sql) {
            String sampleResult = samplesGenerator.sampleSql(faker, this.countOfResults);
            System_out_format("sample sql%n%s%n", sampleResult);
        } else if (sampleModes == SampleModes.providers) {
            String sampleProviders = samplesGenerator.sampleProviders2(faker);
            System_out_format("sample providers%n%s%n", sampleProviders);
        }
    }

    /**
     * Query for classes, or methods extending {@link AbstractProvider}.
     */
    static class ProvidersQueries {

        final String[] defaultProviderPackages = new String[]{"net.datafaker.providers.base",
            "net.datafaker.providers.entertainment",
            "net.datafaker.providers.food",
            "net.datafaker.providers.sport",
            "net.datafaker.providers.videogame"
        };

        List<Class> findAllClassesExtendingAbstractProvider() {
            String[] packageNames = defaultProviderPackages;
            final List<Class> result = new Reflections(packageNames).getSubTypesOf(AbstractProvider.class)
                    .stream()
                    .sorted((cl1, cl2) -> cl1.getName().compareTo(cl2.getName()))
                    .collect(Collectors.toList());
            return result;
        }

        List<Method> findAllMathodsClassesExtendingAbstractProvider() {
            String[] packageNames = defaultProviderPackages;
            final Set<String> ignoreMethods = new HashSet<>() {
                {
                    add("getFaker");
                    add("getClass");
                    add("equals");
                    add("hashCode");
                    add("toString");
                    add("wait");
                    add("notify");
                    add("notifyAll");
                }
            };
            final List<Method> result4 = new Reflections(packageNames)
                    .getSubTypesOf(AbstractProvider.class)
                    .stream()
                    .sorted((cl1, cl2) -> cl1.getName().compareTo(cl2.getName()))
                    .flatMap(cl -> Arrays.asList(cl.getMethods()).stream())
                    .filter(m -> !ignoreMethods.contains(m.getName()))
                    .collect(Collectors.toList());
            return result4;

        }
    }
}
