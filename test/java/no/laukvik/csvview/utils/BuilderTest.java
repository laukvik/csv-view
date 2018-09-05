package no.laukvik.csvview.utils;

import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class BuilderTest {

    @Test
    public void getIcon(){
        assertNotNull(Builder.getIcon());
    }

    @Test
    public void getLibrary(){
        assertNotNull(Builder.getLibrary());
    }

    @Test
    public void getApplication(){
        String appname = "no.laukvik.csvview";
        assertNotNull(Builder.getApplication(appname));
    }

    @Test
    public void getBundle_Locale() {
        ResourceBundle bundle = Builder.getBundle(new Locale("no"));
        assertNotNull(bundle);
    }

    @Test
    public void getBundle_no_Locale() {
        ResourceBundle bundle = Builder.getBundle();
        assertNotNull(bundle);
    }

    @Test
    public void toKb() {
        assertEquals("120 Kb", Formatter.toKb(123456));
        assertEquals("1023 bytes", Formatter.toKb(1023));
        assertEquals("1 Kb", Formatter.toKb(1024));
    }
}