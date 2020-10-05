package io.github.javaasasecondlanguage.homework02.di;

public class Injector {
    public static <T> T inject(Class<T> clazz) {
        return inject(clazz, null);
    }

    public static <T> T inject(Class<T> clazz, String qualifier) {
        if (Context.getGlobalContext() == null) {
            throw new IllegalArgumentException("Context not instantiated");
        }

        return Context.getGlobalContext().findInjection(clazz, qualifier);
    }
}
