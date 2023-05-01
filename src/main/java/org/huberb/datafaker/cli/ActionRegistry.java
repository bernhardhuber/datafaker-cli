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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author berni3
 */
class ActionRegistry<K, T> {
    public static <T> void consumingOneOf(Integer i, T t, Consumer<T>... actions) {
        final ActionRegistry<Integer, T> actionRegistry = new ActionRegistry<>();
        for (int j = 0; j < actions.length; j++) {
            actionRegistry.register(j, actions[j]);
        }
        actionRegistry.consume(i, t);
    }
    public static <T> void consumingOneOf(Enum e, T t, Consumer<T>... actions) {
        final ActionRegistry<Integer, T> actionRegistry = new ActionRegistry<>();
        for (int j = 0; j < actions.length; j++) {
            actionRegistry.register(j, actions[j]);
        }
        actionRegistry.consume(e.ordinal(), t);
    }

    private final Consumer<T> noop = any -> {
    };
    private final Map<K, Consumer<T>> registry = new HashMap<>();

    public ActionRegistry<K, T> register(K key, Consumer<T> action) {
        registry.put(key, action);
        return this;
    }

    public void consume(K key, T t) {
        registry.getOrDefault(key, noop).accept(t);
    }


}
