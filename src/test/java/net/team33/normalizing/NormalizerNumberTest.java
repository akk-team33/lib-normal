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
    private static final Function<Byte, BigInteger> BYT_TO_BIG_INT = BigInteger::valueOf;
    private static final Function<Byte, BigDecimal> BYT_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Byte, String> BYT_TO_STRING = Object::toString;
    private static final Function<Short, BigInteger> SHT_TO_BIG_INT = BigInteger::valueOf;
    private static final Function<Short, BigDecimal> SHT_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Short, String> SHT_TO_STRING = Object::toString;
    private static final Function<Integer, BigInteger> INT_TO_BIG_INT = BigInteger::valueOf;
    private static final Function<Integer, BigDecimal> INT_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Integer, String> INT_TO_STRING = Object::toString;
    private static final Function<Long, BigInteger> LNG_TO_BIG_INT = BigInteger::valueOf;
    private static final Function<Long, BigDecimal> LNG_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Long, String> LNG_TO_STRING = Object::toString;
    private static final Function<Float, BigInteger> FLT_TO_BIG_INT = flt -> BigDecimal.valueOf(flt).toBigInteger();
    private static final Function<Float, BigDecimal> FLT_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Float, String> FLT_TO_STRING = flt -> BigDecimal.valueOf(flt).toString();
    private static final Function<Double, BigInteger> DBL_TO_BIG_INT = dbl -> BigDecimal.valueOf(dbl).toBigInteger();
    private static final Function<Double, BigDecimal> DBL_TO_BIG_DEC = BigDecimal::valueOf;
    private static final Function<Double, String> DBL_TO_STRING = flt -> BigDecimal.valueOf(flt).toString();
    private static final Function<BigInteger, BigInteger> BIG_INT_TO_BIG_INT = bi -> bi;
    private static final Function<BigInteger, BigDecimal> BIG_INT_TO_BIG_DEC = BigDecimal::new;
    private static final Function<BigInteger, String> BIG_INT_TO_STRING = BigInteger::toString;
    private static final Function<BigDecimal, BigInteger> BIG_DEC_TO_BIG_INT = BigDecimal::toBigInteger;
    private static final Function<BigDecimal, BigDecimal> BIG_DEC_TO_BIG_DEC = bd -> bd;
    private static final Function<BigDecimal, String> BIG_DEC_TO_STRING = BigDecimal::toString;

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
                new Object[]{
                        Byte.class, Byte.valueOf((byte) 0), NEW_BYTE,
                        BYT_TO_BIG_INT, BYT_TO_BIG_DEC, BYT_TO_STRING, 299},
                new Object[]{
                        Short.class, Short.valueOf((short) 0), NEW_SHORT,
                        SHT_TO_BIG_INT, SHT_TO_BIG_DEC, SHT_TO_STRING, 999},
                new Object[]{
                        Integer.class, Integer.valueOf(0), NEW_INTEGER,
                        INT_TO_BIG_INT, INT_TO_BIG_DEC, INT_TO_STRING, 999},
                new Object[]{
                        Long.class, Long.valueOf(0), NEW_LONG,
                        LNG_TO_BIG_INT, LNG_TO_BIG_DEC, LNG_TO_STRING, 999},
                new Object[]{
                        Float.class, Float.valueOf(0), NEW_FLOAT,
                        FLT_TO_BIG_INT, FLT_TO_BIG_DEC, FLT_TO_STRING, 999},
                new Object[]{
                        Double.class, Double.valueOf(0), NEW_DOUBLE,
                        DBL_TO_BIG_INT, DBL_TO_BIG_DEC, DBL_TO_STRING, 999},
                new Object[]{
                        BigInteger.class, BigInteger.ZERO, NEW_BIG_INTEGER,
                        BIG_INT_TO_BIG_INT, BIG_INT_TO_BIG_DEC, BIG_INT_TO_STRING, 999},
                new Object[]{
                        BigDecimal.class, BigDecimal.ZERO, NEW_BIG_DECIMAL,
                        BIG_DEC_TO_BIG_INT, BIG_DEC_TO_BIG_DEC, BIG_DEC_TO_STRING, 999}
        );
    }

//    @Test
//    public final void asList() {
//        final T value = nonZero.apply(random);
//        final Normal normal = normalizer.normal(value);
//        final List<Normal> list = normal.asList();
//        Assert.assertEquals(1, list.size());
//        Assert.assertSame(normal, list.get(0));
//    }

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
            Assert.assertEquals(value.floatValue(), normal.asFloat(), 0.0);
            Assert.assertEquals(value.doubleValue(), normal.asDouble(), 0.0);
            Assert.assertEquals(toBigDecimal.apply(value), normal.asBigDecimal());
            Assert.assertEquals(Character.valueOf((char) value.intValue()).charValue(), normal.asCharacter());
            Assert.assertEquals(toString.apply(value), normal.asString());
        });
    }
}