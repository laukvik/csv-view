package no.laukvik.csvview.pivot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.Column;
import no.laukvik.csvview.column.ColumnController;
import no.laukvik.csvview.column.ColumnSelectorTest;

import java.io.File;

import static org.junit.Assert.*;

public class PivotSelectionControllerTest extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    private static File getFile(String filename) {
        ClassLoader classLoader = ColumnSelectorTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Override
    public void start(Stage stage) throws Exception {
        PivotSelectionController controller = new PivotSelectionController();
        final CSV csv = new CSV(getFile("presidents.csv"));
        Column party = csv.getColumn("Party");

        PivotSelection selection = new PivotSelection();
        selection.add(get("Whig", party,12));
        selection.add(get("Democratic", party,  12));


//        controller.setPivotSelec/tion(selection);

        final Scene scene = new Scene(controller, 300, 500);
        stage.setScene(scene);
        stage.show();
    }

    public PivotFilter get(String label, Column column,  int count){
        return new PivotFilter(true, label, label, count, column, null, null );
    }

}