package net.team33.normalizing;

import net.team33.normalizing.normal.Integral;
import net.team33.normalizing.normal.Rational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
            new LinkedHashMap<Class<?>, Function<Object, Normal>>() {{
                put(Byte.class, original -> new Integral(BigInteger.valueOf((Byte) original)));
                put(Short.class, original -> new Integral(BigInteger.valueOf((Short) original)));
                put(Integer.class, original -> new Integral(BigInteger.valueOf((Integer) original)));
                put(Long.class, original -> new Integral(BigInteger.valueOf((Long) original)));
                put(BigInteger.class, (original) -> new Integral((BigInteger) original));
                put(Float.class, original -> new Rational(BigDecimal.valueOf((Float) original)));
                put(Double.class, original -> new Rational(BigDecimal.valueOf((Double) original)));
                put(BigDecimal.class, original -> new Rational((BigDecimal) original));
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

    private static class FloatX extends Rational {
        private FloatX(final Object original) {
            super(BigDecimal.valueOf((Float) original));
        }
    }

    private static class DoubleX extends Rational {
        private DoubleX(final Object original) {
            super(BigDecimal.valueOf((Double) original));
        }
    }
}
