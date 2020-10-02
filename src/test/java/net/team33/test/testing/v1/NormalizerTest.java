package net.team33.test.testing.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.team33.libs.testing.v1.Uniform;
import net.team33.libs.testing.v1.Normalizer;
import net.team33.test.testing.Sample;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class NormalizerTest {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Function<Object, Uniform> UNIFORM = Uniform.builder(GSON::toJson);

    private final Random random = new Random();
    private final Normalizer normalizer = Normalizer.builder()
                                                    .addMethod(Set.class, Normalizer::normalSet)
                                                    .addMethod(Collection.class, Normalizer::normalList)
                                                    .addMethod(Map.class, Normalizer::normalMap)
                                                    .addMethod(Sample.class, Normalizer::normalFieldMap)
                                                    .build();

    @Test
    public final void normal() {
        final Sample sample = anySample();
        final Object expected = expected(sample);
        final Object result = normalizer.normal(sample);
        System.out.println(result);
        assertEquals(uniform(expected), uniform(result));

        assertEquals(GSON.toJson(expected), uniform(expected).toString());
        assertEquals(GSON.toJson(result), uniform(result).toString());

        assertEquals(uniform(expected).toString(), GSON.toJson(result));
        assertEquals(GSON.toJson(expected), uniform(result).toString());
    }

    private Uniform uniform(final Object subject) {
        return UNIFORM.apply(subject);
    }

    private static Map<String, Object> expected(final Sample sample) {
        final Map<String, Object> result = new TreeMap<>();
        result.put("additional", "an additional field to fail the test");
        result.put("thePrimitive", sample.getThePrimitive());
        result.put("theString", sample.getTheString());
        result.put("theNumber", sample.getTheNumber());
        result.put("theByteArray", expectedBytes(sample.getTheByteArray()));
        result.put("theObject", sample.getTheObject());
        result.put("theList", expectedListOf(sample.getTheList()));
        result.put("theSet", expectedSetOf(sample.getTheSet()));
        result.put("theMap", expectedMapOf(sample.getTheMap()));
        result.put(".theSuperPrimitive", sample.getTheSuperPrimitive());
        result.put(".theSuperString", sample.getTheSuperString());
        result.put(".theSuperNumber", sample.getTheSuperNumber());
        result.put(".theSuperByteArray", expectedBytes(sample.getTheSuperByteArray()));
        result.put(".theSuperObject", sample.getTheSuperObject());
        result.put(".theSuperList", expectedListOf(sample.getTheSuperList()));
        result.put(".theSuperSet", expectedSetOf(sample.getTheSuperSet()));
        result.put(".theSuperMap", expectedMapOf(sample.getTheSuperMap()));
        return result;
    }

    private static Set<?> expectedSetOf(final Set<?> subjects) {
        return (null == subjects) ? null : subjects.stream()
                                                   .map(NormalizerTest::expectedObject)
                                                   .collect(Collectors.toCollection(() -> new TreeSet<>(Normalizer.ORDER)));
    }

    private static List<?> expectedListOf(final List<?> subjects) {
        return (null == subjects) ? null : subjects.stream().map(NormalizerTest::expectedObject).collect(Collectors.toList());
    }

    private static Map<?, ?> expectedMapOf(final Map<?, ?> subject) {
        return Optional.ofNullable(subject)
                       .map(map -> map.entrySet().stream()
                                      .collect(
                                              () -> new TreeMap(Normalizer.ORDER),
                                              (result, entry) -> result.put(
                                                      expectedObject(entry.getKey()),
                                                      expectedObject(entry.getValue())),
                                              Map::putAll))
                       .orElse(null);
    }

    private static Object expectedObject(final Object subject) {
        if (subject instanceof byte[]) return expectedBytes((byte[]) subject);
        if (subject instanceof Sample) return expected((Sample) subject);
        if (subject instanceof Set) return expectedSetOf((Set<?>) subject);
        if (subject instanceof List) return expectedListOf((List<?>) subject);
        if (subject instanceof Map) return expectedMapOf((Map<?, ?>) subject);
        return subject;
    }

    private static List<Byte> expectedBytes(final byte[] bytes) {
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
