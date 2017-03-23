package net.team33.normal;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toList;

public class NormalTest {

    private Normal.Producer producer = Normal.producer();

    @Test
    public final void producer() {
        final Set<String> original = new HashSet<>(Arrays.asList("abc", "def", "ghi", "def", "ghi", "jkl", "jkl"));
        final Normal result = producer.normal(original);
        Assert.assertTrue(result instanceof Set<?>);
        Assert.assertEquals(
                new ArrayList<>(new TreeSet<>(original)),
                new ArrayList<>(result.toSet().stream().map(Object::toString).collect(toList()))
        );
    }
}