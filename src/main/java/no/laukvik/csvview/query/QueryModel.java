package no.laukvik.csvview.query;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.StringColumn;
import no.laukvik.csv.query.*;
import no.laukvik.csvview.pivot.PivotSelection;
import no.laukvik.csvview.pivot.PivotType;

import java.util.ArrayList;
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

    public Query buildQuery(CSV csv) {
        Query query = new Query();
        // Finn alle PivotType.STRING og lag en matcher med det
        for (Column column : csv.getColumns()){
            if (column instanceof StringColumn){
                StringColumn sc = (StringColumn) column;
                for (PivotType type : PivotType.values()){

                    if (type == PivotType.STRING || type == PivotType.STRING_LETTER || type == PivotType.STRING_PREFIX || type == PivotType.STRING_POSTFIX){
                        List<String> list = getStrings(sc, type);
                        if (!list.isEmpty()){
                            switch (type){
                                case STRING: query.addMatcher(new StringInMatcher(sc, list)); break;
                                case STRING_LETTER: query.addMatcher(new FirstLetterMatcher(sc, list)); break;
                                case STRING_PREFIX: query.addMatcher(new PrefixMatcher(sc, list)); break;
                                case STRING_POSTFIX: query.addMatcher(new PostfixMatcher(sc, list)); break;
                            }
                        }
                    }
                    if (type == PivotType.STRING_LENGTH || type == PivotType.STRING_WORDS){
                        List<Integer> list = getIntegers(sc, type);
                        if (!list.isEmpty()){
                            switch (type){
                                case STRING_LENGTH: query.addMatcher(new StringLengthMatcher(sc, list)); break;
                                case STRING_WORDS: query.addMatcher(new WordCountMatcher(sc, list)); break;
                            }
                        }
                    }


                }
            }
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

}
