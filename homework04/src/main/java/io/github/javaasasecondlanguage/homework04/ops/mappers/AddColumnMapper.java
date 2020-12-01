package io.github.javaasasecondlanguage.homework04.ops.mappers;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.Mapper;

import java.util.function.Function;

/**
 * Calculates a new value from record using specified lambda. Then saves it into the outputColumn.
 */
public class AddColumnMapper implements Mapper {
    Function<Record, ?> recordModifier;
    String outputColumn;

    public AddColumnMapper(String outputColumn, Function<Record, ?> recordModifier) {
        this.recordModifier = recordModifier;
        this.outputColumn = outputColumn;
    }

    @Override
    public void apply(Record inputRecord, Collector collector) {
        Record output = inputRecord.copy().set(outputColumn, recordModifier.apply(inputRecord));
        collector.collect(output);
    }
}
