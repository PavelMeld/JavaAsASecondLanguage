package io.github.javaasasecondlanguage.flitter.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionTestUtils {

    public static <A> Set<Map<String, A>> retainKeys(Collection<Map<String, A>> inputCollection,
                                                     String... retainedKeys) {
        return inputCollection
                .stream()
                .map(m -> retainKeys(m, retainedKeys))
                .collect(Collectors.toSet());
    }

    public static <A> Map<String, A> retainKeys(Map<String, A> inputMap, String... retainedKeys) {
        var retainedKeySet = Set.of(retainedKeys);
        var outputMap = new LinkedHashMap<>(inputMap);
        outputMap.keySet().retainAll(retainedKeySet);
        return outputMap;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> castToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        } else {
            throw new IllegalArgumentException("Object is not a map: " + obj);
        }
    }

    public static List<Map<String, Object>> castToMapList(Object inputObject) {
        var outputList = new ArrayList<Map<String, Object>>();

        var inputList = (List) inputObject;
        for (var obj : inputList) {
            Map<String, Object> map = castToMap(obj);
            outputList.add(map);
        }

        return outputList;
    }
}
