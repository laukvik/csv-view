package no.laukvik.csvview.content;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.laukvik.csv.CSV;
import no.laukvik.csvview.table.ResultsTable;

import java.io.File;

public class ContentControllerTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ContentController controller = new ContentController();
        final CSV csv = new CSV(getFile("presidents.csv"));
//        controller.openEmpty();
        System.out.println(csv.getColumnCount() + " x " + csv.getRowCount());

        ResultsTable table = new ResultsTable();
        table.setCSV(csv);
        controller.openResults(table);


        final Scene scene = new Scene(controller, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private static File getFile(String filename) {
        ClassLoader classLoader = ContentControllerTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }


}