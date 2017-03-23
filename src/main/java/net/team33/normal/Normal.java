package net.team33.normal;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

public interface Normal {

    Normal NULL = new Normal() {
    };

    static Producer producer() {
        return new Producer();
    }

    default java.util.Set<Normal> toSet() {
        throw new UnsupportedOperationException();
    }

    class Producer {

        private final List<Entry> entries = Arrays.asList(
                new Entry(java.util.Set.class, Set::new)
        );

        private Producer() {
        }

        public final Normal normal(final Object original) {
            if (null == original) {
                return NULL;
            } else {
                return entries.stream()
                        .filter(entry -> entry.oriClass.isInstance(original))
                        .findFirst()
                        .map(entry -> entry.mapping.apply(original))
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("unknown: <%s> of <%s>", original, original.getClass())));
            }
        }

        private static class Entry {
            private final Class<?> oriClass;
            private final Function<Object, Normal> mapping;

            private Entry(final Class<?> oriClass, final Function<Object, Normal> mapping) {
                this.oriClass = oriClass;
                this.mapping = mapping;
            }
        }

        private static class Set extends AbstractSet<Normal> implements Normal {

            private Set(Object original) {
                this((java.util.Set<?>) original);
            }

            private Set(java.util.Set<?> original) {
                backing = new TreeSet<>(original);
            }

            @Override
            public Iterator<Normal> iterator() {
                throw new UnsupportedOperationException("not yet implemented");
            }

            @Override
            public int size() {
                throw new UnsupportedOperationException("not yet implemented");
            }
        }
    }
}
