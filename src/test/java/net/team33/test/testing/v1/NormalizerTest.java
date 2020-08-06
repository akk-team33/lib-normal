package net.team33.test.testing.v1;

import net.team33.libs.testing.v1.Normalizer;
import net.team33.test.testing.Sample;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class NormalizerTest {

    private final Random random = new Random();
    private final Normalizer normalizer = Normalizer.DEFAULT;

    @Test
    public final void normal() {
        final Sample sample = anySample();
        final Object result = normal(sample);
        System.out.println(result);
        assertEquals(expected(sample), result);
    }

    private Object normal(final Sample sample) {
        return normalizer.normal(sample);
    }

    private static Map<String, Object> expected(final Sample sample) {
        final Map<String, Object> result = new TreeMap<>();
        result.put("thePrimitive", sample.getThePrimitive());
        result.put("theString", sample.getTheString());
        result.put("theNumber", sample.getTheNumber());
        result.put("theByteArray", expected(sample.getTheByteArray()));
        result.put("theObject", sample.getTheObject());
        result.put("theList", expected(sample.getTheList()));
        result.put("theSet", expected(sample.getTheSet()));
        result.put("theMap", expected(sample.getTheMap()));
        result.put(".theSuperPrimitive", sample.getTheSuperPrimitive());
        result.put(".theSuperString", sample.getTheSuperString());
        result.put(".theSuperNumber", sample.getTheSuperNumber());
        result.put(".theSuperByteArray", expected(sample.getTheSuperByteArray()));
        result.put(".theSuperObject", sample.getTheSuperObject());
        result.put(".theSuperList", expected(sample.getTheSuperList()));
        result.put(".theSuperSet", expected(sample.getTheSuperSet()));
        result.put(".theSuperMap", expected(sample.getTheSuperMap()));
        return result;
    }

    private static Set<?> expected(final Set<?> subjects) {
        return (null == subjects) ? null : subjects.stream().map(NormalizerTest::expected).collect(Collectors.toSet());
    }

    private static List<?> expected(final List<?> subjects) {
        return (null == subjects) ? null : subjects.stream().map(NormalizerTest::expected).collect(Collectors.toList());
    }

    private static Map<?, ?> expected(final Map<?, ?> subjects) {
        return (null == subjects)
                ? null
                : subjects.entrySet().stream().collect(
                HashMap::new, (map, entry) -> map.put(
                        expected(entry.getKey()),
                        expected(entry.getValue())), Map::putAll);
    }

    private static Object expected(final Object subject) {
        if (subject instanceof byte[]) return expected((byte[]) subject);
        if (subject instanceof Sample) return expected((Sample) subject);
        if (subject instanceof Set) return expected((Set<?>) subject);
        if (subject instanceof List) return expected((List<?>) subject);
        if (subject instanceof Map) return expected((Map<?, ?>) subject);
        return subject;
    }

    private static List<Byte> expected(final byte[] bytes) {
        final List<Byte> result = new ArrayList<>(bytes.length);
        for (final byte value : bytes) {
            result.add(value);
        }
        return result;
    }

    private Sample anySample() {
        return new Sample().setThePrimitive(random.nextInt())
                           .setTheString(anyString())
                           .setTheNumber(random.nextDouble())
                           .setTheByteArray(anyBytes())
                           .setTheList(anyList())
                           .setTheSet(anyList())
                           .setTheMap(anyMap())
                           .setTheSuperPrimitive(random.nextInt())
                           .setTheSuperString(anyString())
                           .setTheSuperNumber(random.nextGaussian())
                           .setTheSuperByteArray(anyBytes())
                           .setTheSuperList(anyList())
                           .setTheSuperSet(anyList())
                           .setTheSuperMap(anyMap());
    }

    private byte[] anyBytes() {
        final byte[] bytes = new byte[random.nextInt(32)+1];
        random.nextBytes(bytes);
        return bytes;
    }

    private String anyString() {
        return new BigInteger(random.nextInt(128) + 1, random).toString(Character.MAX_RADIX);
    }

    private List<Object> anyList() {
        return asList(random.nextInt(), anyString(), anyBytes(), new Date(), new Sample(), null);
    }

    private Map<Object, Object> anyMap() {
        final Map<Object, Object> result = new HashMap<>(4);
        result.put(anyString(), anyBytes());
        result.put(random.nextInt(), new Sample());
        result.put(new Sample(), new Date());
        result.put(null, random.nextDouble());
        result.put(random.nextLong(), null);
        return result;
    }
}
