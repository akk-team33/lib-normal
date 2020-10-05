package net.team33.libs.testing.v1;

import net.team33.test.testing.v1.Competing;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static net.team33.test.testing.v1.Competing.*;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public final void ofSuperToSub() {
        assertEquals(0, Distance.of(C.class).from(C.class));
        assertEquals(0, Distance.of(D.class).from(D.class));
        assertEquals(0, Distance.of(E.class).from(E.class));
        assertEquals(0, Distance.of(F.class).from(F.class));

        //assertEquals(1, Distance.of(Competing.class).from(C.class));
        assertEquals(1, Distance.of(C.class).from(D.class));
        assertEquals(1, Distance.of(D.class).from(E.class));
        assertEquals(1, Distance.of(D.class).from(F.class));

        assertEquals(2, Distance.of(Object.class).from(C.class));
        assertEquals(2, Distance.of(Competing.class).from(D.class));
        assertEquals(2, Distance.of(C.class).from(E.class));
        assertEquals(2, Distance.of(C.class).from(F.class));

        assertEquals(3, Distance.of(Object.class).from(D.class));
        assertEquals(3, Distance.of(Competing.class).from(E.class));
        assertEquals(3, Distance.of(Competing.class).from(F.class));

        assertEquals(4, Distance.of(Object.class).from(E.class));
        assertEquals(4, Distance.of(Object.class).from(F.class));
    }

    @Test
    public final void ofAnyToDivergent() {
        assertEquals(0x8000, Distance.of(Date.class).from(ArrayList.class));
    }
}