package no.laukvik.csvview.table;

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
import no.laukvik.csvview.utils.Builder;
import no.laukvik.csvview.Main;

import java.util.*;

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
    private List<DataTableListener> listeners;
    private List<Column> visibleColumns;

    /**
     * Creates a new empty instance.
     */
    public ResultsTable() {
        super();
        listeners = new ArrayList<>();
        visibleColumns = new ArrayList<>();
        ResourceBundle bundle = Builder.getBundle();
        setEditable(true);
        Label l = new Label(bundle.getString("table.results.empty"));
        setPlaceholder(l);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        clearRows();
    }

    private void fireRowSelected(Row row){

    }

    public void addDataTableListener(DataTableListener listener){
        listeners.add(listener);
    }

    /**
     * Removes all rows.
     */
    public final void clearRows() {
        setItems(FXCollections.observableArrayList());
        getColumns().clear();
    }

    /**
     * Notifies that the rows where changed and needs rebuilding.
     *
     * @param csv  the csv instance
     * @param main the main instance
     */
    public final void columnsChanged(final CSV csv, final Main main) {
        buildColumns(csv);
        buildRows(csv);
    }

    public void clearAll(){
        visibleColumns.clear();
        clearRows();
    }

    public void setCSV(CSV csv) {
        clearRows();
        buildColumns(csv);
        buildRows(csv);
    }

    private TableColumn<ObservableRow, String> buildColumn(final Column c, int colX){
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

    public void buildColumns(CSV csv){
        getColumns().clear();
        visibleColumns
                .stream()
                .forEach(column -> getColumns().add(buildColumn(column, visibleColumns.indexOf(column))));
//        for (int x = 0; x < csv.getColumnCount(); x++) {
//            final Column c = csv.getColumn(x);
//            final TableColumn<ObservableRow, String> tc = buildColumn(c, x);
//            getColumns().add(tc);
//        }
    }

    public void buildRows(CSV csv){
        setItems(FXCollections.observableArrayList());
        csv.findRows().stream().forEach(row -> getItems().add(new ObservableRow(row, csv)));
    }

    public void setColumns(List<Column> selectedColumns) {
        this.visibleColumns = selectedColumns;
    }


//    /**
//     * Builds an ObservableList of all Columns found in the MetaData.
//     *
//     * @param csv the csv
//     * @return ObservableList of all Columns
//     */
//    public static ObservableList<ObservableColumn> createAllObservableList(final CSV csv) {
//        List<ObservableColumn> list = new ArrayList<>();
//        for (int x = 0; x < csv.getColumnCount(); x++) {
//            Column c = csv.getColumn(x);
//            list.add(new ObservableColumn(c));
//        }
//        return FXCollections.observableArrayList(list);
//    }

//    /**
//     * Builds an ObservableList of FrequencyDistribution.
//     *
//     * @param columnIndex the columnIndex
//     * @param csv         the CSV
//     * @param main        the Main
//     * @return ObservableList of FrequencyDistribution
//     */
//    public static ObservableList<PivotFilter> createFrequencyDistributionObservableList(
//            final int columnIndex, final CSV csv, final Main main) {
//        List<PivotFilter> list = new ArrayList<>();
////        FrequencyDistribution d = csv.buildFrequencyDistribution(columnIndex);
////        Column c = csv.getColumn(columnIndex);
////        for (String key : d.getKeys()) {
////            boolean selected = main.getQueryModel().isSelected(c, key);
////            list.addValueMatcher(new PivotFilter(selected, key, key, d.getCount(key), c, main));
////        }
//        return FXCollections.observableArrayList(list);
//    }

//    /**
//     * Creates the Rows.
//     *
//     * @param resultsTable the TableView
//     * @param csv              the csv file
//     * @param main             the Main
//     */
//    public static void createResultsRows(final ResultsTable resultsTable,
//                                         final CSV csv,
//                                         final Main main) {
////        resultsTable.getItems().clearAll();
////        QueryModel query = main.getQuery();
////        if (query != null) {
////            List<Row> rows = csv.findRowsByQuery(query);
////            for (int y = 0; y < rows.size(); y++) {
////                resultsTable.getItems().addValueMatcher(new ObservableRow(rows.get(y), csv, main));
////            }
////        } else {
////            for (int y = 0; y < csv.getRowCount(); y++) {
////                resultsTable.getItems().addValueMatcher(new ObservableRow(csv.getRow(y), csv, main));
////            }
////        }
////        csv.findRows().stream().forEach(row -> resultsTable.getItems().add(new ObservableRow(row, csv)));
//        resultsTable.buildRows(csv);
//    }

//    /**
//     * Removes all columns in the resultsTable and populates it with all columns found in the MetaData.
//     *
//     * @param resultsTable the resultsTable
//     * @param csv the csv
//     */
//    public static void createResultsColumns(final ResultsTable resultsTable, final CSV csv) {
//        resultsTable.getColumns().clearAll();
////        for (int x = 0; x < csv.getColumnCount(); x++) {
////            final Column c = csv.getColumn(x);
////                final TableColumn<ObservableRow, String> tc = new TableColumn<>(c.getName());
////                tc.setCellFactory(TextFieldTableCell.forTableColumn());
////
//////                resultsTable.getColumns().addValueMatcher(tc);
////                final int colX = x;
////                tc.setCellValueFactory(
////                        new Callback<TableColumn.CellDataFeatures<ObservableRow, String>, ObservableValue<String>>() {
////                            @Override
////                            public ObservableValue<String> call(
////                                    final TableColumn.CellDataFeatures<ObservableRow, String> param) {
////                                return param.getValue().getValue(colX);
////                            }
////                        }
////                );
////                tc.setCellFactory(TextFieldTableCell.forTableColumn());
////                tc.setMinWidth(COLUMN_WIDTH_DEFAULT);
////        }
//        resultsTable.buildColumns(csv);
//    }

}
