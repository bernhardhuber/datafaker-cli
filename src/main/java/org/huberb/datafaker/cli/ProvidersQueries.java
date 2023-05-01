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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.datafaker.providers.base.AbstractProvider;
import org.reflections.Reflections;

/**
 * Query for classes, or methods extending {@link AbstractProvider}.
 */
class ProvidersQueries {

    final String[] defaultProviderPackages = new String[]{
        "net.datafaker.providers.base",
        "net.datafaker.providers.entertainment",
        "net.datafaker.providers.food",
        "net.datafaker.providers.sport",
        "net.datafaker.providers.videogame"};

    /**
     * Find all classes being subtype of {@link  AbstractProvider}.
     *
     * @return
     */
    List<Class> findAllClassesExtendingAbstractProvider() {
        final Comparator<Class> classNameComparator = (Class cl1, Class cl2) -> cl1.getName().compareTo(cl2.getName());
        final String[] packageNames = defaultProviderPackages;
        final List<Class> result = new Reflections(packageNames)
                .getSubTypesOf(AbstractProvider.class)
                .stream()
                .sorted(classNameComparator)
                .collect(Collectors.toList());
        return result;
    }

    /**
     * Find all methods of all classes being subtype of
     * {@link  AbstractProvider}.
     *
     * @return
     */
    List<Method> findAllMethodsClassesExtendingAbstractProvider() {
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
        final List<Method> result4 = findAllClassesExtendingAbstractProvider()
                .stream()
                .flatMap(cl -> Arrays.asList(cl.getMethods()).stream())
                .filter(m -> !ignoreMethods.contains(m.getName()))
                .collect(Collectors.toList());
        return result4;
    }

}
