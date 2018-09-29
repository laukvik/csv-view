package no.laukvik.csvview;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.io.CsvReaderException;
import no.laukvik.csvview.table.ResultsTableTest;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ClipboardHelperTest {

    private static String row1 = "1\tGeorge Washington\thttp://en.wikipedia.org/wiki/George_Washington\t30/04/1789\t04/03/1797\tIndependent\tGeorgeWashington.jpg\tthmb_GeorgeWashington.jpg\tVirginia";
    private static String row5 = "5\tJames Monroe\thttp://en.wikipedia.org/wiki/James_Monroe\t04/03/1817\t04/03/1825\tDemocratic-Republican\tJamesMonroe.gif\tthmb_JamesMonroe.gif\tVirginia";

    private static File getFile(String filename) {
        ClassLoader classLoader = ResultsTableTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void convertStringToRows() throws CsvReaderException, IOException {
        final CSV csv = new CSV(getFile("presidents.csv"));
        csv.removeRows();
        String value = row1 + "\n" + row5 + "\n";
        ClipboardHelper.convertStringToRows(csv, value, 0);
        assertEquals(2, csv.stream().count());
    }


    @Test
    public void convertRowsToString() throws CsvReaderException {
        final CSV csv = new CSV(getFile("presidents.csv"));
        List<Row> rows = List.of(csv.getRow(0));
        assertEquals(ClipboardHelper.convertRowsToString(rows, csv), row1);
    }

//    @Test
//    public void copyToClipboardByRows() throws CsvReaderException {
//        final CSV csv = new CSV(getFile("presidents.csv"));
//        List<Row> rows = List.of(csv.getRow(0), csv.getRow(5));
//        assertEquals(ClipboardHelper.convertRowsToString(rows, csv), row1 + "\n" + row5);
//    }

}