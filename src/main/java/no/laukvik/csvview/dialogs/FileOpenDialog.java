package no.laukvik.csvview.dialogs;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import no.laukvik.csvview.utils.FileChooserExtensions;

import java.io.File;
import java.util.ResourceBundle;

public class FileOpenDialog {

    final FileChooser fileChooser;

    public FileOpenDialog(ResourceBundle bundle) {
        super();
        fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("fileopendialog"));
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(FileChooserExtensions.buildCompatible());
    }

    public File getFile(Stage stage) {
        return fileChooser.showOpenDialog(stage);
    }
}
