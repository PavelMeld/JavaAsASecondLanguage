package io.github.javaasasecondlanguage.homework04.ops.reducers;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.Reducer;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Calculate frequency of values in column for each group.
 */
public class WordFrequencyReducer implements Reducer {
    String termColumn;
    String outputColumn;
    Map<String, Integer> index;

    public WordFrequencyReducer(String termColumn, String outputColumn) {
        this.termColumn = termColumn;
        this.outputColumn = outputColumn;
        index = new TreeMap<>();
    }

    @Override
    public void apply(Record inputRecord, Collector collector, Map<String, Object> groupByEntries) {
        String key = (String) inputRecord.get(termColumn);

        if (index.keySet().contains(key)) {
            index.put(key, index.get(key) + 1);
        } else {
            index.put(key, 1);
        }
    }

    @Override
    public void signalGroupWasFinished(Collector collector, Map<String, Object> groupByEntries) {
        Double total = index.values().stream()
                .collect(Collectors.summingDouble(Integer::doubleValue));

        index.entrySet().stream()
                .map(entry -> {
                    Double tf = index.get(entry.getKey()) / total;
                    return new Record(groupByEntries).set(termColumn,
                            entry.getKey()).set(outputColumn, tf);
                })
                .forEach(collector::collect);

        index.clear();
    }

}
