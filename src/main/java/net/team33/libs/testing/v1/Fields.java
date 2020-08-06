package net.team33.libs.testing.v1;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Stream;

final class Fields {

    static final Predicate<Field> IS_SIGNIFICANT = field -> {
        final int modifiers = field.getModifiers();
        final boolean isSynthetic = 0 != (0x00001000 & modifiers);
        return !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || isSynthetic);
    };

    private Fields() {
    }

    static Stream<Field> deepStreamOf(final Class<?> type) {
        return streamOf(Classes.deepStreamOf(type));
    }

    private static Stream<Field> streamOf(final Stream<Class<?>> classes) {
        return classes.map(Class::getDeclaredFields)
                      .map(Stream::of)
                      .reduce(Stream::concat)
                      .orElseGet(Stream::empty);
    }

    static String compactName(final Class<?> contextClass, final Field field) {
        final Class<?> declaringClass = field.getDeclaringClass();
        final String fieldName = field.getName();
        try {
            return prefix(contextClass, declaringClass) + fieldName;
        } catch (final NullPointerException e) {
            return declaringClass.getCanonicalName() + "." + fieldName;
        }
    }

    private static String prefix(final Class<?> contextClass, final Class<?> declaringClass) {
        return (contextClass == declaringClass) ? "" : ("." + prefix(contextClass.getSuperclass(), declaringClass));
    }
}
