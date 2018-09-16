package no.laukvik.csvview.pivot;

public interface PivotFilterListener {

    void pivotFilterChanged(PivotFilter filter);
    void pivotFilterFocused(PivotFilter filter);

}
