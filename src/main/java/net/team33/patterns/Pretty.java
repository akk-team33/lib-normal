package net.team33.patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a wrapper for instances of (in principle) any class that provides a toString() method
 * that supports a multi-line, structured representation.
 * <p>
 * In addition, two Pretty instances are equal if their embedded instances are equal.
 */
public class Pretty<T> {

    private final Map<String, String> delimiters = new HashMap<String, String>(2) {{
        put("{", "}");
        put("[", "]");
    }};
    private final T subject;

    public Pretty(final T subject) {
        this.subject = subject;
    }

    public final T getSubject() {
        return subject;
    }

    @Override
    public final boolean equals(final Object other) {
        return (this == other) || ((other instanceof Pretty) && subject.equals(((Pretty<?>) other).subject));
    }

    @Override
    public final int hashCode() {
        return (null == subject) ? 0 : subject.hashCode();
    }

    @Override
    public final String toString() {
        return pretty(subject.toString());
    }

    private String pretty(final String fragment) {
        int start = 0;
        final Optional<String> any = delimiters.keySet().stream()
                .filter(open -> fragment.substring(start).startsWith(open)).findAny();
        throw new UnsupportedOperationException("not yet implemented");
    }
}
