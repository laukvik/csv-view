package no.laukvik.csvview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomePaneTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        WelcomePane pane = new WelcomePane("v1.0");
        final Scene scene = new Scene(pane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

}