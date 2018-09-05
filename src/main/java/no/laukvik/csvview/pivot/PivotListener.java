package no.laukvik.csvview.pivot;

import javafx.scene.control.Tab;

public interface PivotListener {

    void pivotFocused(PivotFilter filter);
    void pivotChanged(PivotFilter filter, PivotSelection selection);
    void pivotTabChanged(Tab tab);

}
