package net.team33.normalizing;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class Normalizer {

    private final List<Entry> entries;

    private Normalizer() {
        entries = Arrays.asList(
                new Entry(Number.class, Numeric::new),
                new Entry(CharSequence.class, Text::new),
                new Entry(Set.class, MySet::new)
        );
    }

    public static Normalizer instance() {
        return new Normalizer();
    }

    public final Normal normal(final Object original) {
        if (null == original) {
            return Normal.NULL;
        } else {
            return entries.stream()
                    .filter(entry -> entry.oriClass.isInstance(original))
                    .findFirst()
                    .map(entry -> entry.mapping.apply(this, original))
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("unknown: <%s> of <%s>", original, original.getClass())));
        }
    }

    private static class Basic<T> implements Normal {
        @SuppressWarnings("PackageVisibleField")
        final T backing;

        private Basic(final T backing) {
            this.backing = backing;
        }
    }

    private static class Numeric extends Basic<String> {
        public static final char DECIMAL_DOT = '.';

        private Numeric(final Normalizer normalizer, final Object original) {
            super(original.toString());
        }

        private static String prefix(final String value) {
            final int index = value.indexOf(DECIMAL_DOT);
            return (0 > index) ? value : value.substring(0, index);
        }

        @Override public final Integer asInteger() {
            return Integer.valueOf(prefix(backing));
        }

        @Override public final BigInteger asBigInteger() {
            return new BigInteger(prefix(backing));
        }
    }

    private static class Text extends Basic<String> {
        private Text(final Normalizer normalizer, final Object original) {
            super(original.toString());
        }

        @Override public final String asString() {
            return backing;
        }
    }

    private static class MySet extends Basic<HashSet<Normal>> {
        private MySet(final Normalizer normalizer, final Object original) {
            this(normalizer, (Set<?>) original);

        }

        private MySet(final Normalizer normalizer, final Set<?> original) {
            super(new HashSet<>(0));
            original.forEach(e -> backing.add(normalizer.normal(e)));
        }

        @Override public final Set<Normal> asSet() {
            return Collections.unmodifiableSet(backing);
        }
    }

    private static class Entry {
        private final Class<?> oriClass;
        private final BiFunction<Normalizer, Object, Normal> mapping;

        private Entry(final Class<?> oriClass, final BiFunction<Normalizer, Object, Normal> mapping) {
            this.oriClass = oriClass;
            this.mapping = mapping;
        }
    }
}
