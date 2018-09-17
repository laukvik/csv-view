package no.laukvik.csvview.table;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.laukvik.csv.CSV;
import no.laukvik.csvview.pivot.PivotFilter;
import no.laukvik.csvview.pivot.PivotSelection;
import no.laukvik.csvview.pivot.PivotType;
import no.laukvik.csvview.query.QueryModel;

import java.io.File;
import java.util.List;

public class ResultsTableTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final CSV csv = new CSV(getFile("presidents.csv"));
        System.out.println(csv.getColumnCount() + " x " + csv.getRowCount());
        PivotSelection selection = new PivotSelection();
        PivotFilter filter = new PivotFilter(true, "Whig", "Whig", 1, csv.getColumn("Party"), PivotType.STRING, null);
        selection.add(filter);
        QueryModel model = new QueryModel();
        model.setSelection(selection);
        ResultsTable table = new ResultsTable();
        table.setCSV(csv);
        table.setQuery(model.buildQuery(csv));
        table.setColumns(List.of(csv.getColumn("President"), csv.getColumn("Party"),csv.getColumn("Home State")));
        final Scene scene = new Scene(table, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private static File getFile(String filename) {
        ClassLoader classLoader = ResultsTableTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

}