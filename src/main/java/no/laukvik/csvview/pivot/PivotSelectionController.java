package no.laukvik.csvview.pivot;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;

public class PivotSelectionController extends ScrollPane implements PivotListener {


    private PivotSelection pivotSelection;
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
//        String label = filter.getColumn().getName() + " - " + filter.labelProperty().getValue();
        String label = filter.labelProperty().getValue();
        b.setText(label);
        return b;
    }

    @Override
    public void pivotFocused(PivotFilter filter) {

    }

    @Override
    public void pivotChanged(PivotFilter filter, PivotSelection selection) {
        this.pivotSelection = selection;
        flow.getChildren().clear();
        pivotSelection.getFilters().forEach(f -> flow.getChildren().add(buildbutton(f)));
    }

    @Override
    public void pivotTabChanged(Tab tab) {

    }
}
