package no.laukvik.csvview.table;

import no.laukvik.csv.Row;

import java.util.List;

public interface ResultsTableListener {

    void rowsSelected(List<Row> rows);
}
