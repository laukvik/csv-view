package no.laukvik.csvview.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.*;
import no.laukvik.csv.query.*;
import no.laukvik.csvview.pivot.PivotSelection;
import no.laukvik.csvview.pivot.PivotType;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class QueryModel {

    private List<Column> columns;
    private PivotSelection selection;

    public QueryModel() {
        columns = new ArrayList<>();
        selection = new PivotSelection();
    }


    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public PivotSelection getSelection() {
        return selection;
    }

    public void setSelection(PivotSelection selection) {
        this.selection = selection;
    }

    public List<ValueMatcher> buildMatchersByStringColumn(StringColumn column){
        List<ValueMatcher> query = new ArrayList<>();
        for (PivotType type : PivotType.values()){
            if (type == PivotType.STRING || type == PivotType.STRING_LETTER || type == PivotType.STRING_PREFIX || type == PivotType.STRING_POSTFIX){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    switch (type){
                        case STRING: query.add(new StringInMatcher(column, list)); break;
                        case STRING_LETTER: query.add(new FirstLetterMatcher(column, list)); break;
                        case STRING_PREFIX: query.add(new PrefixMatcher(column, list)); break;
                        case STRING_POSTFIX: query.add(new PostfixMatcher(column, list)); break;
                    }
                }
            }
            if (type == PivotType.STRING_LENGTH || type == PivotType.STRING_WORDS){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    switch (type){
                        case STRING_LENGTH: query.add(new StringLengthMatcher(column, list)); break;
                        case STRING_WORDS: query.add(new WordCountMatcher(column, list)); break;
                    }
                }
            }
        }
        return query;
    }

    public List<ValueMatcher> buildMatchersByIntegerColumn(IntegerColumn column){
        List<ValueMatcher> query = new ArrayList<>();
        for (PivotType type : PivotType.values()){
            if (type == PivotType.INTEGER){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    switch (type){
                        case INTEGER: query.add(new IntegerIsInMatcher(column, list)); break;
                    }
                }
            }
        }
        return query;
    }

    public List<ValueMatcher> buildMatchersByDoubleColumn(DoubleColumn column){
        List<ValueMatcher> query = new ArrayList<>();
        for (PivotType type : PivotType.values()){
            if (type == PivotType.DOUBLE){
                List<Double> list = getDoubles(column, type);
                if (!list.isEmpty()){
                    switch (type){
                        case DOUBLE: query.add(new DoubleMatcher(column, list)); break;
                    }
                }
            }
        }
        return query;
    }

    public List<ValueMatcher> buildMatchersByFloatColumn(FloatColumn column){
        List<ValueMatcher> query = new ArrayList<>();
        for (PivotType type : PivotType.values()){
            if (type == PivotType.FLOAT){
                List<Float> list = getFloats(column, type);
                if (!list.isEmpty()){
                    switch (type){
                        case FLOAT: query.add(new FloatMatcher(column, list)); break;
                    }
                }
            }
        }
        return query;
    }

    public List<ValueMatcher> buildMatchersByBigDecimalColumn(BigDecimalColumn column){
        List<ValueMatcher> query = new ArrayList<>();
        for (PivotType type : PivotType.values()){
            if (type == PivotType.BIGDECIMAL){
                List<BigDecimal> list = getBigDecimals(column, type);
                if (!list.isEmpty()){
                    switch (type){
                        case BIGDECIMAL: query.add(new BigDecimalMatcher(column, list)); break;
                    }
                }
            }
        }
        return query;
    }

    public List<ValueMatcher> buildMatchersByDateColumn(DateColumn column){
        List<ValueMatcher> query = new ArrayList<>();
        for (PivotType type : PivotType.values()){
            if (type == PivotType.DATE){
                List<Date> list = getDates(column, type);
                if (!list.isEmpty()){
                    query.add(new DateIsInMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_YEAR){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new YearMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_MONTH){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new MonthMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_DOM){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new DateOfMonthMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_WEEK){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new WeekMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_WEEKDAY){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new WeekdayMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_HOUR){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new HourMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_MINUTE){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new MinuteMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_SECOND){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new SecondMatcher(column, list));
                }
            }
            if (type == PivotType.DATE_MILLIS){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new MillisecondMatcher(column, list));
                }
            }
        }
        return query;
    }

    public URL getURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public List<ValueMatcher> buildMatchersByUrlColumn(UrlColumn column){
        List<ValueMatcher> query = new ArrayList<>();
        for (PivotType type : PivotType.values()){
            if (type == PivotType.URL){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlMatcher(column, list.stream().map(s -> getURL(s)).collect(Collectors.toList())));
                }
            }
            if (type == PivotType.URL_PROTOCOL){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlProtocolMatcher(column, list));
                }
            }
            if (type == PivotType.URL_HOST){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlHostMatcher(column, list));
                }
            }
            if (type == PivotType.URL_PORT){
                List<Integer> list = getIntegers(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlPortMatcher(column, list));
                }
            }
            if (type == PivotType.URL_PATH){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlPathMatcher(column, list));
                }
            }
            if (type == PivotType.URL_FILE){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlFileMatcher(column, list));
                }
            }
            if (type == PivotType.URL_PREFIX){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlFilePrefixMatcher(column, list));
                }
            }
            if (type == PivotType.URL_POSTFIX){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlFilePostfixMatcher(column, list));
                }
            }
            if (type == PivotType.URL_QUERY){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlQueryMatcher(column, list));
                }
            }
            if (type == PivotType.URL_ANCHOR){
                List<String> list = getStrings(column, type);
                if (!list.isEmpty()){
                    query.add(new UrlAnchorMatcher(column, list));
                }
            }
        }
        return query;
    }

    public List<ValueMatcher> buildMatchersByColumn(Column column){
        if (column instanceof StringColumn){
            return buildMatchersByStringColumn((StringColumn) column);
        }
        if (column instanceof IntegerColumn){
            return buildMatchersByIntegerColumn((IntegerColumn) column);
        }
        if (column instanceof DoubleColumn){
            return buildMatchersByDoubleColumn((DoubleColumn) column);
        }
        if (column instanceof FloatColumn){
            return buildMatchersByFloatColumn((FloatColumn) column);
        }
        if (column instanceof BigDecimalColumn){
            return buildMatchersByBigDecimalColumn((BigDecimalColumn) column);
        }
        if (column instanceof DateColumn){
            return buildMatchersByDateColumn((DateColumn) column);
        }
        if (column instanceof UrlColumn){
            return buildMatchersByUrlColumn((UrlColumn) column);
        }
        return new ArrayList<>();
    }

    public Query buildQuery(CSV csv) {
        Query query = new Query();
        for (Column column : csv.getColumns()){
            query.getMatchers().addAll(buildMatchersByColumn(column));
        }
        return query;
    }

    public List<Integer> getIntegers(Column column, PivotType type){
        return selection
                .getFilters()
                .stream()
                .filter(filter -> filter.getColumn() == column)
                .filter(filter -> filter.getPivotType() == type)
                .map(filter -> (Integer) filter.getValue())
                .collect(Collectors.toList());
    }

    public List<String> getStrings(Column column, PivotType type){
        return selection
            .getFilters()
            .stream()
            .filter(filter -> filter.getColumn() == column)
            .filter(filter -> filter.getPivotType() == type)
            .map(filter -> (String) filter.getValue())
            .collect(Collectors.toList());
    }

    public List<BigDecimal> getBigDecimals(Column column, PivotType type){
        return selection
                .getFilters()
                .stream()
                .filter(filter -> filter.getColumn() == column)
                .filter(filter -> filter.getPivotType() == type)
                .map(filter -> (BigDecimal) filter.getValue())
                .collect(Collectors.toList());
    }

    public List<Float> getFloats(Column column, PivotType type){
        return selection
                .getFilters()
                .stream()
                .filter(filter -> filter.getColumn() == column)
                .filter(filter -> filter.getPivotType() == type)
                .map(filter -> (Float) filter.getValue())
                .collect(Collectors.toList());
    }

    public List<Double> getDoubles(Column column, PivotType type){
        return selection
                .getFilters()
                .stream()
                .filter(filter -> filter.getColumn() == column)
                .filter(filter -> filter.getPivotType() == type)
                .map(filter -> (Double) filter.getValue())
                .collect(Collectors.toList());
    }


    public List<Date> getDates(Column column, PivotType type){
        return selection
                .getFilters()
                .stream()
                .filter(filter -> filter.getColumn() == column)
                .filter(filter -> filter.getPivotType() == type)
                .map(filter -> (Date) filter.getValue())
                .collect(Collectors.toList());
    }

    public List<URL> getURLs(Column column, PivotType type){
        return selection
                .getFilters()
                .stream()
                .filter(filter -> filter.getColumn() == column)
                .filter(filter -> filter.getPivotType() == type)
                .map(filter -> (URL) filter.getValue())
                .collect(Collectors.toList());
    }

}
