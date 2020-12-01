package io.github.javaasasecondlanguage.homework04.nodes;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.RoutingCollector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinerNode implements ProcNode {
    static final int NUM_GATES = 2;
    List<String> keyColumns;
    RoutingCollector collector = new RoutingCollector();
    Deque<Record> [] inputs;
    Integer [] terminations;

    public JoinerNode(List<String> keyColumns) {
        this.keyColumns = keyColumns;
        inputs = new ArrayDeque[NUM_GATES];
        terminations = new Integer[NUM_GATES];
        terminations[0] = 0;
        terminations[1] = 0;
        inputs[0] = new ArrayDeque<>();
        inputs[1] = new ArrayDeque<>();
    }

    @Override
    public RoutingCollector getCollector() {
        return collector;
    }

    @Override
    public void push(Record inputRecord, int gateNumber) {
        if (gateNumber >= NUM_GATES) {
            throw new IllegalArgumentException("Gate does not exist: " + gateNumber);
        }

        inputs[gateNumber].addLast(inputRecord);

        if (inputRecord.isTerminal()) {
            terminations[gateNumber]++;
        }

        while (terminations[0] > 0 && terminations[1] > 0) {
            List<Record> right = new ArrayList<>();
            List<Record> left = new ArrayList<>();

            Record rec = inputs[1].removeFirst();
            while (!rec.isTerminal()) {
                right.add(rec);
                rec = inputs[1].removeFirst();
            }

            rec = inputs[0].removeFirst();
            while (!rec.isTerminal()) {
                left.add(rec);
                rec = inputs[0].removeFirst();
            }

            for (Record l : left) {
                for (Record r : right) {
                    boolean found = true;

                    for (String col : keyColumns) {
                        if (!l.get(col).equals(r.get(col))) {
                            found = false;
                            break;
                        }
                    }

                    if (found) {
                        Map<String, Object> rdata = r.getData();
                        Record newRecord = new Record(l.getData());
                        rdata.entrySet().forEach(entry ->
                                newRecord.set(entry.getKey(), entry.getValue()));
                        collector.collect(newRecord);
                    }
                }
            }

            terminations[0]--;
            terminations[1]--;

            collector.collect(Record.terminalRecord());
        }
    }
}
