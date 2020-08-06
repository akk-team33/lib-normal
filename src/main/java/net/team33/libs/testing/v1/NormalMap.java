package net.team33.libs.testing.v1;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class NormalMap extends AbstractMap<Object, Object> {

    @SuppressWarnings("CallToSuspiciousStringMethod")
    private static final Comparator<Object> ORDER = (left, right) -> {
        final String leftString = left.toString();
        final String rightString = right.toString();
        final int result = leftString.compareToIgnoreCase(rightString);
        if (0 != result) {
            return result;
        } else {
            final int result2 = leftString.compareTo(rightString);
            if ((0 != result2) || (left.equals(right))) {
                return result2;
            } else {
                return Integer.compare(System.identityHashCode(left), System.identityHashCode(right));
            }
        }
    };

    private final Map<Object, Object> backing = new TreeMap<>(ORDER);

    @Override
    public final Object put(final Object key, final Object value) {
        return backing.put(key, value);
    }

    @Override
    public final Set<Entry<Object, Object>> entrySet() {
        return backing.entrySet();
    }
}
