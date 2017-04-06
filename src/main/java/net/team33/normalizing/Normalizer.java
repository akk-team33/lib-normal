package net.team33.normalizing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Normalizer {

    private static final Set<String> FALSE_STRINGS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "",
            "0",
            "0.0",
            "false",
            null
    )));

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, Function<Object, Normal>> functions =
            new HashMap<Class<?>, Function<Object, Normal>>() {{
                put(Character.class, Char::new);
                put(Number.class, Numeric::new);
            }};

    private Normalizer() {
    }

    public static Normalizer instance() {
        return new Normalizer();
    }

    public final Normal normal(final Object original) {
        if (null == original) {
            return Normal.NULL;
        } else {
            return getFunction(original.getClass()).apply(original);
        }
    }

    private Function<Object, Normal> getFunction(final Class<?> originalClass) {
        return functions.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(originalClass))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("not yet implemented"))
                .getValue();
    }

    private static class Char extends Normal {
        private static final char NULL_CHAR = '\0';

        private final char backing;

        private Char(final Object original) {
            this((Character) original);
        }

        ;

        private Char(final Character original) {
            backing = original;
        }

        @Override public final boolean asBoolean() {
            return NULL_CHAR != backing;
        }

        @Override public final BigInteger asBigInteger() {
            return BigInteger.valueOf(backing);
        }

        @Override public final BigDecimal asBigDecimal() {
            return BigDecimal.valueOf(backing);
        }

        @Override public final char asCharacter() {
            return backing;
        }

        @Override public final String asString() {
            return String.valueOf(backing);
        }
    }

    private static class Element extends Normal {
        private static final char DECIMAL_DOT = '.';

        private final String backing;

        private Element(final String backing) {
            this.backing = backing;
        }

        private static String decimalPrefix(final String value) {
            final int dotIndex = value.indexOf(DECIMAL_DOT);
            return (0 > dotIndex) ? value : value.substring(0, dotIndex);
        }

        @Override public final boolean asBoolean() {
            return !FALSE_STRINGS.contains(asString());
        }

        @Override public final BigInteger asBigInteger() {
            return new BigInteger(decimalPrefix(backing));
        }

        @Override public final BigDecimal asBigDecimal() {
            return new BigDecimal(decimalPrefix(backing));
        }

        @Override public final String asString() {
            return backing;
        }
    }

    private static class Numeric extends Element {
        private Numeric(final Object original) {
            super(original.toString());
        }

        @Override public final char asCharacter() {
            //noinspection NumericCastThatLosesPrecision
            return Character.valueOf((char) asBigInteger().intValue());
        }
    }
}
