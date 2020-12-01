package io.github.javaasasecondlanguage.homework04.nodes;

import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.RoutingCollector;
import io.github.javaasasecondlanguage.homework04.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;

public class SorterNode  implements ProcNode {

    private final RoutingCollector collector = new RoutingCollector();
    private final List<Record> accumulatedRecords = new ArrayList<>();

    private final Comparator<Record> recordComparator;

    public SorterNode(List<String> keyColumns, SortOrder order) {
        Comparator<Record> comparator = (o1, o2) -> Utils.compareRecordsByKeys(o1, o2, keyColumns);
        if (order == SortOrder.DESCENDING) {
            this.recordComparator = comparator.reversed();
        } else {
            this.recordComparator = comparator;
        }
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

        if (!inputRecord.isTerminal()) {
            accumulatedRecords.add(inputRecord);
            return;
        }

        sort(accumulatedRecords, recordComparator);
        for (Record record : accumulatedRecords) {
            collector.collect(record);
        }
        collector.collect(Record.terminalRecord());
    }
}
