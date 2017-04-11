package net.team33.normalizing.normal;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Rational extends Numeric<BigDecimal> {

    public Rational(final BigDecimal original) {
        super(original);
    }

    @Override final BigDecimal zero() {
        return BigDecimal.ZERO;
    }

    @Override public final BigInteger asBigInteger() {
        return original.toBigInteger();
    }

    @Override public final BigDecimal asBigDecimal() {
        return original;
    }
}
