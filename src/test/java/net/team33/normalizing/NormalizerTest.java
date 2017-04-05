package net.team33.normalizing;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NormalizerTest {

    private static final String A_STRING = "This is a String";

    private final Normalizer normalizer = Normalizer.instance();

    @Test
    public final void asInteger() {
        Arrays.asList(0, 278, -2409)
                .forEach(value -> Arrays.<Function<Integer, ? extends Number>>asList(
                        Integer::valueOf,
                        Long::valueOf,
                        Float::valueOf,
                        Double::valueOf,
                        BigInteger::valueOf,
                        BigDecimal::valueOf
                ).forEach(map -> Assert.assertEquals(
                        Integer.valueOf(value),
                        normalizer.normal(map.apply(value)).asInteger()
                )));
    }

    @Test
    public final void asBigIntegerFromInt() {
        Arrays.asList(0, 278, -2409)
                .forEach(value -> Arrays.<Function<Integer, ? extends Number>>asList(
                        Integer::valueOf,
                        Long::valueOf,
                        Float::valueOf,
                        Double::valueOf,
                        BigInteger::valueOf,
                        BigDecimal::valueOf
                ).forEach(map -> Assert.assertEquals(
                        BigInteger.valueOf(value),
                        normalizer.normal(map.apply(value)).asBigInteger()
                )));
    }

    @Test
    public final void asBigIntegerFromDouble() {
        Arrays.asList(0.0, 3.141592654, -24.0970694)
                .forEach(value -> Arrays.<Function<Double, ? extends Number>>asList(
                        Double::valueOf,
                        BigDecimal::valueOf
                ).forEach(map -> Assert.assertEquals(
                        BigInteger.valueOf(value.longValue()),
                        normalizer.normal(map.apply(value)).asBigInteger()
                )));
    }

    @Test
    public final void asString() {
        Assert.assertEquals(
                A_STRING,
                normalizer.normal(A_STRING).asString()
        );
        Assert.assertEquals(
                A_STRING,
                normalizer.normal(new StringBuilder(A_STRING)).asString()
        );
    }

    @Test
    public final void asSet() {
        final Set<String> original = new TreeSet<>(Arrays.asList(
                "These",
                "are",
                "several",
                "are",
                "several",
                "strings",
                "strings"));
        Assert.assertEquals(
                original,
                normalizer.normal(original).asSet().stream().map(Normal::asString).collect(Collectors.toSet())
        );
        Assert.assertEquals(
                new ArrayList<>(original),
                normalizer.normal(original).asSet().stream().map(Normal::asString).collect(Collectors.toList())
        );
    }
}