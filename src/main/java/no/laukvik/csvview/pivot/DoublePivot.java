package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.DoubleColumn;
import no.laukvik.csv.statistics.FrequencyDistribution;

import java.util.List;

/**
 *
 *
 *
 */
public class DoublePivot extends AbstractPivot<DoubleColumn> {

    private PivotTab uniqueTab;

    public DoublePivot(final DoubleColumn column, final PivotFilterListener listener) {
        super(column, listener);
        uniqueTab = new PivotTab(PivotType.BIGDECIMAL, column, listener);
    }

    @Override
    public List<PivotTab> getTabs() {
        return List.of(uniqueTab);
    }

//    public List<ValueMatcher> getMatchers() {
//        DoubleColumn ic = (DoubleColumn) getColumn();
//        matchers.clear();
//
//        List<Double> doubleList = findDoubles(uniqueView);
//        if (!doubleList.isEmpty()) {
//            matchers.add(new DoubleMatcher(ic, doubleList));
//        }
//
//        return matchers;
//    }

    public void loadCSV(final CSV csv) {
        getTabs().forEach(PivotTab::clear);

        DoubleColumn sc = getColumn();
        FrequencyDistribution<Double> uniqueDistribution = csv.buildFrequencyDistribution(sc);
        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(), PivotType.DOUBLE,
                    this.getListener()));
        }
        for (Double key : uniqueDistribution.getKeys()) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    key + "", key, uniqueDistribution.getCount(key),
                    getColumn(), PivotType.DOUBLE,
                    this.getListener()));
        }
    }

//    public static List<Double> findDoubles(final FrequencyDistributionTableView view){
//        List<Double> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                Double i =  (Double) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }
//
//    public static List<DoubleRange> findDoubleRanges(final FrequencyDistributionTableView view){
//        List<DoubleRange> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                DoubleRange i =  (DoubleRange) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }

}
