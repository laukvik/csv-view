package no.laukvik.csvview.pivot;

import javafx.scene.control.Tab;
import no.laukvik.csv.columns.Column;
import no.laukvik.csvview.utils.Builder;

public class PivotTab extends Tab {

    private PivotType pivotType;
    private PivotTableView tableView;
    private Column column;
    private PivotFilterListener listener;

    public PivotTab(PivotType pivotType, Column column, PivotFilterListener listener) {
        super(Builder.getBundle().getString("pivot." + pivotType.name().toLowerCase().replace("_", ".")));
        this.pivotType = pivotType;
        this.column = column;
        this.tableView = new PivotTableView(pivotType, listener);
        setContent(this.tableView);
    }

    public void clear(){
        tableView.getItems().clear();
    }

    public void addPivotFilter(PivotFilter filter){
        tableView.getItems().add(filter);
    }

    public void removePivotFilter(PivotFilter filter){
        tableView.getItems().remove(filter);
    }
}
