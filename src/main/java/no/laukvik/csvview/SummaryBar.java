package no.laukvik.csvview;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToolBar;

import java.nio.charset.Charset;
import java.util.ResourceBundle;

import static no.laukvik.csvview.utils.Formatter.formatSeparator;
import static no.laukvik.csvview.utils.Formatter.toKb;

class SummaryBar extends ToolBar {

    private final ResourceBundle bundle;
    /**
     * The Label for showing the amount of rows.
     */
    private Label rowsLabel;
    /**
     * The Label for showing the amount of columns.
     */
    private Label colsLabel;
    /**
     * The Label for showing the encoding.
     */
    private Label encodingLabel;
    /**
     * The Label for showing the file size.
     */
    private Label sizeLabel;
    /**
     * The Label for showing the separator.
     */
    private Label separatorLabel;
    /**
     * The Label for showing the file type.
     */
    private Label fileTypeLabel;
    /**
     * The Label for showing the progress bar.
     */
    private ProgressBar progressBar;

    /**
     * The width of the progressbar.
     */
    private static final int PROGRESS_BAR_WIDTH = 200;

    public SummaryBar(final ResourceBundle bundle) {
        this.bundle = bundle;
        Label rows = new Label(bundle.getString("summarybar.status.rows"));

        rows.setDisable(true);
        rowsLabel = new Label("-");
        Label cols = new Label(bundle.getString("summarybar.status.columns"));
        cols.setDisable(true);
        colsLabel = new Label("-");
        Label encoding = new Label(bundle.getString("summarybar.status.encoding"));
        encoding.setDisable(true);
        encodingLabel = new Label("-");
        Label size = new Label(bundle.getString("summarybar.status.size"));
        size.setDisable(true);
        sizeLabel = new Label("-");
        Label separator = new Label(bundle.getString("summarybar.status.separator"));
        separator.setDisable(true);
        separatorLabel = new Label("-");
        Label filetype = new Label(bundle.getString("summarybar.status.filetype"));
        filetype.setDisable(true);
        fileTypeLabel = new Label("");
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        progressBar.setPrefWidth(PROGRESS_BAR_WIDTH);
        getItems().addAll(rows, rowsLabel, cols, colsLabel, size, sizeLabel, encoding, encodingLabel,
                separator, separatorLabel, filetype, fileTypeLabel, progressBar);
    }

    public void setRowCount(int rows){
        rowsLabel.setText(rows + "");
    }

    public void setColumnCount(int rows){
        colsLabel.setText(rows + "");
    }

    public void setCharset(Charset charset){
        if (charset == null) {
            encodingLabel.setText(bundle.getString("metadata.encoding.na"));
        } else {
            encodingLabel.setText(charset.name());
        }
    }

    public void setFileSize(long fileSize) {
        sizeLabel.setText(toKb(fileSize));
    }

    public void setSeparator(Character separatorChar) {
        separatorLabel.setText(formatSeparator(separatorChar));
    }

    public void setFileType(String formatFiletype) {
        fileTypeLabel.setText(formatFiletype);
    }

    public void setProgressBar(boolean isVisible){
        progressBar.setVisible(isVisible);
    }
}
