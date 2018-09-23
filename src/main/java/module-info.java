module no.laukvik.csvview {
    requires java.base;
    requires no.laukvik.csv;
    requires transitive javafx.controls;
    requires javafx.web;
    requires java.desktop;
    exports no.laukvik.csvview;
    exports no.laukvik.csvview.chart;
    exports no.laukvik.csvview.column;
    exports no.laukvik.csvview.content;
    exports no.laukvik.csvview.dialogs;
    exports no.laukvik.csvview.pivot;
}