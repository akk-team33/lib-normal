package net.team33.normalizing;

import java.math.BigDecimal;
import java.math.BigInteger;

class Integral extends Normal {
    private final BigInteger original;

    Integral(final BigInteger original) {
        this.original = original;
    }

    @Override public final boolean asBoolean() {
        return 0 != BigInteger.ZERO.compareTo(original);
    }

    @Override public final char asCharacter() {
        //noinspection NumericCastThatLosesPrecision
        return Character.valueOf((char) asBigInteger().intValue());
    }

    @Override public final BigInteger asBigInteger() {
        return original;
    }

    @Override public final BigDecimal asBigDecimal() {
        return new BigDecimal(original);
    }

    @Override public final String asString() {
        return original.toString();
    }
}
