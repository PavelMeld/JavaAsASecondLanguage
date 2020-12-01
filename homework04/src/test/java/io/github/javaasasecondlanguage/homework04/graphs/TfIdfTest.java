package io.github.javaasasecondlanguage.homework04.graphs;

import io.github.javaasasecondlanguage.homework04.GraphPartBuilder;
import io.github.javaasasecondlanguage.homework04.Record;
import io.github.javaasasecondlanguage.homework04.nodes.ProcNode;
import io.github.javaasasecondlanguage.homework04.utils.ListDumper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.javaasasecondlanguage.homework04.utils.AssertionUtils.assertRecordsEqual;
import static io.github.javaasasecondlanguage.homework04.utils.TestUtils.convertToRecords;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TfIdfTest {
    private ProcNode inputNode;
    private ProcNode outputNode;

    @BeforeEach
    void setUp() {
        var graph = TfIdf.createGraph();

        assertNotNull(graph.getInputNodes());
        assertNotNull(graph.getInputNodes());

        assertEquals(1, graph.getInputNodes().size());
        assertEquals(1, graph.getOutputNodes().size());

        inputNode = graph.getInputNodes().get(0);
        outputNode = graph.getOutputNodes().get(0);
    }

    @Test
    void tfidfGeneralTest() {
        var listDumper = new ListDumper();
        GraphPartBuilder
                .startFrom(outputNode)
                .map(listDumper);

        for (var record : inputRecords) {
            inputNode.push(record, 0);
        }

        inputNode.push(Record.terminalRecord(), 0);

        List<Record> actualRecords = listDumper.getRecords();

        assertRecordsEqual(expectedRecords, actualRecords);
    }

    private static final List<Record> inputRecords = convertToRecords(
            new String[]{"docId", "Text"},
            new Object[][]{
                    {1, "some few words count"},
                    {1, "some few words"},
                    {1, "some few"},
                    {1, "some "},
                    {2, "count some few words"},
                    {2, "count some few"},
                    {2, "count some"},
                    {2, "count"},
            }
    );

    private static final List<Record> expectedRecords = convertToRecords(
            new String[]{"docId", "Word", "Idf"},
            new Object[][]{
                    {1, "count", 1.0 / 10},
                    {1, "few", 3.0 / 10},
                    {1, "some", 4.0 / 10},
                    {1, "words", 2.0 / 10},
                    {2, "count", 4.0 / 10},
                    {2, "few", 2.0 / 10},
                    {2, "some", 3.0 / 10},
                    {2, "words", 1.0 / 10},
            }
    );


}
