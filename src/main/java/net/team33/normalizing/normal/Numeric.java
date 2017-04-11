package net.team33.normalizing.normal;

import net.team33.normalizing.Normal;

abstract class Numeric<T extends Comparable<T>> extends Normal {
    final T original;

    Numeric(final T original) {
        this.original = original;
    }

    abstract T zero();

    @Override public final boolean asBoolean() {
        return 0 != zero().compareTo(original);
    }

    @Override public final char asCharacter() {
        //noinspection NumericCastThatLosesPrecision
        return Character.valueOf((char) asInteger());
    }

    @Override public final String asString() {
        return original.toString();
    }
}
