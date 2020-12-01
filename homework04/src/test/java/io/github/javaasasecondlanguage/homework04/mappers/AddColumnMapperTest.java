package io.github.javaasasecondlanguage.homework04.mappers;

import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.ops.mappers.AddColumnMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static io.github.javaasasecondlanguage.homework04.utils.AssertionUtils.assertRecordsEqual;
import static io.github.javaasasecondlanguage.homework04.utils.TestUtils.applyMapperToAllRecords;
import static io.github.javaasasecondlanguage.homework04.utils.TestUtils.convertToRecords;
import static java.lang.String.format;

public class AddColumnMapperTest {

    @Test
    void general() {
        Function<Record, Object> lambda = record -> format("%s %s",
                record.get("Name"), record.get("Surname"));
        AddColumnMapper mapper = new AddColumnMapper("Full name", lambda);

        List<Record> actualRecords = applyMapperToAllRecords(mapper, inputRecords);
        assertRecordsEqual(expectedRecords, actualRecords);
    }

    private static final List<Record> inputRecords = convertToRecords(
            new String[]{"Id", "Name", "Surname"},
            new Object[][]{
                    {13, "Roboute", "Guilliman"},
                    {3, "Fulgrim", "Phoenician"},
                    {6, "Leman", "Russ"},
            }
    );

    private static final List<Record> expectedRecords = convertToRecords(
            new String[]{"Id", "Name", "Surname", "Full name"},
            new Object[][]{
                    {13, "Roboute", "Guilliman", "Roboute Guilliman"},
                    {3, "Fulgrim", "Phoenician", "Fulgrim Phoenician"},
                    {6, "Leman", "Russ", "Leman Russ"},
            }
    );
}
