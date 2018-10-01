package no.laukvik.csvview.table;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;
import no.laukvik.csv.query.Query;
import no.laukvik.csvview.ClipboardHelper;
import no.laukvik.csvview.utils.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * A JavaFX component showing all columns in a CSV.
 *
 * @author Morten Laukvik
 */
public class ResultsTable extends TableView<ObservableRow> {

    /**
     * The default width of columns in the result sets.
     */
    private static final int COLUMN_WIDTH_DEFAULT = 100;
    private List<Column> visibleColumns;
    private Query query;
    private CSV csv;
    private List<ResultsTableListener> listeners;

    /**
     * Creates a new empty instance.
     */
    public ResultsTable() {
        super();
        this.query = null;
        this.csv = null;
        visibleColumns = new ArrayList<>();
        listeners = new ArrayList<>();
        ResourceBundle bundle = Builder.getBundle();
        setEditable(true);
        Label l = new Label(bundle.getString("resultstable.empty"));
        setPlaceholder(l);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        clearAll();
        getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                fireSelectedRows(true);
            }
        });
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    fireSelectedRows(newValue);
                }
            }
        });
    }

    public void addResultsTableListener(ResultsTableListener listener){
        this.listeners.add(listener);
    }

    private void fireSelectedRows(boolean hasFocus){
        List<Row> rows = getSelectedRows();
        this.listeners.forEach(listener -> listener.rowsSelected(hasFocus ? rows : List.of()));
    }

    public void setCSV(CSV csv) {
        this.csv = csv;
        clearRows();
        buildColumns(csv);
        buildRows(csv);
    }

    public void setQuery(Query query) {
        this.query = query;
        rebuild();
    }

    public Query getQuery() {
        return query;
    }

    public void setColumns(List<Column> selectedColumns) {
        this.visibleColumns = selectedColumns;
        this.rebuild();
    }

    /**
     * Removes all rows.
     */
    private void clearRows() {
        setItems(FXCollections.observableArrayList());
        getColumns().clear();
    }

    public void clearAll() {
        visibleColumns = null;
        clearRows();
    }

    private void rebuild() {
        buildColumns(csv);
        buildRows(csv);
    }

    private void buildColumns(CSV csv) {
        getColumns().clear();
        if (csv == null) {
            return;
        }
        if (emptyColumns()) {
            csv.getColumns()
                    .stream()
                    .forEach(column -> getColumns().add(buildColumn(column, csv.indexOf(column))));
        } else {
            visibleColumns
                    .stream()
                    .forEach(column -> getColumns().add(buildColumn(column, csv.indexOf(column))));
        }
    }

    private boolean emptyColumns(){
        return visibleColumns == null || visibleColumns.isEmpty();
    }

    public void buildRows(CSV csv) {
        setItems(FXCollections.observableArrayList());
        if (csv == null) {
            return;
        }
        if (query == null) {
            csv.findRows().stream().forEach(row -> getItems().add(new ObservableRow(row, csv)));
        } else {
            csv.findRowsByQuery(query).stream().forEach(row -> getItems().add(new ObservableRow(row, csv)));
        }
    }

    private TableColumn<ObservableRow, String> buildColumn(final Column c, int colX) {
        final TableColumn<ObservableRow, String> tc = new TableColumn<>(c.getName());
        tc.setCellFactory(TextFieldTableCell.forTableColumn());
        tc.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ObservableRow, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            final TableColumn.CellDataFeatures<ObservableRow, String> param) {
                        return param.getValue().getValue(colX);
                    }
                }
        );
        tc.setCellFactory(TextFieldTableCell.forTableColumn());
        tc.setMinWidth(COLUMN_WIDTH_DEFAULT);
        return tc;
    }

    private List<Row> getSelectedRows(){
        return getSelectionModel().getSelectedItems().stream().map(observableRow -> observableRow.getRow()).collect(Collectors.toList());
    }

    public void copyToClipboard(){
        List<Row> rows = getSelectedRows();
        if (!rows.isEmpty()) {
            ClipboardHelper.copyRowsToClipboard(rows, csv);
        }
    }

    public void pasteFromClipboard(){
        int rowIndex = getSelectionModel().getSelectedIndex();
        if (rowIndex > -1) {
            ClipboardHelper.pasteClipboard(rowIndex, csv);
            rebuild();
            getSelectionModel().select(rowIndex);
        }
    }

    public void cutToClipboard() {
        List<Row> rows = getSelectedRows();
        ClipboardHelper.copyRowsToClipboard(rows, csv);
        csv.findRows().removeAll(rows);
        rebuild();
    }
}
