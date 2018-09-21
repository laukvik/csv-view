package no.laukvik.csvview.content;

import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import no.laukvik.csv.CSV;
import no.laukvik.csvview.pivot.PivotFilter;
import no.laukvik.csvview.table.ResultsTable;
import no.laukvik.csvview.utils.Builder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ContentController extends ScrollPane {

    private final ResourceBundle bundle;

    public ContentController() {
        super();
        this.bundle = Builder.getBundle();
        setFitToHeight(true);
        setFitToWidth(true);
    }

    public void openEmpty(){
        setContent(getPreviewEmptyNode());
    }

    public void openPieChart(PieChart chart){
        setContent(chart);
    }

    public void openResults(ResultsTable resultsTable) {
        setContent(resultsTable);
    }

    public Node getPreviewEmptyNode() {
        BorderPane pane = new BorderPane();
        pane.centerProperty().setValue(new Label(bundle.getString("view.preview.empty")));
        return pane;
    }

    public void openPreview(PivotFilter filter, CSV csv) {
        if (filter == null) {
            openEmpty();
        } else {
            String filename = filter.labelProperty().getValue();
            if (filename == null || filename.trim().isEmpty()) {
                setContent(new Label(bundle.getString("view.preview.empty")));
            } else if (filename.startsWith("http")) {
                WebView v = new WebView();
                WebEngine webEngine = v.getEngine();
                setContent(v);
                webEngine.load(filename);

            } else if (filename.endsWith(".gif") || filename.endsWith(".jpg") || filename.endsWith(".png")) {
                if (filename.indexOf('\\') > -1) {
                    filename = filename.replace('\\', '/');
                }
                Path p = Paths.get(csv.getFile().getParent(), filename);
                File f = p.toFile();
                if (f.exists()) {
                    setContent(new ImageView(new Image(f.toURI().toString())));
                }
            } else {
                setContent(getPreviewEmptyNode());
            }
        }
    }

    public void openWikipedia(PivotFilter ofd) {
        if (ofd == null) {
            openEmpty();
        } else {
            String value = ofd.labelProperty().getValue();
            if (value != null && !value.isEmpty()) {
                WebView v = new WebView();
                WebEngine webEngine = v.getEngine();
                setContent(v);
                webEngine.load("https://en.wikipedia.org/wiki/" + value);
            } else {
                setContent(getPreviewEmptyNode());
            }
        }
    }

    public void openGoogleMaps(PivotFilter ofd) {
        if (ofd == null) {
            openEmpty();
        } else {
            String value = ofd.labelProperty().getValue();
            if (value != null && !value.isEmpty()) {
                WebView v = new WebView();
                WebEngine webEngine = v.getEngine();
                setContent(v);
                webEngine.load("https://www.google.com/maps?q=" + value);
            } else {
                setContent(getPreviewEmptyNode());
            }
        }
    }

    public void openGoogleSearch(PivotFilter ofd) {
        if (ofd == null) {
            openEmpty();
        } else {
            String value = ofd.labelProperty().getValue();
            if (value != null && !value.isEmpty()) {
                WebView v = new WebView();
                WebEngine webEngine = v.getEngine();
                setContent(v);
                webEngine.load("https://www.google.no/?q=" + value);
            } else {
                setContent(getPreviewEmptyNode());
            }
        }
    }
}
