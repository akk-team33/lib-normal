package net.team33.test.testing.v1;

import net.team33.libs.testing.v1.Normalizer;
import net.team33.test.testing.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum Expected {

    SAMPLE_(Sample.class, Expected::mapSample),
    BYTES__(byte[].class, Expected::mapBytes),
    SET____(Set.class, Expected::mapSet),
    LIST___(List.class, Expected::mapList),
    MAP____(Map.class, Expected::mapMap);

    @SuppressWarnings("rawtypes")
    private final Function mapper;
    private final Predicate<Object> filter;

    <T> Expected(final Class<T> subjectClass, final Function<T, ?> mapper) {
        this.filter = subjectClass::isInstance;
        this.mapper = mapper;
    }

    static Object map(final Object subject) {
        //noinspection unchecked
        return Stream.of(values())
                     .filter(value -> value.filter.test(subject))
                     .findAny()
                     .map(value -> value.mapper.apply(subject))
                     .orElse(subject);
    }

    private static Map<String, Object> mapSample(final Sample sample) {
        final Map<String, Object> result = new TreeMap<>();
        //result.put("additional", "an additional field to fail the test");
        result.put("thePrimitive", sample.getThePrimitive());
        result.put("theString", sample.getTheString());
        result.put("theNumber", sample.getTheNumber());
        result.put("theByteArray", mapBytes(sample.getTheByteArray()));
        result.put("theObject", sample.getTheObject());
        result.put("theList", mapList(sample.getTheList()));
        result.put("theSet", mapSet(sample.getTheSet()));
        result.put("theMap", mapMap(sample.getTheMap()));
        result.put(".theSuperPrimitive", sample.getTheSuperPrimitive());
        result.put(".theSuperString", sample.getTheSuperString());
        result.put(".theSuperNumber", sample.getTheSuperNumber());
        result.put(".theSuperByteArray", mapBytes(sample.getTheSuperByteArray()));
        result.put(".theSuperObject", sample.getTheSuperObject());
        result.put(".theSuperList", mapList(sample.getTheSuperList()));
        result.put(".theSuperSet", mapSet(sample.getTheSuperSet()));
        result.put(".theSuperMap", mapMap(sample.getTheSuperMap()));
        return result;
    }

    private static Set<?> mapSet(final Set<?> subjects) {
        return (null == subjects) ? null : subjects.stream()
                                                   .map(Expected::map)
                                                   .collect(Collectors.toCollection(() -> new TreeSet<>(Normalizer.ORDER)));
    }

    private static List<?> mapList(final List<?> subjects) {
        return (null == subjects) ? null : subjects.stream().map(Expected::map).collect(Collectors.toList());
    }

    private static Map<?, ?> mapMap(final Map<?, ?> subject) {
        return Optional.ofNullable(subject)
                       .map(map -> map.entrySet().stream()
                                      .collect(
                                              () -> new TreeMap<>(Normalizer.ORDER),
                                              (result, entry) -> result.put(
                                                      map(entry.getKey()),
                                                      map(entry.getValue())),
                                              Map::putAll))
                       .orElse(null);
    }

    private static List<Byte> mapBytes(final byte[] bytes) {
        final List<Byte> result = new ArrayList<>(bytes.length);
        for (final byte value : bytes) {
            result.add(value);
        }
        return result;
    }
}
