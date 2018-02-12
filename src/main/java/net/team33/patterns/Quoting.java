package net.team33.patterns;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Quoting {

    private static final int ASCII_LIMIT = 128;
    private static final int MAX_MAPPING_SIZE = 1024;

    private final Map<Character, String> mapping;
    private final char startQuote = '"';
    private final char endQuote = '"';

    public Quoting() {
        mapping = new ConcurrentHashMap<>(0);
        mapping.put('\b', "\\b");
        mapping.put('\f', "\\f");
        mapping.put('\n', "\\n");
        mapping.put('\r', "\\r");
        mapping.put('\t', "\\t");
        mapping.put('\\', "\\\\");
        mapping.put(startQuote, "\\" + startQuote);
        mapping.put(endQuote, "\\" + endQuote);
    }

    public String quote(final String input) {
        final int length = input.length();
        final StringBuilder result = new StringBuilder(length)
                .append(startQuote);
        int actual = 0;
        while (actual < length) {
            int limit = actual;
            while ((limit < length) && isPrimary(input.charAt(limit))) {
                limit += 1;
            }
            result.append(input, actual, limit);
            if (limit < length) {
                result.append(escaped(input.charAt(limit)));
                limit += 1;
            }
            actual = limit;
        }
        return result
                .append(endQuote)
                .toString();
    }

    private String escaped(final char c) {
        return Optional.ofNullable(mapping.get(c)).orElseGet(() -> {
            final String fmt = (ASCII_LIMIT > c) ? "\\%03o" : "\\u%04X";
            final String result = String.format(fmt, Integer.valueOf(c));
            if (MAX_MAPPING_SIZE > mapping.size()) {
                mapping.put(c, result);
            }
            return result;
        });
    }

    private boolean isPrimary(final char c) {
        return (' ' <= c) && (c < ASCII_LIMIT) && !mapping.containsKey(c);
    }
}
