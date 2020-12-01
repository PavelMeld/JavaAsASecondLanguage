package io.github.javaasasecondlanguage.homework04.nodes;

import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.RoutingCollector;
import io.github.javaasasecondlanguage.homework04.ops.Reducer;

import java.util.Collection;
import java.util.Map;

import static io.github.javaasasecondlanguage.homework04.Utils.smartEquals;

public class ReducerNode  implements ProcNode {

    private final RoutingCollector collector = new RoutingCollector();

    private final Reducer reducer;
    private final Collection<String> groupByKeys;

    private Map<String, Object> currentGroupByEntries = null;

    public ReducerNode(Reducer reducer, Collection<String> groupByKeys) {
        this.reducer = reducer;
        this.groupByKeys = groupByKeys;
    }

    public final Reducer getReducer() {
        return reducer;
    }

    @Override
    public RoutingCollector getCollector() {
        return collector;
    }

    @Override
    public void push(Record inputRecord, int gateNumber) {
        if (gateNumber != 0) {
            throw new IllegalArgumentException("Gate does not exist: "
                    + gateNumber);
        }

        if (inputRecord.isTerminal()) {
            if (currentGroupByEntries != null) {
                reducer.signalGroupWasFinished(collector, currentGroupByEntries);
            }
            collector.collect(Record.terminalRecord());
            return;
        }

        var inputGroupByEntries = inputRecord.getAll(groupByKeys);
        if (!smartEquals(inputGroupByEntries, currentGroupByEntries)) {
            if (currentGroupByEntries != null) {
                reducer.signalGroupWasFinished(collector, currentGroupByEntries);
            }
            currentGroupByEntries = inputGroupByEntries;
        }

        reducer.apply(inputRecord, collector, currentGroupByEntries);
    }

}
