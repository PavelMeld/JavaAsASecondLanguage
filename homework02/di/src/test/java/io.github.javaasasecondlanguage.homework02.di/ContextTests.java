package io.github.javaasasecondlanguage.homework02.di;

import org.junit.jupiter.api.Test;
import javax.management.openmbean.KeyAlreadyExistsException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContextTests {

    @Test
    void detectDuplicateClasses() {
        assertThrows(
            KeyAlreadyExistsException.class,
            () -> new Context()
                    .register(123)
                    .register(456));
    }

    @Test
    void detectDuplicateClassesWithLabels() {
        assertThrows(KeyAlreadyExistsException.class, () -> new Context()
                    .register(123, "lbl")
                    .register(456, "lbl"));
    }

    @Test
    void detectDuplicateClassesWithoutLabels() {
        assertThrows(KeyAlreadyExistsException.class, () -> new Context()
                    .register(123)
                    .register(456));
    }

    @Test
    void sameClassesWithDifferentLabels() {
        assertDoesNotThrow(() -> new Context()
                    .register(123, "lbl1")
                    .register(456, "lbl2"));
    }

    @Test
    void differentClassesWithSameLabel() {
        String label = "lbl1";
        assertDoesNotThrow(() -> new Context()
                    .register(123, label)
                    .register("123", label));
    }

    @Test
    void simpleFindTest() {
        Integer a = 123;
        Integer b = 456;
        String l1 = "lbl1";
        String l2 = "lbl2";
        Context ctx = new Context()
                .register(a, l1)
                .register(b, l2);

        assertSame(a, ctx.findInjection(Integer.class, l1));
        assertSame(b, ctx.findInjection(Integer.class, l2));
    }

    @Test
    void findTestWithNullLabel() {
        Integer a = 123;
        Integer b = 456;
        String l1 = "lbl1";
        String l2 = null;
        Context ctx = new Context()
                .register(a, l1)
                .register(b, l2);

        assertSame(a, ctx.findInjection(Integer.class, l1));
        assertSame(b, ctx.findInjection(Integer.class, l2));
    }

    @Test
    void superClassTest() {
        Integer a = 123;

        Context ctx = new Context().register(a, null);

        assertSame(a, ctx.findInjection(a.getClass().getSuperclass(), null));
    }

}
