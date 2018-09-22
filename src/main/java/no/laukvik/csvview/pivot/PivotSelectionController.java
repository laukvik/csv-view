package no.laukvik.csvview.pivot;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;

public class PivotSelectionController extends ScrollPane implements PivotListener {

    private FlowPane flow;

    public PivotSelectionController() {
        super();
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setFitToWidth(true);
        setFitToHeight(true);
        flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(4);
        flow.setHgap(4);
        setContent(flow);
    }

    public Button buildbutton(PivotFilter filter){
        Button b = new Button();
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                filter.selectedProperty().set(false);
            }
        });
        String label = filter.labelProperty().getValue();
        b.setText(label);
        b.setTooltip(new Tooltip(filter.getColumn().getName()));
        return b;
    }

    @Override
    public void pivotFocused(PivotFilter filter) {

    }

    @Override
    public void pivotChanged(PivotFilter filter, PivotSelection selection) {
        flow.getChildren().clear();
        selection.getFilters().forEach(f -> flow.getChildren().add(buildbutton(f)));
    }

    @Override
    public void pivotTabChanged(Tab tab) {

    }

}
