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
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import org.huberb.datafaker.cli.FakerAdapter.Locales;
import org.reflections.Reflections;
import picocli.CommandLine;
import picocli.CommandLine.Option;

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
            description = "language-tag informat {language}_{country}")
    private String languageTag;
    @Option(names = {"-c", "--count"},
            required = false,
            defaultValue = "3",
            description = "count of results")
    private int countOfResults;

    @Option(names = {"--available-locales"}, description = "shows available locales")
    boolean availableLocales;
    @Option(names = {"--available-providers"}, description = "shows available providers")
    boolean availableProviders;
    @Option(names = {"--available-provider-methods"}, description = "shows available provider methods")
    boolean availableProviderMethods;

    public static void main(String[] args) {
        final int exitCode = new CommandLine(new DatafakerCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        System_out_format("Hello %s%n", this.getClass().getName());

        if (availableLocales) {
            System_out_format("default locale: %s%n", Locales.defaultLocale().get().toLanguageTag());
            Locales.availableLocales().get().
                    stream()
                    .sorted((Locale l1, Locale l2) -> {
                        return l1.toLanguageTag().compareTo(l2.toLanguageTag());
                    }).
                    forEach(l -> System_out_format("locale : %s%n", l.toLanguageTag()));
        }
        if (availableProviders) {
            findAllClassesExtendingAbstractProvider("net.datafaker.providers.base")
                    .forEach(cl -> System_out_format("%s : %s%n", cl.getName(), cl.getSimpleName()));
        }
        if (availableProviderMethods) {
            findAllMathodsClassesExtendingAbstractProvider("net.datafaker.providers.base")
                    .forEach(l -> System_out_format("%s%n", l));
        }

        if (!availableLocales
                && !availableProviders
                && !availableProviderMethods) {
            if (languageTag == null) {
                languageTag = Locales.defaultLocale().get().toLanguageTag();
            }
            Faker faker = FakerAdapter.createFakerFromLocale(languageTag);
            String expression = "fullName: #{Name.fullName}, fullAddress: #{Address.fullAddress}";
            System_out_format("expression: %s%n", expression);
            if (countOfResults < 0) {
                countOfResults = 1;
            }
            for (int i = 0; i < countOfResults; i++) {
                String result = faker.expression(expression);
                System_out_format("%d result: %s%n", i, result);
            }
        }
        return 0;
    }

    List<Class> findAllClassesExtendingAbstractProvider(String... packageNames) {
        final List<Class> result = new Reflections(packageNames).getSubTypesOf(AbstractProvider.class)
                .stream()
                .sorted((cl1, cl2) -> cl1.getName().compareTo(cl2.getName()))
                .collect(Collectors.toList());
        return result;
    }

    void System_out_format(String format, Object... args) {
        System.out.format(format, args);
    }

    List<Method> findAllMathodsClassesExtendingAbstractProvider(String... packageNames) {
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
