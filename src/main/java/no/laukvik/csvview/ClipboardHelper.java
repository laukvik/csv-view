package no.laukvik.csvview;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.DateColumn;
import no.laukvik.csv.io.CsvReaderException;

import java.io.IOException;
import java.util.List;

public class ClipboardHelper {

    /**
     * Copy multiple rows to clipboard
     * @param rows
     * @param csv
     */
    public static void copyRowsToClipboard(List<Row> rows, CSV csv) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(convertRowsToString(rows, csv));
        clipboard.setContent(content);
    }

    /**
     * Paste rows from clipboard into CSV at the specified rowIndex
     * @param rowIndex
     * @param csv
     */
    public static void pasteClipboard(int rowIndex, final CSV csv){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        String pasted = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
        try {
            convertStringToRows(csv, pasted, rowIndex);
        } catch (CsvReaderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void build(final StringBuilder b, Row row, CSV csv){
        for (int x = 0; x < csv.getColumnCount(); x++) {
            if (x > 0) {
                b.append(CSV.TAB);
            }
            Column c = csv.getColumn(x);
            if (!row.isNull(c)) {
                if (c instanceof DateColumn){
                    DateColumn dc = (DateColumn) c;
                    b.append(dc.asString(row.get(dc)));
                } else {
                    b.append(row.getRaw(c));
                }
            }
        }
        b.append(CSV.LINEFEED);
    }

    public static String convertRowsToString(List<Row> rows, CSV csv){
        StringBuilder b = new StringBuilder();
        rows.forEach(row -> build(b, row, csv));
        return b.toString();
    }

    public static void convertStringToRows(final CSV csv, final String value, final int rowIndex) throws CsvReaderException, IOException {
        String [] rows = value.split("\n");
        int y = rowIndex;
        for (String raw : rows) {
            String [] cols = raw.split("\t");
            Row r = csv.addRow(y);
            int x = 0;
            for (String c: cols) {
                r.setRaw(csv.getColumn(x), c);
                x++;
            }
            y++;
        }

    }
}
