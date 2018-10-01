package no.laukvik.csvview.column;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.Column;

import java.io.File;
import java.util.List;

public class ColumnSelectorTest extends Application implements ColumnListener {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ColumnController controller = new ColumnController();
        final CSV csv = new CSV(getFile("presidents.csv"));
        controller.addColumnListener(this);
        controller.setCSV(csv);
        final Scene scene = new Scene(controller, 300, 500);
        stage.setScene(scene);
        stage.show();
    }

    private static File getFile(String filename) {
        ClassLoader classLoader = ColumnSelectorTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Override
    public void columnSelected(Column column) {
        System.out.println("columnSelected: " + column);
    }

    @Override
    public void columnChanged(Column column) {

    }

    @Override
    public void columnVisibilityChanged(Column column, boolean isVisible) {
        System.out.println("columnVisibilityChanged: " + column + " visible: " + isVisible);
    }
}