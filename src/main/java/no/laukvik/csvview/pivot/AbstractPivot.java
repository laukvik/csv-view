package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;

import java.util.List;

public abstract class AbstractPivot<T> {

    private T column;
    private PivotFilterListener listener;
    final static String EMPTY = "";

    AbstractPivot(final T column, final PivotFilterListener listener) {
        this.column = column;
        this.listener = listener;
    }

    public PivotFilterListener getListener() {
        return listener;
    }

    public T getColumn() {
        return column;
    }

    public abstract List<PivotTab> getTabs();

    public abstract void loadCSV(final CSV csv);

}
