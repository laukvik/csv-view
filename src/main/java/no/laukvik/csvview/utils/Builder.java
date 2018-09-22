package no.laukvik.csvview.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A helper class for the JavaFX application.
 *
 * @author Morten Laukvik
 */
final public class Builder {

    /**
     * The width of the logo in pixels.
     */
    private static final int LOGO_WIDTH = 48;
    /**
     * The height of the logo in pixels.
     */
    private static final int LOGO_HEIGHT = 48;


    /**
     * Hides the constructor.
     */
    private Builder() {
    }

    /**
     * Returns the ResourceBundle for the application.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("messages", Locale.getDefault());
    }

    public static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("messages", locale);
    }


    /**
     * Returns the Image symobolizeing the application.
     *
     * @return the Image
     */
    public static ImageView getImage() {
        return new ImageView(new Image("feather.png", LOGO_WIDTH, LOGO_HEIGHT, true, true));
    }

    /**
     * Returns the dimension of the screen by specifying the percentage in float values.
     * <br>
     * <pre>
     * Example: 1 = 100% and 0.5f = 50%.
     * </pre>
     *
     * @param w the width in percent
     * @param h the height in percent
     * @return the Dimension
     */
    public static java.awt.Dimension getPercentSize(final float w, final float h) {
        java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Float width = size.width * w;
        Float height = size.height * h;
        return new java.awt.Dimension(width.intValue(), height.intValue());
    }

    /**
     * Returns true if operating system is Mac.
     *
     * @return true if Mac
     */
    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static Image getIcon(){
        return new Image("/feather.png");
    }

    /**
     * Copies the selected row to Clipboard.
     * <p>
     *
     * @param rowIndex the row index
     * @param csv      the csv
     * @return the string representation of the row
     */
    public static String toClipboardString(final int rowIndex, final CSV csv) {
        StringBuilder b = new StringBuilder();
        for (int x = 0; x < csv.getColumnCount(); x++) {
            if (x > 0) {
                b.append(CSV.TAB);
            }
//            StringColumn sc = (StringColumn) csv.getColumn(x);
            Column sc = csv.getColumn(x);

            b.append(csv.getRow(rowIndex).getRaw(sc));
        }
        return b.toString();
    }

    /**
     * Returns the Library folder for the user.
     *
     * @return the folder called Library in the users home folder
     */
    public static File getLibrary() {
        if (isWindows()){
//            return "C:\\Users\\Morten\\AppData\\Local\\Ableton"
            return new File(System.getProperty("user.home") + "\\AppData\\Local\\");
        }
        return new File(System.getProperty("user.home"), "Library");
    }


    public static File getApplication(String appname) {
        File file = new File(getLibrary(), appname);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * Returns an array of the supported separator characters.
     *
     * @return separator characters
     */
    public static char[] listSupportedSeparatorChars() {
        return new char[]{CSV.COMMA, CSV.SEMICOLON, CSV.PIPE, CSV.TAB};
    }
}
