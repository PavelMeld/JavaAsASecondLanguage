package io.github.javaasasecondlanguage.flitter.db;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeedDatabase {
    Map<String, List<String>> userFeed;

    public void clear() {
        userFeed = new HashMap<>();
    }

    public FeedDatabase() {
        clear();
    }
}
