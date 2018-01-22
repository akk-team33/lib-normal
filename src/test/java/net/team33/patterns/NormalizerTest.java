package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NormalizerTest {

    private static final boolean[] BOOLEANS = {true, false};
    private static final int SPACE = ' ';

    private final Random random = new Random();
    private final Normalizer subject = Normalizer.builder()
            .build();

    @SuppressWarnings("rawtypes")
    private static Object array(final Class elmClass, final int length, final Supplier supplier) {
        final Object result = Array.newInstance(elmClass, length);
        for (int i = 0; i < length; ++i) {
            Array.set(result, i, supplier.get());
        }
        return result;
    }

    private <E> E[] objArray(final int length, final Supplier<E> supplier) {
        //noinspection unchecked
        return (E[]) array(supplier.get().getClass(), length, supplier);
    }

    private char[] charArray_(final int length) {
        //noinspection NumericCastThatLosesPrecision
        return (char[]) array(Character.TYPE, length, () -> (char) random.nextInt(256));
    }

    private char[] charArray(final int length) {
        return new char[]{'\0', '\01', '\023', '\345', '\12', '\b', '\n', ' ', 'a', '2', '+', (char) 250};
    }

    private int[] intArray(final int length) {
        return (int[]) array(Integer.TYPE, length, random::nextInt);
    }

    private double[] doubleArray(final int length) {
        return (double[]) array(Double.TYPE, length, random::nextDouble);
    }

    @Test
    public final void normalBoolean() {
        for (final boolean sample : BOOLEANS) {
            Assert.assertEquals(
                    String.valueOf(sample),
                    subject.normal(sample).toString());
        }
    }

    @Test
    public final void normalInt() {
        for (final int sample : intArray(10)) {
            final Normal result = subject.normal(sample);
            Assert.assertEquals(String.valueOf(sample), result.toString());
            final Normal bigResult = subject.normal(BigInteger.valueOf(sample));
            Assert.assertEquals(result, bigResult);
        }
    }

    @Test
    public final void normalDouble() {
        for (final double sample : doubleArray(10)) {
            final Normal result = subject.normal(sample);
            Assert.assertEquals(String.valueOf(sample), result.toString());
            final Normal bigResult = subject.normal(BigDecimal.valueOf(sample));
            Assert.assertEquals(result, bigResult);
        }
    }

    @Test
    public final void normalChar() {
        for (final CharCheck check : Arrays.asList(
                new CharCheck('\0', "\"\\000\""),
                new CharCheck('\01', "\"\\001\""),
                new CharCheck('\023', "\"\\023\""),
                new CharCheck('\145', "e"),
                new CharCheck(' ', "\" \""),
                new CharCheck('a', "a"),
                new CharCheck('b', "b")
        )) {
            check.ckeck(charCheck -> {
                Assert.assertEquals(charCheck.string, charCheck.normal.toString());
            });
        }

        for (char c = 128; c < 256; ++c) {
            new CharCheck(c, String.valueOf(c)).ckeck(cc -> {
                Assert.assertEquals(cc.string, cc.normal.toString());
            });
        }
    }

    private class CharCheck {

        private final char value;
        private final Normal normal;
        private final String string;

        private CharCheck(final char value, final String string) {
            this.value = value;
            this.normal = subject.normal(value);
            this.string = string;
        }

        public void ckeck(final Consumer<CharCheck> consumer) {
            consumer.accept(this);
        }
    }
}