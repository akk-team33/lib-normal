package net.team33.libs.testing.v1;

import java.util.function.Function;
import java.util.stream.Stream;


class Distance {

    private final Class<?> superClass;
    private final Function<Class<?>, Stream<Class<?>>> superClasses;

    private Distance(final Class<?> superClass, final Function<Class<?>, Stream<Class<?>>> superClasses) {
        this.superClass = superClass;
        this.superClasses = superClasses;
    }

    static Distance to(final Class<?> superClass) {
        if (superClass.isInterface())
            return new Distance(superClass, Distance::superClassesOf);
        else
            return new Distance(superClass, Distance::superClassOf);
    }

    private static <E> Stream<E> streamOf(final E nullable) {
        return (null == nullable) ? Stream.empty() : Stream.of(nullable);
    }

    private static Stream<Class<?>> superClassOf(final Class<?> aClass) {
        return streamOf(aClass.getSuperclass());
    }

    private static Stream<Class<?>> superClassesOf(final Class<?> aClass) {
        return Stream.concat(Stream.of(aClass.getInterfaces()), superClassOf(aClass));
    }

    final int from(final Class<?> subClass) {
        if (superClass.equals(subClass))
            return 0;
        else
            return from(superClasses.apply(subClass));
    }

    private int from(final Stream<Class<?>> subClasses) {
        return subClasses.filter(superClass::isAssignableFrom)
                         .map(this::from)
                         .reduce(Integer.MAX_VALUE, Math::min);
    }
}
