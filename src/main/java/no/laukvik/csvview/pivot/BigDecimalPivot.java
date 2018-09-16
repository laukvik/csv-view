package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.BigDecimalColumn;
import no.laukvik.csv.statistics.FrequencyDistribution;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 *
 *
 */
public class BigDecimalPivot extends AbstractPivot<BigDecimalColumn> {

    private PivotTab uniqueTab;

    public BigDecimalPivot(final BigDecimalColumn column, final PivotFilterListener listener) {
        super(column, listener);
        uniqueTab = new PivotTab(PivotType.BIGDECIMAL, column, listener);
    }

    @Override
    public List<PivotTab> getTabs() {
        return List.of(uniqueTab);
    }

//    public List<ValueMatcher> getMatchers() {
//        BigDecimalColumn bc = getColumn();
//        matchers.clear();
//        List<BigDecimal> bigDecimals;
//        bigDecimals = findBigDecimals(uniqueView);
//        if (!bigDecimals.isEmpty()) {
//            matchers.add(new BigDecimalMatcher(bc, bigDecimals));
//        }
//        return matchers;
//    }

    public void loadCSV(final CSV csv) {
        uniqueTab.clear();
        BigDecimalColumn sc = getColumn();
        FrequencyDistribution<BigDecimal> uniqueDistribution = csv.buildFrequencyDistribution(sc);
        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(), PivotType.BIGDECIMAL,
                    this.getListener()));
        }
        for (BigDecimal key : uniqueDistribution.getKeys()) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    key + "", key, uniqueDistribution.getCount(key),
                    getColumn(), PivotType.BIGDECIMAL,
                    this.getListener()));
        }
    }

//    public static List<BigDecimal> findBigDecimals(final FrequencyDistributionTableView view){
//        List<BigDecimal> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                BigDecimal i =  (BigDecimal) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }
//
//    public static List<BigDecimalRange> findBigDecimalRanges(final FrequencyDistributionTableView view){
//        List<BigDecimalRange> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                BigDecimalRange i =  (BigDecimalRange) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }
}
