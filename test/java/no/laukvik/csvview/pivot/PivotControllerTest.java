package no.laukvik.csvview.pivot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import no.laukvik.csv.CSV;
import no.laukvik.csvview.column.ColumnSelectorTest;

import java.io.File;

public class PivotControllerTest extends Application implements PivotListener {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        PivotController controller = new PivotController();
        controller.addPivotListener(this);
        final CSV csv = new CSV(getFile("presidents.csv"));
        controller.setColumn(csv.getColumn(5));
        final Scene scene = new Scene(controller, 300, 500);
        stage.setScene(scene);
        stage.show();
    }

    private static File getFile(String filename) {
        ClassLoader classLoader = ColumnSelectorTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }


    @Override
    public void pivotFocused(PivotFilter filter) {
        System.out.println("pivotFocused: " + filter);
    }

    @Override
    public void pivotChanged(PivotFilter filter, PivotSelection selection) {
        System.out.println("pivotChanged: " + filter + " " + selection.getFilters().size());
    }

    @Override
    public void pivotTabChanged(Tab tab) {
        System.out.println("pivotTabChanged: " + tab);
    }
}