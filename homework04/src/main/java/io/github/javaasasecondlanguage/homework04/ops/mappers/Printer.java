package io.github.javaasasecondlanguage.homework04.ops.mappers;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.Mapper;

/**
 * Simply logs all passing records. Useful for debugging.
 */
public class Printer implements Mapper {

    private final String prefix;

    public Printer(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void apply(Record inputRecord, Collector collector) {
        System.out.printf("%s: %s%n", prefix, inputRecord);
        if (inputRecord.isTerminal()) {
            System.out.printf("T");
        }
        collector.collect(inputRecord);
    }
}
