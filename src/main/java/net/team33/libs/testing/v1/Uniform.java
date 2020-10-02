package net.team33.libs.testing.v1;

import java.util.function.Function;

/**
 * Encapsulates any object and associates it with an alternative {@linkplain Object#toString() string representation},
 * e.g. in order to achieve a conveniently readable representation in test cases, especially error cases.
 *
 * @see #builder(Function)
 */
public final class Uniform {

    private final Object subject;
    private final Function<Object, String> stringRepresentationMethod;

    private Uniform(final Object subject, final Function<Object, String> stringRepresentationMethod) {
        this.subject = subject;
        this.stringRepresentationMethod = stringRepresentationMethod;
    }

    /**
     * Retrieves a {@link Function} to build new {@link Uniform} instances, each associated with a given
     * string representation method that will be used in {@link #toString()}.
     */
    public static Function<Object, Uniform> builder(final Function<Object, String> stringRepresentationMethod) {
        return value -> new Uniform(value, stringRepresentationMethod);
    }

    @Override
    public final int hashCode() {
        return subject.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Uniform) && subject.equals(((Uniform) obj).subject));
    }

    @Override
    public final String toString() {
        return stringRepresentationMethod.apply(subject);
    }
}
