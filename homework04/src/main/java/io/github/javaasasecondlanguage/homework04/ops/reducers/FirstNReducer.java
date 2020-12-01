package io.github.javaasasecondlanguage.homework04.ops.reducers;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.Reducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Returns at most maxAmount records per group.
 */
public class FirstNReducer implements Reducer {
    int maxAmount;

    List<Record> output;

    public FirstNReducer(int maxAmount) {
        this.maxAmount = maxAmount;
        output = new ArrayList<>(maxAmount);
    }

    @Override
    public void apply(Record inputRecord, Collector collector, Map<String, Object> groupByEntries) {
        if (output.size() >= maxAmount) {
            return;
        }

        output.add(inputRecord.copy());
    }

    @Override
    public void signalGroupWasFinished(Collector collector, Map<String, Object> groupByEntries) {
        output.forEach(collector::collect);
        output.clear();
    }
}
