package net.team33.libs.testing.v1;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


@SuppressWarnings("WeakerAccess")
public final class Normalizer {

    public static final Normalizer DEFAULT = builder().build();
    private static final String NO_ACCESS = "cannot access field <%s> for subject <%s>";
    private static final Map<Class<?>, Map<String, Field>> FIELDS_CACHE = new ConcurrentHashMap<>(0);
    @SuppressWarnings("rawtypes")
    private final Map<Class, BiFunction> methods;

    private Normalizer(final Builder builder) {
        methods = new ConcurrentHashMap<>(builder.methods);
    }

    public static Builder builder() {
        return new Builder();
    }

    private static Map<String, Field> newFieldMap(final Class<?> subjectClass, final Class<?> key) {
        return Fields.deepStreamOf(key)
                     .filter(Fields.IS_SIGNIFICANT)
                     .peek(field -> field.setAccessible(true))
                     .collect(toMap(field -> Fields.compactName(subjectClass, field), field -> field));
    }

    public final Object normal(final Object subject) {
        final Class<?> subjectClass = (null == subject) ? Void.class : subject.getClass();
        return getMethod(subjectClass).apply(this, subject);
    }

    private BiFunction<Normalizer, Object, Object> getMethod(final Class<?> subjectClass) {
        //noinspection unchecked
        return Optional.ofNullable(methods.get(subjectClass))
                       .orElseGet(() -> {
                           //noinspection rawtypes
                           final BiFunction result = Category.map(subjectClass);
                           methods.put(subjectClass, result);
                           return result;
                       });
    }

    public final Map<?, ?> normalFieldMap(final Class<?> subjectClass, final Object subject) {
        return FIELDS_CACHE.computeIfAbsent(subjectClass, key -> newFieldMap(subjectClass, key))
                           .entrySet().stream()
                           .collect(TreeMap::new,
                                    (map, entry) -> put(map, entry, subject),
                                    Map::putAll);
    }

    private void put(final Map<Object, Object> map,
                     final Map.Entry<String, Field> entry,
                     final Object subject) {
        try {
            map.put(normal(entry.getKey()), normal(entry.getValue().get(subject)));
        } catch (final IllegalAccessException caught) {
            throw new IllegalStateException(String.format(NO_ACCESS, entry.getValue(), subject), caught);
        }
    }

    public final List<Object> normalArray(final Object array) {
        final int length = Array.getLength(array);
        final List<Object> result = new ArrayList<>(length);
        for (int i = 0; i < length; ++i) {
            result.add(normal(Array.get(array, i)));
        }
        return result;
    }

    public final List<Object> normalList(final Collection<?> subject) {
        return subject.stream()
                      .map(this::normal)
                      .collect(Collectors.toList());
    }

    public final Set<?> normalSet(final Set<?> subject) {
        return subject.stream()
                      .map(this::normal)
                      .collect(Collectors.toSet());
    }

    public final Map<?, ?> normalMap(final Map<?, ?> subject) {
        return subject.entrySet().stream().collect(
                HashMap::new, (map, entry) -> map.put(normal(entry.getKey()), normal(entry.getValue())), Map::putAll);
    }

    private enum Category {

        NULL______(Void.class::equals,
                   subjectClass -> (normalizer, subject) -> subject),
        ARRAY_____(Class::isArray,
                   subjectClass -> Normalizer::normalArray),
        SET_______(Set.class::isAssignableFrom,
                   subjectClass -> (normalizer, subject) -> normalizer.normalSet((Set<?>) subject)),
        LIST______(Collection.class::isAssignableFrom,
                   subjectClass -> (normalizer, subject) -> normalizer.normalList((Collection<?>) subject)),
        MAP_______(Map.class::isAssignableFrom,
                   subjectClass -> (normalizer, subject) -> normalizer.normalMap((Map<?, ?>) subject)),
        VALUE_____(Classes::isValueClass,
                   subjectClass -> (normalizer, subject) -> subject),
        COMPOSITE_(subjectClass -> false,
                   subjectClass -> (normalizer, subject) -> normalizer.normalFieldMap(subjectClass, subject));

        private final Predicate<Class<?>> filter;
        private final Function<? super Class<?>, ? extends BiFunction<Normalizer, Object, Object>> mapping;

        Category(final Predicate<Class<?>> filter,
                 final Function<? super Class<?>, ? extends BiFunction<Normalizer, Object, Object>> mapping) {
            this.filter = filter;
            this.mapping = mapping;
        }

        private static BiFunction<Normalizer, Object, Object> map(final Class<?> subjectClass) {
            return Stream.of(values())
                         .filter(value -> value.filter.test(subjectClass))
                         .findFirst()
                         .orElse(COMPOSITE_)
                         .apply(subjectClass);
        }

        private BiFunction<Normalizer, Object, Object> apply(final Class<?> subjectClass) {
            return mapping.apply(subjectClass);
        }
    }

    public static final class Builder {

        @SuppressWarnings("rawtypes")
        private final Map<Class, BiFunction> methods = new HashMap<>(0);

        private Builder() {
        }

        public final Normalizer build() {
            return new Normalizer(this);
        }

        public final <T> Builder addMethod(final Class<T> subjectClass,
                                           final BiFunction<Normalizer, T, Object> method) {
            methods.put(subjectClass, method);
            return this;
        }
    }
}
