package no.laukvik.csvview.pivot;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import no.laukvik.csv.columns.Column;

/**
 * A JavaFX data model for FrequencyDistribution.
 *
 * @author Morten Laukvik
 */
public class PivotFilter implements ChangeListener<Boolean>{

    /**
     * The selection property.
     */
    private final SimpleBooleanProperty selected;
    /**
     * The value property.
     */
    private final SimpleObjectProperty value;
    /**
     * The label for the value.
     */
    private final SimpleStringProperty label;
    /**
     * The count property.
     */
    private final SimpleIntegerProperty count;
    private final PivotFilterListener listener;
    private Column column;
    private PivotType pivotType;

    /**
     * Builds a new instance with the specified values.
     *
     * @param selected whether the value is selected
     * @param label the label
     * @param value the value
     * @param count how many times its used
     * @param column the column
     *
     */
    public PivotFilter(final boolean selected,
                       final String label,
                       final Object value,
                       final int count,
                       final Column column,
                       final PivotType pivotType,
                       final PivotFilterListener listener) {
        this.selected = new SimpleBooleanProperty(selected);
        this.value = new SimpleObjectProperty(value);
        this.label = new SimpleStringProperty(label);
        this.count = new SimpleIntegerProperty(count);
        this.column = column;
        this.pivotType = pivotType;
        this.listener = listener;
        this.selected.addListener(this);
    }

    @Override
    public String toString() {
        return labelProperty().get() + " : " + selected.get() + " " + column.getName() + " " + pivotType;
    }

    public Object getValue() {
        return value.get();
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        listener.pivotFilterChanged(this);
    }

    public PivotType getPivotType() {
        return pivotType;
    }

    public Column getColumn() {
        return column;
    }

    /**
     * Returns true if it is selected.
     *
     * @return the true if selected
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Returns the selected Property.
     * @return the selected Property.
     */
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }


    /**
     * Returns the value Property.
     * @return the value Property.
     */
    public SimpleObjectProperty valueProperty() {
        return value;
    }

    /**
     * Returns the label Property.
     * @return the label Property.
     */
    public SimpleStringProperty labelProperty() {
        return label;
    }

    /**
     * Returns the count.
     *
     * @return the count
     */
    public int getCount() {
        return count.get();
    }

    /**
     * Returns the count Property.
     * @return the count Property.
     */
    public SimpleIntegerProperty countProperty() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PivotFilter that = (PivotFilter) o;

        Object thisValue = value.get();
        Object thatValue = that.value.get();

        if (thisValue == null && thatValue != null){
            return false;
        }
        if (thisValue != null && thatValue == null){
            return false;
        }

        if (!thisValue.equals(thatValue)) return false;
        if (!column.equals(that.column)) return false;
        return pivotType == that.pivotType;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + column.hashCode();
        result = 31 * result + pivotType.hashCode();
        return result;
    }
}
