package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.IntegerColumn;
import no.laukvik.csv.statistics.FrequencyDistribution;

import java.util.List;

/**
 *
 *
 *
 */
public class IntegerPivot extends AbstractPivot<IntegerColumn> {

    private PivotTab uniqueTab;

    public IntegerPivot(final IntegerColumn column, final PivotFilterListener listener) {
        super(column, listener);
        uniqueTab = new PivotTab(PivotType.BIGDECIMAL, column, listener);
    }

    @Override
    public List<PivotTab> getTabs() {
        return List.of(uniqueTab);
    }

//    public List<ValueMatcher> getMatchers() {
//        IntegerColumn ic = getColumn();
//        matchers.clear();
//        List<Integer> intList = findIntegers(uniqueView);
//        if (!intList.isEmpty()){
//            matchers.add(new IntegerIsInMatcher(ic, intList));
//        }
//        return matchers;
//    }


    @Override
    public void loadCSV(final CSV csv) {
        getTabs().forEach(PivotTab::clear);
        IntegerColumn sc = getColumn();
        FrequencyDistribution<Integer> uniqueDistribution = csv.buildFrequencyDistribution(sc);
        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(), PivotType.INTEGER,
                    this.getListener()));
        }
        for (Integer key : uniqueDistribution.getKeys()) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    key + "", key, uniqueDistribution.getCount(key),
                    getColumn(), PivotType.INTEGER,
                    this.getListener()));
        }

    }

//    public static List<Integer> findIntegers(final PivotTableView view){
//        List<Integer> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                Integer i =  (Integer) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }
//
//    public List<IntegerRange> findIntegerRanges(final PivotTableView view){
//        List<IntegerRange> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                IntegerRange i =  (IntegerRange) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }

//    @Override
//    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//        if (main != null) {
//            main.handleSelectionChanged(column);
////                    if (newValue) {
////                        main.handleSelected(column, (String) value);
////                    } else {
////                        main.handleUnselected(column, (String) value);
////                    }
//        }
//    }
}
