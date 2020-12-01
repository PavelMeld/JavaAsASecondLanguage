package io.github.javaasasecondlanguage.homework04.ops.mappers;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.Mapper;

/**
 * Shifts selected column to lowercase.
 */
public class LowerCaseMapper implements Mapper {
    String column;

    public LowerCaseMapper(String column) {
        this.column = column;
    }

    @Override
    public void apply(Record inputRecord, Collector collector) {
        Record outputRecord = inputRecord.copy();

        String value = outputRecord.getString(column);
        outputRecord.set(column, value.toLowerCase());
        collector.collect(outputRecord);
    }
}
