package no.laukvik.csvview.table;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.laukvik.csv.CSV;

import java.io.File;

public class ResultsTableTest extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final CSV csv = new CSV(getFile("presidents.csv"));
//        controller.openEmpty();
        System.out.println(csv.getColumnCount() + " x " + csv.getRowCount());

        ResultsTable table = new ResultsTable();
        table.setCSV(csv);

        final Scene scene = new Scene(table, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private static File getFile(String filename) {
        ClassLoader classLoader = ResultsTableTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }


}