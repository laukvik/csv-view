package no.laukvik.csvview.pivot;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import no.laukvik.csvview.utils.Builder;

import java.util.ResourceBundle;

/**
 * JavaFX table which contains a Frequency distribution table of values and the amount of instance
 * pr item in the list.
 */
public final class PivotTableView extends TableView<PivotFilter> {

    /**
     * The width of the select column.
     */
    private static final int SELECT_WIDTH = 32;
    /**
     * The width of the count column.
     */
    private static final int COUNT_WIDTH = 96;
    /**
     * The ratio of the select column.
     */
    private static final float SELECT_RATIO = 0.1f;
    /**
     * The ratio of the value column.
     */
    private static final float VALUE_RATIO = 0.7f;
    /**
     * The ratio of the count column.
     */
    private static final float COUNT_RATIO = 0.2f;
    /**
     * The minimum width of the count column.
     */
    private static final int MIN_WIDTH = 32;
    /**
     * The maximum width of the count column.
     */
    private static final int MAX_WIDTH = 120;
    private final TableColumn selectUniqueColumn;
    private final TableColumn valueUniqueColumn;
    private final TableColumn countUniqueColumn;
    private final PivotFilterListener listener;
    private final PivotType type;

    /**
     * Creates a new instance.
     */
    public PivotTableView(final PivotType type, final PivotFilterListener listener) {
        super();
        this.listener = listener;
        this.type = type;
        ResourceBundle bundle = Builder.getBundle();
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setEditable(true);
        setPlaceholder(new Label(bundle.getString("pivot.unique.empty")));
        selectUniqueColumn = new TableColumn("");
        selectUniqueColumn.setSortable(false);
        selectUniqueColumn.setMinWidth(SELECT_WIDTH);
        selectUniqueColumn.setMaxWidth(SELECT_WIDTH);
        selectUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<PivotFilter, Boolean>("selected")
        );
        selectUniqueColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectUniqueColumn));
        selectUniqueColumn.setEditable(true);
        getColumns().add(selectUniqueColumn);
        valueUniqueColumn = new TableColumn(bundle.getString("pivot"));
        valueUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<PivotFilter, String>("label")
        );
        getColumns().add(valueUniqueColumn);
        countUniqueColumn = new TableColumn("");
        countUniqueColumn.setCellValueFactory(
                new PropertyValueFactory<PivotFilter, Integer>("count")
        );
        countUniqueColumn.setStyle("-fx-alignment: CENTER_RIGHT");
        countUniqueColumn.setMinWidth(MIN_WIDTH);
        countUniqueColumn.setMaxWidth(MAX_WIDTH);
        countUniqueColumn.setPrefWidth(COUNT_WIDTH);
        getColumns().add(countUniqueColumn);
        selectUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(SELECT_RATIO));
        valueUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(VALUE_RATIO));
        countUniqueColumn.prefWidthProperty().bind(widthProperty().multiply(COUNT_RATIO));

        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PivotFilter>() {
            @Override
            public void changed(ObservableValue<? extends PivotFilter> observable, PivotFilter oldValue, PivotFilter newValue) {
                listener.pivotFilterFocused(newValue);
            }
        });
    }

    public TableColumn getValueColumn() {
        return valueUniqueColumn;
    }

    public TableColumn getCountColumn() {
        return countUniqueColumn;
    }

    public TableColumn getSelectColumn() {
        return selectUniqueColumn;
    }
}
