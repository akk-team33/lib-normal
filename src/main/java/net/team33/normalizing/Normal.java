package net.team33.normalizing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings("MethodMayBeStatic")
public abstract class Normal {

    public static final Normal NULL = new Normal() {
    };

    @SuppressWarnings("DesignForExtension")
    public Stream<Normal> asStream() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("DesignForExtension")
    public Set<Normal> asSet() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("DesignForExtension")
    public List<Normal> asList() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("DesignForExtension")
    public Normal[] asArray() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("DesignForExtension")
    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    public final byte asByte() {
        return asBigInteger().byteValue();
    }

    public final short asShort() {
        return asBigInteger().shortValue();
    }

    public final int asInteger() {
        return asBigInteger().intValue();
    }

    public final long asLong() {
        return asBigInteger().longValue();
    }

    @SuppressWarnings("DesignForExtension")
    public BigInteger asBigInteger() {
        throw new UnsupportedOperationException();
    }

    public final float asFloat() {
        return asBigDecimal().floatValue();
    }

    public final double asDouble() {
        return asBigDecimal().doubleValue();
    }

    @SuppressWarnings("DesignForExtension")
    public BigDecimal asBigDecimal() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("DesignForExtension")
    public char asCharacter() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("DesignForExtension")
    public String asString() {
        throw new UnsupportedOperationException();
    }
}
