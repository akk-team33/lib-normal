package net.team33.normalizing;

import java.math.BigInteger;
import java.util.Set;

public interface Normal {

    Normal NULL = new Normal() {
    };

    default Set<Normal> asSet() {
        throw new UnsupportedOperationException();
    }

    default String asString() {
        throw new UnsupportedOperationException();
    }

    default Integer asInteger() {
        throw new UnsupportedOperationException();
    }

    default BigInteger asBigInteger() {
        throw new UnsupportedOperationException();
    }
}
