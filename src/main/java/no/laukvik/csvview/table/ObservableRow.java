/*
 * Copyright 2015 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.laukvik.csvview.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import no.laukvik.csv.*;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.DateColumn;
import no.laukvik.csv.columns.StringColumn;

import java.util.Date;
import java.util.List;

/**
 * Represents a row in JavaFX.
 */
public class ObservableRow implements javafx.beans.value.ChangeListener<String> {

    /**
     * The values of the column.
     */
    private final List<SimpleStringProperty> items;
    /** The row it represents. */
    private final Row row;
    /**
     * The CSV it belongs to.
     */
    private final CSV csv;

    /**
     * Builds a new ObservableRow from the Row.
     *
     * @param row      the row
     * @param csv the csv instance
     */
    public ObservableRow(final Row row, final CSV csv) {
        this.row = row;
        items = FXCollections.observableArrayList();
        this.csv = csv;
        for (int x = 0; x < csv.getColumnCount(); x++) {
            Column col = csv.getColumn(x);
            if (col instanceof DateColumn){
                DateColumn dc = (DateColumn) col;
                Date d = row.get(dc);
                SimpleStringProperty ssp = new SimpleStringProperty(dc.formatDate(d));
                ssp.addListener(this);
                items.add(ssp);
            } else {
                SimpleStringProperty ssp = new SimpleStringProperty(row.getRaw(col));
                ssp.addListener(this);
                items.add(ssp);
            }
        }
    }

    /**
     * Returns the value.
     *
     * @param columnIndex the column index
     * @return the value property
     */
    public final SimpleStringProperty getValue(final int columnIndex) {
        return items.get(columnIndex);
    }

    /**
     * Returns HashCode.
     *
     * @return the HashCode
     */
    public final int hashCode() {
        int result = 13;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (row != null ? row.hashCode() : 0);
        return result;
    }

    /**
     * Returns whether the Row is equal to another.
     *
     * @param o the other object
     * @return true if equals
     */
    public final boolean equals(final Object o) {
        ObservableRow or = (ObservableRow) o;
        return row.equals(or.row);
    }

    /**
     * Notifies that one of the values was changed.
     *
     * @param observable the observable value
     * @param oldValue   the old value
     * @param newValue   the new value
     */
    public final void changed(final ObservableValue<? extends String> observable,
                              final String oldValue,
                              final String newValue) {
        int columnIndex = items.indexOf(observable);
        int rowIndex = csv.indexOf(row);
        Column column = csv.getColumn(columnIndex);
        if (column instanceof StringColumn) {
            row.set((StringColumn) column, newValue);
        }
//        listener.cellUpdated(columnIndex, rowIndex);
    }

    public Row getRow() {
        return row;
    }
}
