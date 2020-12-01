package io.github.javaasasecondlanguage.homework04.ops;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;

import java.util.Map;

public interface Reducer {
    void apply(Record inputRecord, Collector collector, Map<String, Object> groupByEntries);

    void signalGroupWasFinished(Collector collector, Map<String, Object> groupByEntries);
}
