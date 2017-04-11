package net.team33.normalizing;

import java.math.BigDecimal;
import java.math.BigInteger;

class Rational extends Normal {
    private final BigDecimal original;

    Rational(final BigDecimal original) {
        this.original = original;
    }

    @Override public final boolean asBoolean() {
        return 0 != BigDecimal.ZERO.compareTo(original);
    }

    @Override public final char asCharacter() {
        //noinspection NumericCastThatLosesPrecision
        return Character.valueOf((char) asBigInteger().intValue());
    }

    @Override public final BigInteger asBigInteger() {
        return original.toBigInteger();
    }

    @Override public final BigDecimal asBigDecimal() {
        return original;
    }

    @Override public final String asString() {
        return original.toString();
    }
}
