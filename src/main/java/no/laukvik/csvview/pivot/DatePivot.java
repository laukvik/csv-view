package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.DateColumn;
import no.laukvik.csv.statistics.FrequencyDistribution;
import no.laukvik.csvview.utils.Builder;

import java.util.*;

/**
 * JavaFX control which enables selection of date details.
 * <p>
 * Range: from-to
 * Unique
 * Year
 * Month
 * Date
 * Week
 * Weekday
 */
public class DatePivot extends AbstractPivot<DateColumn> {

    private PivotTab uniqueTab;
    private PivotTab yearTab;
    private PivotTab monthTab;
    private PivotTab weekTab;
    private PivotTab domTab;
    private PivotTab weekdayTab;
    private PivotTab hourTab;
    private PivotTab minuteTab;
    private PivotTab secondTab;
    private PivotTab millisTab;

    public DatePivot(final DateColumn column, final PivotFilterListener listener) {
        super(column, listener);
        uniqueTab = new PivotTab(PivotType.DATE, column, listener);
        yearTab = new PivotTab(PivotType.DATE_YEAR, column, listener);
        monthTab = new PivotTab(PivotType.DATE_MONTH, column, listener);
        weekTab = new PivotTab(PivotType.DATE_WEEK, column, listener);
        domTab = new PivotTab(PivotType.DATE_DOM, column, listener);
        weekdayTab = new PivotTab(PivotType.DATE_WEEKDAY, column, listener);
        hourTab = new PivotTab(PivotType.DATE_HOUR, column, listener);
        minuteTab = new PivotTab(PivotType.DATE_MINUTE, column, listener);
        secondTab = new PivotTab(PivotType.DATE_SECOND, column, listener);
        millisTab = new PivotTab(PivotType.DATE_MILLIS, column, listener);
    }

    @Override
    public List<PivotTab> getTabs() {
        return List.of(uniqueTab, yearTab, monthTab, weekTab, domTab, weekdayTab ,hourTab, minuteTab, secondTab, millisTab);
    }

    public void loadCSV(final CSV csv) {
        getTabs().forEach(PivotTab::clear);

        ResourceBundle bundle = Builder.getBundle();

        DateColumn column = getColumn();

        FrequencyDistribution<Date> uniqueDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> yearDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> monthDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> domDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> weekDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> weekdayDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> hourDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> minuteDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> secondDistribution = new FrequencyDistribution<>(column);
        FrequencyDistribution<Integer> millisecondDistribution = new FrequencyDistribution<>(column);

        DateColumn dc = column;
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Date d = r.get(dc);
            uniqueDistribution.addValue(d);
            if (d == null) {
                yearDistribution.addValue(null);
                monthDistribution.addValue(null);
                domDistribution.addValue(null);
                weekDistribution.addValue(null);
                weekdayDistribution.addValue(null);
                hourDistribution.addValue(null);
                minuteDistribution.addValue(null);
                secondDistribution.addValue(null);
                millisecondDistribution.addValue(null);
            } else {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(d);
                //
                yearDistribution.addValue(cal.get(Calendar.YEAR));
                monthDistribution.addValue(cal.get(Calendar.MONTH));
                domDistribution.addValue(cal.get(Calendar.DATE));
                weekDistribution.addValue(cal.get(Calendar.WEEK_OF_YEAR));
                weekdayDistribution.addValue(cal.get(Calendar.DAY_OF_WEEK));

                hourDistribution.addValue(cal.get(Calendar.HOUR_OF_DAY));
                minuteDistribution.addValue(cal.get(Calendar.MINUTE));
                secondDistribution.addValue(cal.get(Calendar.SECOND));
                millisecondDistribution.addValue(cal.get(Calendar.MILLISECOND));
            }
        }
        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(), PivotType.DATE,
                    this.getListener()));
        }
        for (Date key : uniqueDistribution.getKeys()) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    getColumn().formatDate(key), key, uniqueDistribution.getCount(key),
                    getColumn(), PivotType.DATE,
                    this.getListener()));
        }


        // Year
        if (yearDistribution.getNullCount() > 0) {
            yearTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, yearDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_YEAR,
                    this.getListener()));
        }
        for (Integer key : yearDistribution.getKeys()) {
            yearTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, yearDistribution.getCount(key),
                    getColumn(), PivotType.DATE_YEAR,
                    this.getListener()));
        }


        // Month
        if (monthDistribution.getNullCount() > 0) {
            monthTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, monthDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_YEAR,
                    this.getListener()));
        }
        for (Integer key : monthDistribution.getKeys()) {
            monthTab.addPivotFilter(new PivotFilter(false,
                    getLanguage("date.month." + key), key, monthDistribution.getCount(key),
                    getColumn(), PivotType.DATE_YEAR,
                    this.getListener()));
        }


        // Date of month
        if (domDistribution.getNullCount() > 0) {
            domTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, yearDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_DOM,
                    this.getListener()));
        }
        for (Integer key : domDistribution.getKeys()) {
            domTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, domDistribution.getCount(key),
                    getColumn(), PivotType.DATE_DOM,
                    this.getListener()));
        }



        // Week
        if (weekDistribution.getNullCount() > 0) {
            weekTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, weekDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_WEEK,
                    this.getListener()));
        }
        for (Integer key : weekDistribution.getKeys()) {
            weekTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, weekDistribution.getCount(key),
                    getColumn(), PivotType.DATE_WEEK,
                    this.getListener()));
        }


        // Weekday
        if (weekdayDistribution.getNullCount() > 0) {
            weekdayTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, weekdayDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_WEEKDAY,
                    this.getListener()));
        }
        for (Integer key : weekdayDistribution.getKeys()) {
            weekdayTab.addPivotFilter(new PivotFilter(false,
                    getLanguage("date.weekday." + key), key, weekdayDistribution.getCount(key),
                    getColumn(), PivotType.DATE_WEEKDAY,
                    this.getListener()));
        }


        // Hour
        if (hourDistribution.getNullCount() > 0) {
            hourTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, hourDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_WEEK,
                    this.getListener()));
        }
        for (Integer key : hourDistribution.getKeys()) {
            hourTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, hourDistribution.getCount(key),
                    getColumn(), PivotType.DATE_WEEK,
                    this.getListener()));
        }

        // Minute
        if (minuteDistribution.getNullCount() > 0) {
            minuteTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, minuteDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_MINUTE,
                    this.getListener()));
        }
        for (Integer key : minuteDistribution.getKeys()) {
            minuteTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, hourDistribution.getCount(key),
                    getColumn(), PivotType.DATE_MINUTE,
                    this.getListener()));
        }

        // Second
        if (secondDistribution.getNullCount() > 0) {
            secondTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, secondDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_SECOND,
                    this.getListener()));
        }
        for (Integer key : secondDistribution.getKeys()) {
            secondTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, secondDistribution.getCount(key),
                    getColumn(), PivotType.DATE_SECOND,
                    this.getListener()));
        }


        // Millis
        if (millisecondDistribution.getNullCount() > 0) {
            millisTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, millisecondDistribution.getNullCount(),
                    getColumn(), PivotType.DATE_MILLIS,
                    this.getListener()));
        }
        for (Integer key : millisecondDistribution.getKeys()) {
            millisTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, millisecondDistribution.getCount(key),
                    getColumn(), PivotType.DATE_MILLIS,
                    this.getListener()));
        }
    }

    private String getLanguage(String s) {
        return Builder.getBundle().getString(s);
    }


//    public List<ValueMatcher> getMatchers() {
//        DateColumn dc = (DateColumn) getColumn();
//        List<ValueMatcher> matcher = new ArrayList<>();
//
//        List<Integer> intList;
//        List<String> stringList;
//        List<Date> dateList;
//
//        dateList = findDates(uniqueView);
//        if (!dateList.isEmpty()) {
//            matcher.add(new DateIsMatcher(dc, dateList));
//        }
//
//        intList = findIntegers(yearView);
//        if (!intList.isEmpty()) {
//            matcher.add(new YearMatcher(dc, intList));
//        }
//
//        intList = findIntegers(monthView);
//        if (!intList.isEmpty()) {
//            matcher.add(new MonthMatcher(dc, intList));
//        }
//
//        intList = findIntegers(dateView);
//        if (!intList.isEmpty()) {
//            matcher.add(new DateOfMonthMatcher(dc, intList));
//        }
//
//        intList = findIntegers(weekView);
//        if (!intList.isEmpty()) {
//            matcher.add(new WeekMatcher(dc, intList));
//        }
//
//        intList = findIntegers(weekdayView);
//        if (!intList.isEmpty()) {
//            matcher.add(new WeekdayMatcher(dc, intList));
//        }
//
//
//
//        intList = findIntegers(hourView);
//        if (!intList.isEmpty()) {
//            matcher.add(new HourMatcher(dc, intList));
//        }
//        intList = findIntegers(minuteView);
//        if (!intList.isEmpty()) {
//            matcher.add(new MinuteMatcher(dc, intList));
//        }
//        intList = findIntegers(secondView);
//        if (!intList.isEmpty()) {
//            matcher.add(new SecondMatcher(dc, intList));
//        }
//        intList = findIntegers(millisecondView);
//        if (!intList.isEmpty()) {
//            matcher.add(new MillisecondMatcher(dc, intList));
//        }
//
//
//        return matcher;
//    }
//
//    public List<Date> findDates(final FrequencyDistributionTableView view){
//        List<Date> list = new ArrayList<>();
//        for (PivotFilter omd : view.getItems()){
//            if (omd.isSelected()){
//                Date i =  (Date) omd.valueProperty().getValue();
//                list.add(i);
//            }
//        }
//        return list;
//    }

}
