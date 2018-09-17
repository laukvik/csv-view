package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.FloatColumn;
import no.laukvik.csv.statistics.FrequencyDistribution;

import java.util.List;

/**
 *
 *
 *
 */
public class FloatPivot extends AbstractPivot<FloatColumn> {

    private PivotTab uniqueTab;

    public FloatPivot(final FloatColumn column, final PivotFilterListener listener) {
        super(column, listener);
        uniqueTab = new PivotTab(PivotType.BIGDECIMAL, column, listener);
    }

    @Override
    public List<PivotTab> getTabs() {
        return List.of(uniqueTab);
    }

//    public List<ValueMatcher> getMatchers() {
//        FloatColumn fc = getColumn();
//        matchers.clear();
//
//        List<Float> floatList = findFloats(uniqueView);
//        if (!floatList.isEmpty()) {
//            matchers.add(new FloatMatcher(fc, floatList));
//        }
//
//        List<FloatRange> rangeList = findFloatRanges(rangeView);
//        if (!rangeList.isEmpty()){
//            matchers.add(new FloatRangeMatcher(fc, rangeList));
//        }
//
//        return matchers;
//    }

    public void loadCSV(final CSV csv) {
        getTabs().forEach(PivotTab::clear);
        FloatColumn sc = getColumn();
        FrequencyDistribution<Float> uniqueDistribution = csv.buildFrequencyDistribution(sc);
        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(), PivotType.FLOAT,
                    this.getListener()));
        }
        for (Float key : uniqueDistribution.getKeys()) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    key + "", key, uniqueDistribution.getCount(key),
                    getColumn(), PivotType.FLOAT,
                    this.getListener()));
        }
    }

//    public List<Float> findFloats(final PivotTableView view){
//        List<Float> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                Float i =  (Float) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }
//
//    public List<FloatRange> findFloatRanges(final PivotTableView view){
//        List<FloatRange> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                FloatRange i =  (FloatRange) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }

}
