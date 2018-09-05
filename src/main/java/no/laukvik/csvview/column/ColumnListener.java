package no.laukvik.csvview.column;

import no.laukvik.csv.columns.Column;

import java.util.List;

public interface ColumnListener {

    void columnFocused(Column column);
    void columnChanged(Column column);
    void columnSelectionChanged(List<Column> columns);
    void columnVisibilityChanged(Column column,boolean isVisible);

}
