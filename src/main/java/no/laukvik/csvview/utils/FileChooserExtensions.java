package no.laukvik.csvview.utils;

import javafx.stage.FileChooser;

/**
 * JavaFX helper methods.
 *
 */
public final class FileChooserExtensions {

    /**
     * All supported FileChooser Extensions.
     */
    public static final FileChooser.ExtensionFilter[] SUPPORTED_EXTENSIONS = {
            buildCSV(), buildTSV(), buildResourceBundle(), buildTextFile(), buildJsonFile(), buildHtmlFile(), buildXmlFile()
    };

    /**
     * Hide constructor in this builder class.
     */
    private FileChooserExtensions() {
    }

    public static FileChooser.ExtensionFilter buildCompatible() {
        return new FileChooser.ExtensionFilter("All formats", "*.csv", "*.tsv");
    }

    /**
     * Extension for CSV files.
     *
     * @return filter
     */
    public static FileChooser.ExtensionFilter buildTSV() {
        return new FileChooser.ExtensionFilter("Tab separated files", "*.tsv");
    }

    /**
     * Extension for CSV files.
     *
     * @return filter
     */
    public static FileChooser.ExtensionFilter buildCSV() {
        return new FileChooser.ExtensionFilter("Comma separated files", "*.csv");
    }

    /**
     * Extension for ResourceBundle files.
     * @return filter
     */
    public static FileChooser.ExtensionFilter buildResourceBundle() {
        return new FileChooser.ExtensionFilter("Resource bundles", "*.properties");
    }

    /**
     * Extension for TXT files.
     * @return filter
     */
    private static FileChooser.ExtensionFilter buildTextFile() {
        return new FileChooser.ExtensionFilter("Text files", "*.txt");
    }

    /**
     * Extension for JSON files.
     * @return filter
     */
    public static FileChooser.ExtensionFilter buildJsonFile() {
        return new FileChooser.ExtensionFilter("JSON files", "*.json");
    }

    /**
     * Extension for HTML files.
     * @return filter
     */
    public static FileChooser.ExtensionFilter buildHtmlFile() {
        return new FileChooser.ExtensionFilter("HTML files", "*.html");
    }

    /**
     * Extension for XML files.
     * @return filter
     */
    public static FileChooser.ExtensionFilter buildXmlFile() {
        return new FileChooser.ExtensionFilter("XML files", "*.xml");
    }

}
