package io.github.javaasasecondlanguage.homework04.ops.reducers;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.Reducer;

import java.util.Map;

/**
 * Counts sum of values in a specified column for each group and returns a single record with a sum.
 */
public class SumReducer implements Reducer {
    String inputColumn;
    String outputColumn;
    Double total;

    public SumReducer(String inputColumn, String outputColumn) {
        this.inputColumn = inputColumn;
        this.outputColumn = outputColumn;
        total = 0.0;
    }

    @Override
    public void apply(Record inputRecord, Collector collector, Map<String, Object> groupByEntries) {
        total += inputRecord.getDouble(inputColumn);
    }

    @Override
    public void signalGroupWasFinished(Collector collector, Map<String, Object> groupByEntries) {
        collector.collect(new Record(groupByEntries).set(outputColumn, total));
        total = 0.0;
    }

}
