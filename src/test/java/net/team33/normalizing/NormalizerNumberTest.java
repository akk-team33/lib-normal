package net.team33.normalizing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class NormalizerNumberTest<T extends Number> {

    private static final Function<Random, Byte> NEW_BYTE = rd -> Byte.valueOf((byte) rd.nextInt());
    private static final Function<Random, Short> NEW_SHORT = rd -> Short.valueOf((short) rd.nextInt());
    private static final Function<Random, Integer> NEW_INTEGER = rd -> Integer.valueOf(rd.nextInt());
    private static final Function<Random, Long> NEW_LONG = rd -> Long.valueOf(rd.nextLong());
    private static final Function<Random, Float> NEW_FLOAT = rd -> (float) rd.nextInt() / rd.nextInt();
    private static final Function<Random, Double> NEW_DOUBLE = rd -> (double) rd.nextInt() / rd.nextInt();
    private static final Function<Random, BigInteger> NEW_BIG_INTEGER = rd -> BigInteger.valueOf(NEW_LONG.apply(rd))
            .multiply(BigInteger.valueOf(NEW_LONG.apply(rd)));
    private static final Function<Random, BigDecimal> NEW_BIG_DECIMAL = rd -> new BigDecimal(
            NEW_BIG_INTEGER.apply(rd),
            NEW_BYTE.apply(rd)
    );
    private static final Function<Float, BigInteger> FLT_TO_BIG_INT = flt -> BigDecimal.valueOf(flt).toBigInteger();
    private static final Function<Float, BigDecimal> FLT_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Float, String> FLT_TO_STRING = flt -> BigDecimal.valueOf(flt).toString();
    private static final Function<Double, BigInteger> DBL_TO_BIG_INT = dbl -> BigDecimal.valueOf(dbl).toBigInteger();
    private static final Function<Double, BigDecimal> DBL_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Double, String> DBL_TO_STRING = flt -> BigDecimal.valueOf(flt).toString();

    private final Normalizer normalizer = Normalizer.instance();
    private final Random random = new Random();
    private final T zero;
    private final Function<Random, T> nonZero;
    private final Function<T, BigInteger> toBigInteger;
    private final Function<T, BigDecimal> toBigDecimal;
    private final Function<T, String> toString;
    private final long limit;

    public NormalizerNumberTest(final Class<T> c, final T zero, final Function<Random, T> nonZero,
                                final Function<T, BigInteger> toBigInteger,
                                final Function<T, BigDecimal> toBigDecimal,
                                final Function<T, String> toString, final long limit) {
        this.zero = zero;
        this.nonZero = nonZero;
        this.toBigInteger = toBigInteger;
        this.toBigDecimal = toBigDecimal;
        this.toString = toString;
        this.limit = limit;
    }

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        return Arrays.<Object[]>asList(
//                new Object[] {Byte.class, Byte.valueOf((byte) 0), NEW_BYTE, 299},
//                new Object[] {Short.class, Short.valueOf((short) 0), NEW_SHORT, 999},
//                new Object[] {Integer.class, Integer.valueOf(0), NEW_INTEGER, 999},
//                new Object[] {Long.class, Long.valueOf(0), NEW_LONG, 999},
                new Object[]{
                        Float.class, Float.valueOf(0), NEW_FLOAT, FLT_TO_BIG_INT, FLT_TO_BIG_DEC, FLT_TO_STRING, 999},
                new Object[]{
                        Double.class, Double.valueOf(0), NEW_DOUBLE, DBL_TO_BIG_INT, DBL_TO_BIG_DEC, DBL_TO_STRING, 999}/*,
                new Object[] {BigInteger.class, BigInteger.ZERO, NEW_BIG_INTEGER, 999},
                new Object[] {BigDecimal.class, BigDecimal.ZERO, NEW_BIG_DECIMAL, 999}*/
        );
    }

    @Test
    public final void fromNumber() {
        Stream.concat(
                Stream.of(zero),
                Stream.generate(() -> nonZero.apply(random)).limit(limit)
        ).forEach(value -> {
            final Normal normal = normalizer.normal(value);
//            System.out.printf("value            = %s%n", value);
//            System.out.printf("value.getClass() = %s%n", value.getClass());

            Assert.assertEquals(!zero.equals(value), normal.asBoolean());
            Assert.assertEquals(value.byteValue(), normal.asByte());
            Assert.assertEquals(value.shortValue(), normal.asShort());
            Assert.assertEquals(value.intValue(), normal.asInteger());
            Assert.assertEquals(value.longValue(), normal.asLong());
            Assert.assertEquals(toBigInteger.apply(value), normal.asBigInteger());
            Assert.assertEquals(value.floatValue(), normal.asFloat(), 0.00001);
            Assert.assertEquals(value.doubleValue(), normal.asDouble(), 0.00001);
            Assert.assertEquals(toBigDecimal.apply(value), normal.asBigDecimal());
            Assert.assertEquals(Character.valueOf((char) value.intValue()).charValue(), normal.asCharacter());
            Assert.assertEquals(toString.apply(value), normal.asString());
        });
    }
}