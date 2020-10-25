package io.github.javaasasecondlanguage.flitter.db;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class SubscriptionDatabase {
    Map<String, Set<String>> userSubscriptions;
    Map<String, Set<String>> userReaders;

    public void clear() {
        userSubscriptions = new HashMap<>();
        userReaders = new HashMap<>();
    }

    public SubscriptionDatabase() {
        clear();
    }

    public void subscribe(String reader, String source) {
        if (userSubscriptions.containsKey(reader)) {
            Set<String> sources = userSubscriptions.get(reader);
            sources.add(source);
        } else {
            userSubscriptions.put(reader, new HashSet(Set.of(source)));
        }

        if (userReaders.containsKey(source)) {
            Set<String> readers = userReaders.get(source);
            readers.add(reader);
        } else {
            userReaders.put(source, new HashSet(Set.of(reader)));
        }
    }

    public void unsubscribe(String reader, String source) {
        Set<String> sources = userSubscriptions.getOrDefault(reader, null);

        if (sources != null) {
            sources.remove(source);
        }

        Set<String> readers = userReaders.getOrDefault(source, null);

        if (readers != null) {
            readers.remove(reader);
        }
    }

    public Set<String> getSources(String reader) {
        return userSubscriptions.getOrDefault(reader, Collections.emptySet());
    }

    public boolean userIsSubscribed(String reader, String publisher) {
        if (!userSubscriptions.containsKey(reader)) {
            return false;
        }

        return userSubscriptions.get(reader).contains(publisher);
    }

    public Set<String> getSubscribers(String user) {
        return userReaders.getOrDefault(user, Collections.emptySet());
    }
}
