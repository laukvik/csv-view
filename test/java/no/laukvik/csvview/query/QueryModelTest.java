package no.laukvik.csvview.query;

import no.laukvik.csv.Row;
import no.laukvik.csv.query.Query;
import no.laukvik.csvview.pivot.PivotFilter;
import no.laukvik.csvview.pivot.PivotType;
import org.junit.Test;
import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.StringColumn;
import no.laukvik.csv.io.CsvReaderException;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Morten Laukvik
 */
public class QueryModelTest {

    static File getResource(String filename) {
        ClassLoader classLoader = QueryModelTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    CSV findCSV() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        return csv;
    }

    @Test
    public void shouldAddSelection() throws CsvReaderException {
        CSV csv = findCSV();
        StringColumn party = (StringColumn) csv.getColumn("Party");
//        StringColumn state = (StringColumn) csv.getColumn("Home State");
        QueryModel model = new QueryModel();
        model.setColumns(List.of(party));
        model.getSelection().add(new PivotFilter(true, "Democratic", "Democratic", 15, party, PivotType.STRING, null));
        model.getSelection().add(new PivotFilter(true, "Whig", "Whig", 4, party, PivotType.STRING, null));
        Query query = model.buildQuery(csv);
        List<Row> rows = csv.findRowsByQuery(query);
        assertEquals(19, rows.size());
    }

}