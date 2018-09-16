package no.laukvik.csvview.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import no.laukvik.csv.io.BOM;
import no.laukvik.csvview.utils.FileChooserExtensions;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static no.laukvik.csvview.utils.Builder.listSupportedSeparatorChars;
import static no.laukvik.csvview.utils.Formatter.getSeparatorCharByString;
import static no.laukvik.csvview.utils.Formatter.getSeparatorString;

public class FileOpenAdvancedDialog extends Dialog {

    /**
     * The padding in dialog.
     */
    private static final int DIALOG_PADDING = 10;
    final ChoiceBox separatorBox;
    final ChoiceBox charsetBox;
    private ResourceBundle bundle;

    public FileOpenAdvancedDialog(ResourceBundle bundle) {
        super();
        this.bundle = bundle;
        separatorBox = new ChoiceBox();
        charsetBox = new ChoiceBox();
        setTitle(bundle.getString("app.title"));
        setHeaderText(bundle.getString("dialog.file.open"));

        final GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));
        gridpane.setHgap(DIALOG_PADDING);
        gridpane.setVgap(DIALOG_PADDING);

        final Label sepLabel = new Label(bundle.getString("metadata.separator"));
        gridpane.add(sepLabel, 0, 1);

        List<String> items = new ArrayList<>();
        items.add(bundle.getString("metadata.separator.autodetect"));
        for (char c : listSupportedSeparatorChars()) {
            items.add(getSeparatorString(c));
        }
        separatorBox.getItems().addAll(items);
        gridpane.add(separatorBox, 1, 1);
        separatorBox.getSelectionModel().select(0);

        final Label charsetLabel = new Label(bundle.getString("metadata.encoding"));
        gridpane.add(charsetLabel, 0, 2);

        charsetBox.getItems().add(bundle.getString("metadata.encoding.autodetect"));
        for (BOM b : BOM.values()) {
            charsetBox.getItems().add(b.name());
        }
        for (String key : Charset.availableCharsets().keySet()) {
            charsetBox.getItems().add(key);
        }
        charsetBox.getSelectionModel().select(0);
        gridpane.add(charsetBox, 1, 2);
        getDialogPane().setContent(gridpane);

        // Set the button types.
        ButtonType okButtonType = ButtonType.OK;
        ButtonType cancelButtonType = ButtonType.CANCEL;
        getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
    }

    public Character getSeparator(){
        if (separatorBox.getSelectionModel().getSelectedIndex() == 0) {
            return null;
        } else {
            return getSeparatorCharByString((String) separatorBox.getSelectionModel().getSelectedItem());
        }
    }

    public Charset getCharset(){
        if (charsetBox.getSelectionModel().getSelectedIndex() == 0) {
            return null;
        } else {
            return Charset.forName((String) charsetBox.getSelectionModel().getSelectedItem());
        }
    }

    public File getSelectedFile(Stage stage){
        showAndWait();
        ButtonType resultButtonType = (ButtonType) getResult();
        if (resultButtonType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(bundle.getString("dialog.file.open"));
            fileChooser.setSelectedExtensionFilter(FileChooserExtensions.buildCSV());
            return fileChooser.showOpenDialog(stage);
        }
        return null;
    }

}
