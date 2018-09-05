package no.laukvik.csvview.utils;

import no.laukvik.csv.CSV;

import java.io.File;

public class Formatter {

    /**
     * The value of one kilobyte.
     */
    private static final int KILOBYTE = 1024;

    /**
     * Returns the formatted file type.
     *
     * @param file the file
     * @return the formatted file type
     */
    public static String formatFiletype(final File file) {
        if (file == null) {
            return "";
        } else {
            String filename = file.getName();
            if (filename.endsWith(".txt")) {
                return "Text";
            } else if (filename.endsWith(".properties")) {
                return "ResourceBundle";
            } else {
                return "CSV";
            }
        }
    }

    /**
     * Returns the text version of the separator character.
     *
     * @param character the separator
     * @return the text version
     */
    public static String formatSeparator(final Character character) {
        if (character == null) {
            return "";
        } else {
            return getSeparatorString(character);
        }
    }

    /**
     * Formats the file name as string.
     *
     * @param file the file
     * @return the formatted name
     */
    public static String formatFilename(final File file) {
        if (file == null) {
            return "";
        } else {
            return file.getName();
        }
    }

    /**
     * Returns the value formatted in Kilobytes.
     *
     * @param value the value
     * @return the formatted value in Kilobytes
     */
    public static String toKb(final long value) {
        if (value < KILOBYTE) {
            return (value) + " bytes";
        } else {
            return (value / KILOBYTE) + " Kb";
        }
    }

    /**
     * Returns the String representation of the separator character.
     *
     * @param separator the separator
     * @return the String representation
     */
    public static String getSeparatorString(final char separator) {
        switch (separator) {
            case CSV.COMMA:
                return "COMMA";
            case CSV.TAB:
                return "TAB";
            case CSV.PIPE:
                return "PIPE";
            case CSV.SEMICOLON:
                return "SEMICOLON";
            default:
                return "COMMA";
        }
    }

    /**
     * Returns the char representation of the separator character.
     *
     * @param separator the separator
     * @return the String representation
     */
    public static Character getSeparatorCharByString(final String separator) {
        switch (separator) {
            case "COMMA":
                return CSV.COMMA;
            case "TAB":
                return CSV.TAB;
            case "PIPE":
                return CSV.PIPE;
            case "SEMICOLON":
                return CSV.SEMICOLON;
            default:
                return null;
        }
    }

}
