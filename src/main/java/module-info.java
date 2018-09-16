module no.laukvik.csvview {
    requires javafx.controls;
    requires javafx.web;
    requires java.desktop;
    requires no.laukvik.csv;
    exports no.laukvik.csvview;
    exports no.laukvik.csvview.chart;
    exports no.laukvik.csvview.column;
    exports no.laukvik.csvview.content;
    exports no.laukvik.csvview.dialogs;
    exports no.laukvik.csvview.pivot;
}