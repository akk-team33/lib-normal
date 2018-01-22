package net.team33.patterns.normal;

import net.team33.patterns.Normal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableMap;

public class Simple implements Normal {

    private static final Map<Character, String> SPECIAL_CHAR = unmodifiableMap(new HashMap<Character, String>() {{
        put(' ', " ");
        put('\t', "\\t");
        put('\n', "\\n");
        put('\r', "\\r");
        put('\\', "\\\\");
        put('\"', "\\\"");
        put('\'', "\\\'");
    }});

    private final String value;
    private transient String quoted;

    public Simple(final Object value) {
        this.value = value.toString();
    }

    private static String toQuoted(final char c) {
        return Optional.ofNullable(SPECIAL_CHAR.get(c))
                .orElseGet(() -> toCoded(c));
    }

    private static String toCoded(final int code) {
        final String fmt = (256 > code) ? "\\%03o" : "\\u%04x";
        return String.format(fmt, code);
    }

    @Override
    public final int hashCode() {
        return value.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Simple) && isEqual((Simple) obj));
    }

    private boolean isEqual(final Simple other) {
        return value.equals(other.value);
    }

    @Override
    public final String toString() {
        if (null == quoted) {
            quoted = toQuoted(value);
        }
        return quoted;
    }

    private static String toQuoted(final String raw) {
        final StringBuilder result = new StringBuilder(raw.length() + 2);
        int count = 0;
        for (final char c : raw.toCharArray()) {
            if ((' ' <= c) && (c < 256) && !SPECIAL_CHAR.containsKey(c) && !Character.isWhitespace(c)) {
                result.append(c);
            } else {
                result.append(toQuoted(c));
                count += 1;
            }
        }
        if (raw.isEmpty() || (0 < count)) {
            return result.insert(0, '"').append('"').toString();
        } else {
            return raw;
        }
    }
}
