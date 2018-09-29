package no.laukvik.csvview.pivot;

import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PivotController extends TabPane implements PivotFilterListener {

    private List<PivotListener> listeners;
    private PivotSelection pivotSelection;

    public PivotController() {
        super();
        setSide(Side.BOTTOM);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.listeners = new ArrayList<>();
        pivotSelection = new PivotSelection();
        getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            firePivotTabChanged(newTab);
        });
    }

    public PivotSelection getSelection() {
        return pivotSelection;
    }

    @Override
    public void pivotFilterChanged(PivotFilter filter) {
        if (filter.isSelected()){
            pivotSelection.add(filter);
        } else {
            pivotSelection.remove(filter);
        }
        firePivotChecked(filter);
    }

    @Override
    public void pivotFilterFocused(PivotFilter filter) {
        firePivotFocused(filter);
    }

    public void addPivotListener(final PivotListener listener){
        this.listeners.add(listener);
    }

    private void firePivotChecked(PivotFilter filter){
        this.listeners.stream().forEach(listener -> listener.pivotChanged(filter, pivotSelection));
    }

    private void firePivotFocused(PivotFilter filter){
        this.listeners.stream().forEach(listener -> listener.pivotFocused(filter));
    }

    private void firePivotTabChanged(Tab tab){
        this.listeners.stream().forEach(listener -> listener.pivotTabChanged(tab));
    }

    public void setColumn(Column column){
        getTabs().clear();

        if (column == null){
            return;
        }

        AbstractPivot abstractPivot = null;
        CSV csv = column.getCSV();
        if (column instanceof StringColumn) {
            abstractPivot = new StringPivot((StringColumn) column, this);
            abstractPivot.loadCSV(csv);
        } else if (column instanceof IntegerColumn) {
            abstractPivot = new IntegerPivot((IntegerColumn) column, this);
            abstractPivot.loadCSV(csv);

        } else if (column instanceof DoubleColumn) {
            abstractPivot = new DoublePivot((DoubleColumn) column, this);
            abstractPivot.loadCSV(csv);

        } else if (column instanceof FloatColumn) {
            abstractPivot = new FloatPivot((FloatColumn) column, this);
            abstractPivot.loadCSV(csv);

        } else if (column instanceof BigDecimalColumn) {
            abstractPivot = new BigDecimalPivot((BigDecimalColumn) column, this);
            abstractPivot.loadCSV(csv);

        } else if (column instanceof DateColumn) {
            abstractPivot = new DatePivot((DateColumn) column, this);
            abstractPivot.loadCSV(csv);
        } else if (column instanceof UrlColumn) {
            abstractPivot = new UrlPivot((UrlColumn) column, this);
            abstractPivot.loadCSV(csv);
        }


        if (abstractPivot == null) {
            return;
        }


        List<PivotTab> tabs = abstractPivot.getTabs();
        for (PivotTab t : tabs){
            t.setSelection(pivotSelection);
        }

        // Load all tabs from column into current
        getTabs().addAll(abstractPivot.getTabs());

    }

    public PivotFilter getFocusedPivotFilter() {
        int tabIndex = getSelectionModel().getSelectedIndex();
        if (tabIndex == -1){
            return null;
        }
        PivotTableView tableView = getPivotTableView(tabIndex);
        int rowIndex = tableView.getSelectionModel().getSelectedIndex();
        return rowIndex > -1 ? tableView.getItems().get(rowIndex) : null;
    }

    public PivotTableView getPivotTableView(int tabIndex){
        return (PivotTableView) getTabs().get(tabIndex).getContent();
    }

    public void removeSelectionForColumn(final Column column){
        List<PivotFilter> filters = pivotSelection
                .getFilters()
                .stream()
                .filter(filter -> filter.getColumn() == column).collect(Collectors.toList());
        pivotSelection.getFilters().removeAll(filters);
    }

    public void clearSelections() {
        List<PivotFilter> filters = List.copyOf(pivotSelection.getFilters());
        filters.forEach(filter -> filter.selectedProperty().set(false));
        pivotSelection.clear();
    }
}
