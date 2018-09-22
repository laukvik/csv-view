package no.laukvik.csvview.column;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import no.laukvik.csv.columns.Column;

/**
 * Represents a column that is observable for JavaFX components.
 *
 */
@SuppressWarnings("WeakerAccess")
public final class ObservableColumn {

    /**
     * The property for visible.
     */
    private final SimpleBooleanProperty visible;
    /**
     * The property for name.
     */
    private final SimpleStringProperty name;
    private final SimpleStringProperty columnType;
    /** The Column its for. */
    private final Column column;

    /**
     * Represents a column that is observable.
     *
     * @param column the column
     */
    public ObservableColumn(final Column column, final ColumnController controller) {
        visible = new SimpleBooleanProperty(true);
        name = new SimpleStringProperty(column.getName());
        columnType = new SimpleStringProperty(column.getClass().getSimpleName().replaceAll("Column", ""));
        this.column = column;
        visible.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> observable,
                                final Boolean oldValue,
                                final Boolean newValue) {
                controller.fireVisibilityChanged(column, newValue);
            }
        });
    }

    /**
     * Returns the visibleProperty.
     * @return the visibleProperty
     */
    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }

    /**
     * Returns the nameProperty.
     * @return the nameProperty
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getColumnType() {
        return columnType.get();
    }

    public SimpleStringProperty columnTypeProperty() {
        return columnType;
    }

    /**
     * Returns the name.
     * @return the name
     */
    public String getName() {
        return column.getName();
    }

    public Column getColumn() {
        return column;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name.setValue(name);
        column.setName(name);
    }

}
