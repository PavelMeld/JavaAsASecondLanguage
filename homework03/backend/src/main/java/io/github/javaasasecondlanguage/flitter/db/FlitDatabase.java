package io.github.javaasasecondlanguage.flitter.db;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FlitDatabase {
    public void clear() {
        flits = new ArrayList<>(10);
    }

    public class FlitRecord {
        public final String user;
        public final String content;

        public FlitRecord(String user, String content) {
            this.user = user;
            this.content = content;
        }
    }

    ArrayList<FlitRecord> flits;

    public FlitDatabase() {
        clear();
    }

    public void addFlit(String user, String content) {
        flits.add(0, new FlitRecord(user, content));
    }

    public List<FlitRecord> lastTen() {
        if (flits.size() == 0) {
            return Collections.emptyList();
        }

        return flits.subList(0, Math.min(10, flits.size()));
    }

    public List<FlitRecord> flitsFromUser(String user) {
        if (flits.size() == 0) {
            return Collections.emptyList();
        }

        return flits.stream()
                .filter((entry) -> entry.user.equals((user)))
                .collect(Collectors.toList());
    }

}
