package no.laukvik.csvview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import no.laukvik.csvview.utils.Builder;

import java.util.ResourceBundle;

class WelcomePane extends BorderPane {

    WelcomePane(String version) {
        super();
        ResourceBundle bundle = Builder.getBundle();
        VBox pane = new VBox();
        pane.setMaxSize(200, 200);
        pane.setMinSize(200, 100);
        pane.setPadding(new Insets(5, 0, 5, 0));
        pane.setAlignment(Pos.BASELINE_CENTER);
        ImageView image = Builder.getImage();
        Label title = new Label(bundle.getString("app.title"));
        title.setFont(Font.font(32));
        title.setCenterShape(true);
        Label description = new Label(bundle.getString("app.title.welcome") + " " + version);
        pane.getChildren().addAll(image, title, description);
        setCenter(pane);
    }
}
