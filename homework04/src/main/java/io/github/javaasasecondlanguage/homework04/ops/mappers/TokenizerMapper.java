package io.github.javaasasecondlanguage.homework04.ops.mappers;

import io.github.javaasasecondlanguage.homework04.Collector;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.Mapper;

import java.util.Arrays;
import java.util.Collections;

import static java.util.List.of;

/**
 * Splits text in the specified column into words, then creates a new record with each word.
 *
 * Split should happen on the following symbols: " ", ".", ",", "!", ";", "?", "'", ":"
 */
public class TokenizerMapper implements Mapper {

    // Thanks for this pattern =)
    private static final String SPLIT_PATTERN = "[\\s,\\.\\!\\;\\?\\'\\:\"]+";
    String inputColumn;
    String outputColumn;

    public TokenizerMapper(String inputColumn, String outputColumn) {
        this.inputColumn = inputColumn;
        this.outputColumn = outputColumn;
    }

    @Override
    public void apply(Record inputRecord, Collector collector) {
        String text = (String) inputRecord.get(inputColumn);

        Arrays.stream(text.split(SPLIT_PATTERN))
                .map(token -> inputRecord
                        .copyColumnsExcept(Collections.singleton(inputColumn))
                        .set(outputColumn, token))
                .forEach(collector::collect);

    }
}
