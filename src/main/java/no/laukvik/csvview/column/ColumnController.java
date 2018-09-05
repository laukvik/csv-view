package no.laukvik.csvview.column;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.Column;
import no.laukvik.csvview.utils.Builder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JavaFX table component with the available columns in CSV.
 *
 * @author Morten Laukvik
 */
public class ColumnController extends TableView<ObservableColumn> {

    /**
     * The width of the checkbox column.
     */
    private static final int CHECKBOX_WIDTH = 32;

    /**
     *  The width ratio of checkbox.
     */
    private static final float CHECKBOX_RATIO = 0.2f;
    /**
     * The width ratio of column.
     */
    private static final float COLUMN_RATIO = 1 - CHECKBOX_RATIO;
    private CSV csv;
    private List<ColumnListener> columnListeners;

    /**
     * A TableView representing a Column in the CSV.
     */
    public ColumnController() {
        super();
        this.csv = new CSV();
        columnListeners = new ArrayList<>();
        ResourceBundle bundle = Builder.getBundle();
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setEditable(true);
        final TableColumn<ObservableColumn, Boolean> checkboxColumn = new TableColumn<>("");
        checkboxColumn.setMinWidth(CHECKBOX_WIDTH);
        checkboxColumn.setMaxWidth(CHECKBOX_WIDTH);
        checkboxColumn.setEditable(true);
        checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("visible"));
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));
        final TableColumn columnNameColumn = new TableColumn(bundle.getString("table.columns"));
        columnNameColumn.setCellValueFactory(new PropertyValueFactory<ObservableColumn, String>("name"));
        columnNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        columnNameColumn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<ObservableColumn, String>>() {
                @Override
                public void handle(final TableColumn.CellEditEvent<ObservableColumn, String> t) {
                    String value = t.getNewValue();
                    int rowIndex = getSelectionModel().getSelectedIndex();
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(value);
                    Column column = csv.getColumn(rowIndex);
                    fireColumnChanged(column);
                }
            }
        );

        getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(final Change<? extends Integer> c) {
                int rowIndex = getSelectionModel().getSelectedIndex();
                if (rowIndex > -1){
                    fireFocusChanged(csv.getColumn(rowIndex));
                }
            }
        });

        getColumns().addAll(checkboxColumn, columnNameColumn);
        /* Resizing */
        setPlaceholder(new Label(bundle.getString("table.columns.empty")));
        checkboxColumn.prefWidthProperty().bind(widthProperty().multiply(CHECKBOX_RATIO));
        columnNameColumn.prefWidthProperty().bind(widthProperty().multiply(COLUMN_RATIO));

    }

    public List<Column> getSelectedColumns(){
        List<Column> columns = new ArrayList<>();
        columns.addAll(getItems()
                .stream()
                .filter(observableColumn -> observableColumn.visibleProperty().get())
                .map(observableColumn -> observableColumn.getColumn())
                .collect(Collectors.toList()));
        return columns;
    }

    public void addColumnListener(ColumnListener listener){
        columnListeners.add(listener);
    }

    private void fireColumnChanged(Column column){
        columnListeners.stream().forEach( l -> l.columnChanged(column));
    }

    public void fireVisibilityChanged(Column column, boolean visible){
        columnListeners.stream().forEach( l -> l.columnVisibilityChanged(column, visible));
    }

    private void fireFocusChanged(Column column){
        columnListeners.stream().forEach( l -> l.columnFocused(column));
    }

    public void setCSV(CSV csv) {
        this.csv = csv;
        getItems().clear();
        getItems().addAll(buildColumns(csv));
    }

    /**
     * Builds an ObservableList of all Columns found in the MetaData.
     *
     * @param csv the csv
     * @return ObservableList of all Columns
     */
    public ObservableList<ObservableColumn> buildColumns(final CSV csv) {
        List<ObservableColumn> list = new ArrayList<>();
        csv.getColumns().stream().forEach(column -> list.add(new ObservableColumn(column, this)));
        return FXCollections.observableArrayList(list);
    }
}
