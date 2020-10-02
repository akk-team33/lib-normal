package net.team33.libs.testing.v1;

import java.util.function.Function;

/**
 * <p>Encapsulates an object and associates it with an alternative {@linkplain Object#toString() string representation},
 * e.g. in order to achieve a conveniently readable representation in test cases, especially error cases.</p>
 *
 * <p>The comparability of two encapsulated objects is preserved insofar as two capsules are equal if their encapsulated
 * objects are equal and vice versa.</p>
 *
 * @see #builder(Function)
 * @see #equals(Object)
 * @see #hashCode()
 * @see #toString()
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

    /**
     * This implementation simply returns the {@link Object#hashCode()} of the encapsulated object.
     */
    @Override
    public final int hashCode() {
        return subject.hashCode();
    }

    /**
     * <p>This implementation defines two {@link Uniform} instances as equal if and only if their encapsulated objects
     * are equal.</p>
     *
     * <p>Note: Due to the symmetry requirement of {@link Object#equals(Object)}, the result of a direct comparison
     * of a {@link Uniform} with its encapsulated object must be {@code false}.</p>
     */
    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Uniform) && subject.equals(((Uniform) obj).subject));
    }

    /**
     * This implementation applies the associated method for the string representation to the encapsulated object
     * and returns the result.
     *
     * @see #builder(Function)
     */
    @Override
    public final String toString() {
        return stringRepresentationMethod.apply(subject);
    }
}
