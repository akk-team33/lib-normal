package net.team33.libs.testing.v1;

import java.util.stream.Stream;

final class Classes {

    private Classes() {
    }

    private static boolean isDefault(final Class<?> subjectClass,
                                     final String methodName,
                                     final Class<?>... parameters) throws NoSuchMethodException {
        return subjectClass.getMethod(methodName, parameters)
                           .getDeclaringClass()
                           .equals(Object.class);
    }

    static boolean isValueClass(final Class<?> subjectClass) {
        try {
            final boolean defaultEquals = isDefault(subjectClass, "equals", Object.class);
            final boolean defaultHashCode = isDefault(subjectClass, "hashCode");
            final boolean defaultToString = isDefault(subjectClass, "toString");
            return !(defaultEquals || defaultHashCode || defaultToString);
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }

    static Stream<Class<?>> deepStreamOf(final Class<?> subject) {
        return (null == subject)
                ? Stream.empty()
                : Stream.concat(deepStreamOf(subject.getSuperclass()), Stream.of(subject));
    }
}
