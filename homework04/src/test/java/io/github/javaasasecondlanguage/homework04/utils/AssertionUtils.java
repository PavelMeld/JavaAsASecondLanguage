package io.github.javaasasecondlanguage.homework04.utils;

import io.github.javaasasecondlanguage.homework04.Record;

import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionUtils {


    public static void assertRecordsEqual(List<Record> expectedRecords,
                                          List<Record> actualRecords) {
        for (var recordIndex = 0; recordIndex < expectedRecords.size(); recordIndex++) {
            var actualRecord = actualRecords.get(recordIndex);
            var expectedRecord = expectedRecords.get(recordIndex);
            assertEquals(expectedRecord, actualRecord, format("Record index: %d", recordIndex));
        }

        assertEquals(expectedRecords.size(), actualRecords.size());
    }
}
