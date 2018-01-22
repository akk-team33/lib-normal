package net.team33.patterns;

import net.team33.patterns.normal.Simple;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.util.Collections.unmodifiableMap;

@SuppressWarnings("MethodMayBeStatic")
public class Normalizer {

    private final Methods methods;

    private Normalizer(final Builder builder) {
        methods = new Methods(builder.methods);
    }

    public static Builder builder() {
        return new Builder()
                .put(Boolean.class, (normalizer, t) -> new Simple(t))
                .put(Number.class, (normalizer, t) -> new Simple(t))
                .put(Character.class, (normalizer, t) -> new Simple(t))
                .put(CharSequence.class, (normalizer, t) -> new Simple(t));
    }

    public final <T> Normal normal(final T value) {
        final BiFunction<Normalizer, T, Normal> function = methods.get(value.getClass());
        return function.apply(this, value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class Methods {

        private final Map<Class, BiFunction> pool;
        private final Map<Class, BiFunction> cache = new HashMap<>(0);

        private Methods(final Map<Class, BiFunction> pool) {
            this.pool = unmodifiableMap(new HashMap<>(pool));
        }

        private <T> BiFunction<Normalizer, T, Normal> get(final Class tClass) {
            return Optional.ofNullable(cache.get(tClass)).orElseGet(() -> {
                final BiFunction result = find(tClass);
                cache.put(tClass, result);
                return result;
            });
        }

        private BiFunction find(final Class tClass) {
            return pool.entrySet().stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(tClass))
                    .map(Map.Entry::getValue)
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "cannot find method for <" + tClass.getCanonicalName() + ">"
                    ));
        }
    }

    public static class Builder {

        @SuppressWarnings("rawtypes")
        private final Map<Class, BiFunction> methods = new HashMap<>(0);

        private Builder() {
        }

        public final <T> Builder put(final Class<T> tClass, final BiFunction<Normalizer, T, Normal> method) {
            methods.put(tClass, method);
            return this;
        }

        public final Normalizer build() {
            return new Normalizer(this);
        }
    }
}
