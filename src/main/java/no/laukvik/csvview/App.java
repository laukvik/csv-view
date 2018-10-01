package no.laukvik.csvview;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.*;
import no.laukvik.csv.io.CsvReaderException;
import no.laukvik.csv.io.IncorrectColumnsException;
import no.laukvik.csvview.chart.ChartBuilder;
import no.laukvik.csvview.column.ColumnController;
import no.laukvik.csvview.column.ColumnListener;
import no.laukvik.csvview.content.ContentController;
import no.laukvik.csvview.content.ViewMode;
import no.laukvik.csvview.dialogs.FileOpenAdvancedDialog;
import no.laukvik.csvview.dialogs.FileOpenDialog;
import no.laukvik.csvview.pivot.*;
import no.laukvik.csvview.query.QueryModel;
import no.laukvik.csvview.table.ObservableRow;
import no.laukvik.csvview.table.ResultsTable;
import no.laukvik.csvview.utils.Builder;
import no.laukvik.csvview.utils.FileChooserExtensions;
import no.laukvik.csvview.utils.RecentFiles;

import java.io.File;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;
import static no.laukvik.csvview.utils.Builder.getIcon;
import static no.laukvik.csvview.utils.Formatter.*;

/**
 * The JavaFX desktop application for opening and displaying the data sets.
 *
 * ColumnController | Results
 * PivotTable     |
 *
 *
 *
 * frequencyDistributionTableView | ContentController
 *
 * @author Morten Laukvik
 */
public final class App extends Application implements ColumnListener, PivotListener {

    /**
     * The vertical divider position.
     */
    private static final float DIVIDER_POSITION_V = 0.2f;
    /**
     * The ResourceBundle for this application.
     */
    private final ResourceBundle bundle = Builder.getBundle();
    /**
     * The CSV model.
     */
    private CSV csv;
    /**
     * The JavaFX stage.
     */
    private Stage stage;
    /**
     * The TableView for column selection.
     */
    private ColumnController columnController;

    /**
     * The pivotController
     */
    private PivotController pivotController;
    /**
     * The TableView for results columns.
     */
    private ResultsTable resultsTable;
    /**
     * The RecentFiles manager class.
     */
    private RecentFiles recentFiles;
    /**
     * The MenuBar and all it.
     */
    private AppMenu menuBar;
    /**
     * The ScrollPane to display results and other components in.
     */
    private ContentController contentController;
    /**
     * The view mode.
     */
    private ViewMode viewMode = ViewMode.Results;

    /**
     * The summaryBar.
     */
    private SummaryBar summaryBar;

    private BorderPane root;
    private SplitPane mainSplit;
    private PivotSelectionController pivotSelectionController;
    /**
     * Can be run from commandline.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }



    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the stage
     */
    public final void start(final Stage primaryStage) {
        this.stage = primaryStage;
        stage.getIcons().add(getIcon());
        stage.setTitle(bundle.getString("app.title"));
        columnController = new ColumnController();
        resultsTable = new ResultsTable();
        menuBar = new AppMenu(this);
        final ScrollPane columnsScroll = new ScrollPane(columnController);
        columnsScroll.setFitToHeight(true);
        columnsScroll.setFitToWidth(true);

        pivotSelectionController = new PivotSelectionController();

        pivotController = new PivotController();
        pivotController.addPivotListener(this);
        pivotController.addPivotListener(pivotSelectionController);

        columnController.addColumnListener(this);
        contentController = new ContentController();
        contentController.openResults(resultsTable);

        final SplitPane tableSplit = new SplitPane(columnsScroll, pivotController, pivotSelectionController);
        tableSplit.setOrientation(Orientation.VERTICAL);
        tableSplit.setDividerPositions(0.2, 0.9, 0.1);
        mainSplit = new SplitPane(tableSplit, contentController);
        mainSplit.setDividerPositions(DIVIDER_POSITION_V);

        SplitPane.setResizableWithParent(tableSplit, Boolean.FALSE);

        final VBox topContainer = new VBox();

        topContainer.getChildren().add(menuBar);
        summaryBar = new SummaryBar(bundle);
        root = new BorderPane();
        root.setTop(topContainer);
        root.setCenter(mainSplit);
        root.setBottom(summaryBar);

        final java.awt.Dimension percent = Builder.getPercentSize(0.8f, 0.7f);
        final Scene scene = new Scene(root, percent.getWidth(), percent.getHeight());
        stage.setScene(scene);
        recentFiles = new RecentFiles(RecentFiles.getConfigurationFile());
        menuBar.buildRecentList(recentFiles);

        resultsTable.addResultsTableListener(menuBar);
        columnController.addColumnListener(menuBar);

        handleNewFile();
        showWelcomeScreen(true);
        stage.show();
    }

    /**
     * Sets the selected column by it index.
     *
     * @param selectedColumnIndex the selectedColumnIndex
     */
    private void setSelectedColumnIndex(final int selectedColumnIndex) {
        if (selectedColumnIndex > -1) {
            pivotController.setColumn(csv.getColumn(selectedColumnIndex));
        }
    }

    /**
     * Handles opening files.
     */
    public final void handleFileOpen() {
        final FileOpenDialog dialog = new FileOpenDialog(bundle);
        final File selectedFile = dialog.getFile(this.stage);
        if (selectedFile != null) {
            loadFile(selectedFile);
        }
    }

    /**
     * Loads a file without dialogs.
     *
     * @param file the file to open
     */
    public final void loadFile(final File file) {
        if (file != null) {
            loadFile(file, null, null);
        }
    }

    /**
     * Handles opening file with options.
     */
    public final void handleOpenFileWithOptions() {
        FileOpenAdvancedDialog dialog = new FileOpenAdvancedDialog(bundle);
        File file = dialog.getSelectedFile(this.stage);
        if (file != null) {
            loadFile(file, dialog.getSeparator(), dialog.getCharset());
        }
    }

    /**
     * Clears all existing data in the model.
     */
    public void handleNewFile() {
        showWelcomeScreen(false);
        csv = new CSV();
        columnController.setItems(observableArrayList());
        pivotController.setColumn(null);
        resultsTable.clearAll();
        contentController.openEmpty();
        menuBar.setDefault();
        updateToolbar();
    }

    public String getVersion(){
        return "v1.0";
    }

    public void showWelcomeScreen(boolean visible){
        if (visible){
            root.setCenter(new WelcomePane(getVersion()));
        } else {
            root.setCenter(mainSplit);
        }
    }

    /**
     * Loads a CSV file using the specific separator and charset.
     *
     * @param file          the file to load
     * @param separatorChar the separator char to use
     * @param charset       the charset to use
     */
    public void loadFile(final File file, final Character separatorChar, final Charset charset) {
        handleNewFile();
        csv.setAutoDetectCharset(true);
        csv.setAutoDetectQuote(true);
        csv.setAutoDetectSeparator(true);
        try {
            if (charset == null && separatorChar == null) {
                csv.readFile(file);
            } else if (charset != null) {
                csv.setCharset(charset);
                csv.setSeparator(separatorChar);
                csv.readFile(file);
            } else {
                csv.setSeparator(separatorChar);
                csv.readFile(file);
            }
            if (csv.getFile() != null) {
                recentFiles.open(file);
                menuBar.buildRecentList(recentFiles);
            }
            resultsTable.clearAll();
            resultsTable.setColumns(csv.getColumns());
            resultsTable.setCSV(csv);
            contentController.openResults(resultsTable);
            viewMode = ViewMode.Results;
            menuBar.setDefault();
            showWelcomeScreen(false);
            updateAll();
        } catch (CsvReaderException e) {
            if (e.getCause() instanceof IncorrectColumnsException){
                IncorrectColumnsException ice = (IncorrectColumnsException) e.getCause();
                error(bundle.getString("load.file.load.formaterror"), MessageFormat.format(bundle.getString("load.file.load.incorrect.columns"), ice.getRequired(), ice.getFound(), ice.getRowIndex() ), file);
            } else {
                error(bundle.getString("load.file.load.failed"), e.getCause().getMessage(), file);
            }
        }
    }

    /**
     * Updates the toolbar with details.
     */
    private void updateToolbar() {
        summaryBar.setRowCount(csv.getRowCount());
        summaryBar.setColumnCount(csv.getColumnCount());
        summaryBar.setCharset(csv.getCharset());
        summaryBar.setFileSize(csv.getFile() == null ? 0 : csv.getFile().length());
        summaryBar.setSeparator(csv.getSeparatorChar());
        summaryBar.setFileType(formatFiletype(csv.getFile()));
        if (csv.getFile() == null) {
            stage.setTitle(bundle.getString("app.file.untitled") + " - " + bundle.getString("app.title"));
        } else {
            stage.setTitle(formatFilename(csv.getFile()) + " - " + bundle.getString("app.title"));
        }
    }

    private void updateMenubar(){
//        menuBar.setImportEnabled(csv.getColumnCount() > 0);
//        menuBar.setExportEnabled(csv.getColumnCount() > 0);
//        menuBar.setInsertRowEnabled(csv.getColumnCount() > 0);
    }

    /**
     * Shows a dialog box with the error message.
     *
     * @param message the error message
     */
    private void alert(final String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Shows a dialog box with the error message.
     *
     */
    private void error(String title, String message, final File file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public final void handleDeleteRowsAction(){
        ObservableList<ObservableRow> items = resultsTable.getSelectionModel().getSelectedItems();
        int index = resultsTable.getSelectionModel().getFocusedIndex();
        for (ObservableRow or : items){
            int rowIndex = csv.indexOf(or.getRow());
            csv.removeRow(rowIndex);
        }
        resultsTable.setCSV(csv);
        if (items.size() == 1){
            resultsTable.getSelectionModel().clearAndSelect(index);
            resultsTable.getSelectionModel().focus(index);
        } else {
            resultsTable.getSelectionModel().clearSelection();
        }
    }

    /**
     * Handles the delete action.
     */
    public final void handleDeleteColumnAction() {
        int index = columnController.getSelectionModel().getSelectedIndex();
        handleDeleteColumn(index);
    }

    /**
     * Handles the deletion of a column.
     *
     * @param columnIndex the index of the column
     */
    private void handleDeleteColumn(final int columnIndex) {
        MessageFormat format = new MessageFormat(bundle.getString("dialog.deletecolumn.confirm"));
        Object[] messageArguments = {
                csv.getColumn(columnIndex).getName()
        };
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("app.title"));
        alert.setHeaderText(bundle.getString("dialog.deletecolumn"));
        alert.setContentText(format.format(messageArguments));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            deleteColumn(columnIndex);
        }
    }

    /**
     * Deletes a column.
     *
     * @param columnIndex the index of the column
     */
    private void deleteColumn(final int columnIndex) {
        Column c = csv.getColumn(columnIndex);
        csv.removeColumn(c);
        pivotController.removeSelectionForColumn(c);
        pivotSelectionController.setSelection(pivotController.getSelection());
        columnController.setCSV(csv);
        if (columnIndex == csv.getColumnCount()){
            columnController.setSelectedIndex(csv.getColumnCount() - 1);
            pivotController.setColumn(csv.getColumn(csv.getColumnCount() - 1));
        } else {
            columnController.setSelectedIndex(columnIndex);
            pivotController.setColumn(csv.getColumn(columnIndex));
        }
        resultsTable.setCSV(csv);
    }

    private void updateAll(){
        updateToolbar();
        summaryBar.setProgressBar(false);
        columnController.setCSV(csv);
        setSelectedColumnIndex(0);
    }

    /**
     * Updates the rows.
     */
    private void updateRows() {
        resultsTable.buildRows(csv);
    }

    /**
     * Handles printing action.
     */
    public final void handlePrintAction() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText(bundle.getString("dialog.print"));
        dialog.setGraphic(Builder.getImage());

        final ChoiceBox printerChoiceBox = new ChoiceBox();
        for (Printer p : Printer.getAllPrinters()) {
            printerChoiceBox.getItems().addAll(p);
        }

        if (Printer.getAllPrinters().isEmpty()) {
            alert(bundle.getString("dialog.print.printers.empty"));
        } else {
            dialog.getDialogPane().setContent(printerChoiceBox);
            dialog.showAndWait();

            final Printer printer = (Printer) printerChoiceBox.getSelectionModel().getSelectedItem();
            if (printer != null) {
                PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
                if (printerJob.showPrintDialog(stage)) {
                    printerJob.printPage(resultsTable);
                    alert(bundle.getString("dialog.print.finished"));
                }
            }
        }
    }

    /**
     * Handles new column action.
     */
    public final void handleNewColumnAction() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(bundle.getString("app.title"));
        dialog.setHeaderText(bundle.getString("dialog.newcolumn"));
        dialog.setContentText(bundle.getString("dialog.newcolumn.columnname"));
        dialog.setGraphic(Builder.getImage());
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            csv.addColumn(new StringColumn(result.get()));
            columnController.setCSV(csv);
            resultsTable.setCSV(csv);
        }
    }

    /**
     * Handles new row action.
     */
    public final void handleNewRowAction() {
        int rowIndex = resultsTable.getSelectionModel().getSelectedIndex();
        if (rowIndex == -1) {
            csv.addRow();
            resultsTable.setCSV(csv);
            resultsTable.getSelectionModel().select(0);
        } else {
            csv.addRow(rowIndex);
            resultsTable.setCSV(csv);
            resultsTable.getSelectionModel().select(rowIndex);
        }
    }

    /**
     * Handles copy action.
     */
    public final void handleCopyRowsAction() {
        resultsTable.copyToClipboard();
    }

    /**
     * Handles paste action.
     */
    public final void handlePasteRowsAction() {
        resultsTable.pasteFromClipboard();
    }

    /**
     * Handles cut action.
     */
    public final void handleCutRowsAction() {
        resultsTable.cutToClipboard();
    }

    /**
     * Handels move up action.
     */
    public final void handleMoveUpAction() {
        Node owner = stage.getScene().getFocusOwner();
        if (owner == resultsTable) {
            int rowIndex = resultsTable.getSelectionModel().getSelectedIndex();
            if (rowIndex > 0) {
                moveRow(rowIndex, rowIndex - 1);
            }
        } else if (owner == columnController) {
            int rowIndex = columnController.getSelectionModel().getSelectedIndex();
            if (rowIndex > 0) {
                moveColumn(rowIndex, rowIndex - 1);
            }
        }
    }

    /**
     * Handles move down action.
     */
    public final void handleMoveDownAction() {
        Node owner = stage.getScene().getFocusOwner();
        if (owner == resultsTable) {
            int rowIndex = resultsTable.getSelectionModel().getSelectedIndex();
            if (rowIndex < csv.getRowCount() - 1) {
                moveRow(rowIndex, rowIndex + 1);
            }
        } else if (owner == columnController) {
            int rowIndex = columnController.getSelectionModel().getSelectedIndex();
            if (rowIndex < columnController.getItems().size() - 1) {
                moveColumn(rowIndex, rowIndex + 1);
            }
        }
    }

    /**
     * Moves the row to a new index.
     *
     * @param index   the row to move
     * @param toIndex the new index
     */
    private void moveRow(final int index, final int toIndex) {
        csv.moveRow(index, toIndex);
        resultsTable.getSelectionModel().select(toIndex);
    }

    /**
     * Changes the sort order of a column.
     *
     * @param fromIndex the old index
     * @param toIndex   the new index
     */
    private void moveColumn(final int fromIndex, final int toIndex) {
        csv.moveColumn(fromIndex, toIndex);
        Collections.swap(columnController.getItems(), fromIndex, toIndex);
    }

    public void handleImport() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.import"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildCSV());
        final File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                csv.importFile(selectedFile);
                resultsTable.setCSV(csv);
            } catch (Exception e) {
                e.printStackTrace();
                alert(bundle.getString("file.import.csv.failed"));
            }
        }
    }

    public void handleExportCsvAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.csv"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildCSV());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeFile(selectedFile, resultsTable.getQuery());
            } catch (Exception e) {
                alert(bundle.getString("file.export.csv.failed"));
            }
        }
    }

    /**
     * Handles export to JSON action.
     */
    public final void handleExportJsonAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.json"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildJsonFile());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeJSON(selectedFile, resultsTable.getQuery());
            } catch (Exception e) {
                alert(bundle.getString("file.export.json.failed"));
            }
        }
    }

    /**
     * Handles export to XML action.
     */
    public final void handleExportXmlAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.xml"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildXmlFile());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeXML(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("file.export.xml.failed"));
            }
        }
    }

    /**
     * Handles export to HTML action.
     */
    public final void handleExportHtmlAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.export.html"));
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildHtmlFile());
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeHtml(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("file.export.html.failed"));
            }
        }
    }

    /**
     * Handles save as action.
     */
    public final void handleSaveAsAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("dialog.file.saveas"));
        final File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                csv.writeFile(selectedFile);
            } catch (Exception e) {
                alert(bundle.getString("dialog.file.saveas.failed"));
            }
        }
    }

    /**
     * Handles new query action.
     */
    public final void handleNewQuery() {
        pivotController.clearSelections();
    }

    public void handleTabChanged() {
        if (viewMode == ViewMode.Results) {
            contentController.openResults(resultsTable);
        } else if (viewMode == ViewMode.Chart) {
            int index = pivotController.getSelectionModel().getSelectedIndex();
            if (index > -1){
                final PieChart chart = ChartBuilder.buildPieChart(pivotController.getPivotTableView(index));
                contentController.openPieChart(chart);
            }
        } else if (viewMode == ViewMode.Maps) {
            PivotFilter ofd = pivotController.getFocusedPivotFilter();
            contentController.openGoogleMaps(ofd);
        } else if (viewMode == ViewMode.Preview) {
            PivotFilter ofd = pivotController.getFocusedPivotFilter();
            contentController.openPreview(ofd, csv);
        } else if (viewMode == ViewMode.Search) {
            PivotFilter ofd = pivotController.getFocusedPivotFilter();
            contentController.openGoogleSearch(ofd);
        } else if (viewMode == ViewMode.Wikipedia) {
            PivotFilter ofd = pivotController.getFocusedPivotFilter();
            contentController.openWikipedia(ofd);
        }
    }

    /**
     * Handles chart action.
     */
    public void handleViewChartAction() {
        viewMode = ViewMode.Chart;
        handleTabChanged();
    }

    /**
     * Handles view results action.
     */
    public void handleViewResultsAction() {
        viewMode = ViewMode.Results;
        handleTabChanged();
    }

    /**
     * Handles preview action.
     */
    public void handleViewPreviewAction() {
        viewMode = ViewMode.Preview;
        handleTabChanged();
    }

    /**
     * Handles Wikipedia action.
     */
    public void handleViewWikipediaAction() {
        viewMode = ViewMode.Wikipedia;
        handleTabChanged();
    }

    /**
     * Handles Maps action.
     */
    public void handleViewGoogleMapsAction() {
        viewMode = ViewMode.Maps;
        handleTabChanged();
    }

    /**
     * Handles Search action.
     */
    public void handleViewGoogleSearchAction() {
        viewMode = ViewMode.Search;
        handleTabChanged();
    }

    /* -------------------------------------- Column Controller events */


    @Override
    public void columnChanged(Column column) {
//        setSelectedColumnIndex(csv.indexOf(column));
    }

    @Override
    public void columnSelected(Column column) {
        setSelectedColumnIndex(csv.indexOf(column));
    }

    @Override
    public void columnVisibilityChanged(Column column, boolean isVisible) {
        resultsTable.setColumns(columnController.getSelectedColumns());
        resultsTable.setCSV(csv);
    }

    /* -------------------------------------- Pivot Controller events */

    @Override
    public void pivotFocused(PivotFilter filter) {
        handleTabChanged();
    }

    @Override
    public void pivotChanged(PivotFilter filter, PivotSelection selection) {
        QueryModel model = new QueryModel();
        model.setSelection(selection);
        if (selection.isEmpty()){
            resultsTable.clearAll();
            resultsTable.setQuery(null);
            resultsTable.setCSV(csv);
        } else {
            resultsTable.setQuery(model.buildQuery(csv));
        }
        handleTabChanged();
    }

    @Override
    public void pivotTabChanged(Tab tab) {
        handleTabChanged();
    }
}
