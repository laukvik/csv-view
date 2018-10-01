package no.laukvik.csvview.column;

import no.laukvik.csv.columns.Column;

public interface ColumnListener {

    /**
     * Fires when a selection is done
     * @param column
     */
    void columnSelected(Column column);
    void columnChanged(Column column);
    void columnVisibilityChanged(Column column,boolean isVisible);

}
