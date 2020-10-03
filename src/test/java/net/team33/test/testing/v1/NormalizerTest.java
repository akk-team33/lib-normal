package net.team33.test.testing.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.team33.libs.testing.v1.Normalizer;
import net.team33.libs.testing.v1.Uniform;
import net.team33.test.testing.Sample;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

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

    private static Uniform pretty(final Object subject) {
        return UNIFORM.apply(subject);
    }

    @Test
    public final void normal() {
        final Sample sample = anySample();
        final Object expected = Expected.map(sample);
        final Object result = normalizer.normal(sample);
        System.out.println(result);
        assertEquals(pretty(expected), pretty(result));
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
        final byte[] bytes = new byte[random.nextInt(32) + 1];
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
