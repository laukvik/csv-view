package org.laukvik.csvview;

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.io.CsvReaderException;

import java.io.File;

/**
 * @author Morten Laukvik
 */
public class QueryModelTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = QueryModelTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    public CSV findCSV() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("metadata.csv"));
        return csv;
    }

    @Test
    public void shouldAddSelection() throws CsvReaderException {
        CSV csv = findCSV();
        StringColumn homeState = (StringColumn) csv.getColumn("Home State");
        StringColumn party = (StringColumn) csv.getColumn("Party");
        QueryModel model = new QueryModel(csv, null);
        model.addSelection(homeState, "New York");
        Assert.assertNotNull(model.findSelectionByColumn(homeState));
        model.removeSelection(homeState, "New York");
        Assert.assertNull(model.findSelectionByColumn(homeState));
        model.addSelection(homeState, "New York");
        model.addSelection(homeState, "California");
    }

}