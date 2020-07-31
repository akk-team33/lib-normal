package net.team33.test.testing;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dispenser {

    private final Random random = new Random();

    private Dispenser(final Stage stage) {
    }

    public static Builder builder() {
        return new Builder();
    }

    public final <R> R any(final Class<R> rClass) {
        return (R) any((Type) rClass);
    }

    private Object any(final Type type) {
        Function<Dispenser, ?> method = TypeMapper.map(type);
        return method.apply(this);
    }

    private <R> R anyComposite(final Class<R> type) {
        final R result = newOf(type);
        fieldsOf(type).filter(field -> isSignificant(field))
                      .peek(field -> field.setAccessible(true))
                      .forEach(field -> setField(result, field));
        return result;
    }

    private static boolean isSignificant(final Field field) {
        final int modifiers = field.getModifiers();
        return !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
    }

    private void setField(final Object result, final Field field) {
        try {
            field.set(result, any(field.getGenericType()));
        } catch (final Exception e) {
            throw new IllegalStateException("Could not set field <" + field + ">", e);
        }
    }

    private static <R> R newOf(final Class<R> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (final Exception e) {
            throw new IllegalArgumentException("Could not create new instance of " + type, e);
        }
    }

    private static Stream<Field> fieldsOf(final Class<?> type) {
        return (null == type)
                ? Stream.empty()
                : Stream.concat(fieldsOf(type.getSuperclass()), Stream.of(type.getDeclaredFields()));
    }

    private byte[] anyBytes() {
        final byte[] result = new byte[random.nextInt(16) + 1];
        random.nextBytes(result);
        return result;
    }

    private List<?> anyList(final Type elementType) {
        return Stream.generate(() -> any(elementType))
                     .limit(random.nextInt(16) + 1)
                     .collect(Collectors.toCollection(ArrayList::new));
    }

    private Set<?> anySet(final Type elementType) {
        return Stream.generate(() -> any(elementType))
                     .limit(random.nextInt(16) + 1)
                     .collect(Collectors.toCollection(HashSet::new));
    }

    private enum ClassMapper {

        INTEGER(Settings.INTEGER::contains, type -> dsp -> dsp.random.nextInt()),
        STRING(type -> type.isAssignableFrom(String.class), type -> dsp -> dsp.any(BigInteger.class)
                                                                              .toString(Character.MAX_RADIX)),
        BIGINTEGER(type -> type.isAssignableFrom(BigInteger.class), type -> dsp -> new BigInteger(dsp.random.nextInt(128) + 1, dsp.random)),
        BYTE_ARRAY(type -> byte[].class.equals(type), type -> dsp -> dsp.anyBytes()),
        COMPOSITE(Settings.COMPOSITE::contains, type -> dsp -> dsp.anyComposite(type));

        private final Predicate<Class<?>> filter;
        @SuppressWarnings("rawtypes")
        private final Function mapping;

        <R> ClassMapper(final Predicate<Class<?>> filter, final Function<Class<R>, Function<Dispenser, R>> mapping) {
            this.filter = filter;
            this.mapping = mapping;
        }

        private static <R> Function<Dispenser, R> map(final Class<R> type) {
            return Stream.of(values())
                         .filter(value -> value.filter.test(type))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException(
                                 "No mapping specified for <" + type + ">"))
                         .toMethod(type);
        }

        private <R> Function<Dispenser, R> toMethod(final Class<R> type) {
            //noinspection unchecked
            return (Function<Dispenser, R>) mapping.apply(type);
        }

        @SuppressWarnings("InnerClassFieldHidesOuterClassField")
        private static class Settings {

            private static final Set<Class<?>> INTEGER = Collections.unmodifiableSet(
                    new HashSet<>(Arrays.asList(Integer.class, int.class)));
            private static final Set<Class<?>> COMPOSITE = Collections.unmodifiableSet(
                    new HashSet<>(Arrays.asList(Subject.class)));
        }
    }

    private enum ParameterizedMapper {

        LIST(type -> List.class.equals(type.getRawType()), type -> dsp -> dsp.anyList(type.getActualTypeArguments()[0])),
        SET(type -> Set.class.equals(type.getRawType()), type -> dsp -> dsp.anySet(type.getActualTypeArguments()[0]));

        private final Predicate<ParameterizedType> filter;
        private final Function<ParameterizedType, Function<Dispenser, ?>> mapping;

        ParameterizedMapper(final Predicate<ParameterizedType> filter, final Function<ParameterizedType, Function<Dispenser, ?>> mapping) {
            this.filter = filter;
            this.mapping = mapping;
        }

        public static Function<Dispenser, ?> map(final ParameterizedType type) {
            return Stream.of(values())
                         .filter(value -> value.filter.test(type))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException(
                                 "No mapping specified for type <" + type + ">"))
                         .toMethod(type);
        }

        private Function<Dispenser, ?> toMethod(final ParameterizedType type) {
            return mapping.apply(type);
        }
    }

    private enum TypeMapper {

        CLASS(Filter.CLASS, Mapping.CLASS),
        PARAMETERIZED(Filter.PARAMETERIZED, Mapping.PARAMETERIZED);

        private final Predicate<Type> filter;
        private final Function<Type, Function<Dispenser, ?>> mapping;

        TypeMapper(final Predicate<Type> filter, final Function<Type, Function<Dispenser, ?>> mapping) {
            this.filter = filter;
            this.mapping = mapping;
        }

        private static Function<Dispenser, ?> map(final Type type) {
            return Stream.of(values())
                         .filter(value -> value.filter.test(type))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException(
                                 "No mapping specified for <" + type.getClass() + ">"))
                         .toMethod(type);
        }

        private Function<Dispenser, ?> toMethod(final Type type) {
            return mapping.apply(type);
        }

        @SuppressWarnings("InnerClassFieldHidesOuterClassField")
        @FunctionalInterface
        private interface Filter extends Predicate<Type> {

            Filter CLASS = type -> type instanceof Class;
            Filter PARAMETERIZED = type -> type instanceof ParameterizedType;
        }

        @SuppressWarnings("InnerClassFieldHidesOuterClassField")
        @FunctionalInterface
        private interface Mapping extends Function<Type, Function<Dispenser, ?>> {

            Mapping CLASS = type -> ClassMapper.map((Class<?>) type);
            Mapping PARAMETERIZED = type -> ParameterizedMapper.map((ParameterizedType) type);
        }
    }

    private static class Stage implements Supplier<Dispenser> {

        private Stage(final Builder builder) {
        }

        @Override
        public final Dispenser get() {
            return new Dispenser(this);
        }
    }

    public static class Builder {



        private Builder() {
        }

        public final Supplier<Dispenser> prepare() {
            return new Stage(this);
        }
    }
}
