package net.team33.normalizing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class NormalizerTest {

    private static final Character NULL_CHAR = '\0';

    private final Normalizer normalizer = Normalizer.instance();
    private Random random;

    @Before
    public void setUp() throws Exception {
        random = new Random();
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Test
    public final void fromCharacter() {
        Stream.concat(
                Stream.of(NULL_CHAR),
                Stream.generate(() -> Character.valueOf((char) random.nextInt())).limit(999)
        ).forEach(value0 -> {
            final char value = value0;
            final Normal normal = normalizer.normal(value);

            Assert.assertEquals(0 != value, normal.asBoolean());
            Assert.assertEquals(Byte.valueOf((byte) value).byteValue(), normal.asByte());
            Assert.assertEquals(Short.valueOf((short) value).shortValue(), normal.asShort());
            Assert.assertEquals(value, normal.asInteger());
            Assert.assertEquals(Long.valueOf(value).longValue(), normal.asLong());
            Assert.assertEquals(BigInteger.valueOf(value), normal.asBigInteger());
            Assert.assertEquals(Float.valueOf(value), normal.asFloat(), 0.0);
            Assert.assertEquals(Double.valueOf(value), normal.asDouble(), 0.0);
            Assert.assertEquals(BigDecimal.valueOf(value), normal.asBigDecimal());
            Assert.assertEquals(value, normal.asCharacter());
            Assert.assertEquals(String.valueOf(value), normal.asString());
        });
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Test
    public final void fromByte() {
        Stream.concat(
                Stream.of((byte) 0),
                Stream.generate(() -> (byte) random.nextInt()).limit(999)
        ).forEach(value0 -> {
            final byte value = value0;
            final Normal normal = normalizer.normal(value);

            Assert.assertEquals(0 != value, normal.asBoolean());
            Assert.assertEquals(Byte.valueOf(value).byteValue(), normal.asByte());
            Assert.assertEquals(Short.valueOf(value).shortValue(), normal.asShort());
            Assert.assertEquals(Integer.valueOf(value).intValue(), normal.asInteger());
            Assert.assertEquals(Long.valueOf(value).longValue(), normal.asLong());
            Assert.assertEquals(BigInteger.valueOf(value), normal.asBigInteger());
            Assert.assertEquals(Float.valueOf(value), normal.asFloat(), 0.0);
            Assert.assertEquals(Double.valueOf(value), normal.asDouble(), 0.0);
            Assert.assertEquals(BigDecimal.valueOf(value), normal.asBigDecimal());
            Assert.assertEquals(Character.valueOf((char) value).charValue(), normal.asCharacter());
            Assert.assertEquals(String.valueOf(value), normal.asString());
        });
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Test
    public final void fromInteger() {
        Stream.concat(
                Stream.of(0),
                Stream.generate(random::nextInt).limit(999)
        ).forEach(value0 -> {
            final int value = value0;
            final Normal normal = normalizer.normal(value);

            Assert.assertEquals(0 != value, normal.asBoolean());
            Assert.assertEquals(Byte.valueOf((byte) value).byteValue(), normal.asByte());
            Assert.assertEquals(Short.valueOf((short) value).shortValue(), normal.asShort());
            Assert.assertEquals(Integer.valueOf(value).intValue(), normal.asInteger());
            Assert.assertEquals(Long.valueOf(value).longValue(), normal.asLong());
            Assert.assertEquals(BigInteger.valueOf(value), normal.asBigInteger());
            Assert.assertEquals(Float.valueOf(value), normal.asFloat(), 0.0);
            Assert.assertEquals(Double.valueOf(value), normal.asDouble(), 0.0);
            Assert.assertEquals(BigDecimal.valueOf(value), normal.asBigDecimal());
            Assert.assertEquals(Character.valueOf((char) value).charValue(), normal.asCharacter());
            Assert.assertEquals(String.valueOf(value), normal.asString());
        });
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Test
    public final void fromBigInteger() {
        Stream.concat(
                Stream.of(BigInteger.ZERO),
                Stream.generate(() -> new BigInteger(random.nextInt(256), random)).limit(999)
        ).forEach(value -> {
            final Normal normal = normalizer.normal(value);

            Assert.assertEquals(!Objects.equals(BigInteger.ZERO, value), normal.asBoolean());
            Assert.assertEquals(value.byteValue(), normal.asByte());
            Assert.assertEquals(value.shortValue(), normal.asShort());
            Assert.assertEquals(value.intValue(), normal.asInteger());
            Assert.assertEquals(value.longValue(), normal.asLong());
            Assert.assertEquals(value, normal.asBigInteger());
            Assert.assertEquals(value.floatValue(), normal.asFloat(), 0.0);
            Assert.assertEquals(value.doubleValue(), normal.asDouble(), 0.0);
            Assert.assertEquals(new BigDecimal(value.toString()), normal.asBigDecimal());
            Assert.assertEquals(Character.valueOf((char) value.intValue()).charValue(), normal.asCharacter());
            Assert.assertEquals(String.valueOf(value), normal.asString());
        });
    }

    private static class Sequence<T> {
    }
}