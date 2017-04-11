package net.team33.normalizing.normal;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Integral extends Numeric<BigInteger> {

    public Integral(final BigInteger original) {
        super(original);
    }

    @Override final BigInteger zero() {
        return BigInteger.ZERO;
    }

    @Override public final BigInteger asBigInteger() {
        return original;
    }

    @Override public final BigDecimal asBigDecimal() {
        return new BigDecimal(original);
    }
}
