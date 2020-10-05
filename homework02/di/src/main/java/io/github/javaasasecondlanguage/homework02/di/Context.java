package io.github.javaasasecondlanguage.homework02.di;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;

public class Context {
    Map<Class<?>, Map<String, Object>> injections;
    static Context globalContext = null;

    void addToInjections(Map<Class<?>, Map<String, Object>> destination,
                         Class<?> label, Object obj, String qualifier) {
        Map<String, Object> node;

        if (destination.containsKey(label)) {
            node = destination.get(label);
        } else {
            node = new HashMap<>();
            destination.put(label, node);
        }

        if (node.containsKey(qualifier)) {
            throw new KeyAlreadyExistsException("Object of class " + label
                    + " and qualifier " + qualifier + " already exists.");
        }
        node.put(qualifier, obj);
    }

    public <T> Context register(T object, String qualifier) throws KeyAlreadyExistsException {
        Class<?> objectClass = object.getClass();
        Class<?> [] interfaces;

        while (!objectClass.equals(Object.class)) {
            addToInjections(injections, objectClass, object, qualifier);

            interfaces = objectClass.getInterfaces();
            while (interfaces.length == 1) {
                addToInjections(injections, interfaces[0], object, qualifier);
                interfaces = interfaces[0].getInterfaces();
            }

            objectClass = objectClass.getSuperclass();
        }

        return this;
    }

    public <T> Context register(T object) throws KeyAlreadyExistsException {
        return this.register(object, null);
    }

    public <T> T findInjection(Class<T> clazz, String qualifier) {
        Map<String, Object> node = injections.get(clazz);
        T candidate = null;

        if (node != null) {
            candidate = (T) node.get(qualifier);
        }

        return candidate;
    }

    public static Context getGlobalContext() {
        return globalContext;
    }

    public Context() {
        if (globalContext == null) {
            globalContext = this;
        }

        injections = new HashMap<>();
    }
}
