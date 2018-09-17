package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.StringColumn;
import no.laukvik.csv.statistics.FrequencyDistribution;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 */
public class StringPivot extends AbstractPivot<StringColumn> {

    private PivotTab uniqueTab;
    private PivotTab letterTab;
    private PivotTab lengthTab;
    private PivotTab prefixTab;
    private PivotTab postfixTab;
    private PivotTab wordsTab;

    public StringPivot(final StringColumn column, final PivotFilterListener listener) {
        super(column, listener);
        uniqueTab = new PivotTab(PivotType.STRING, column, listener);
        letterTab = new PivotTab(PivotType.STRING_LETTER, column, listener);
        lengthTab = new PivotTab(PivotType.STRING_LENGTH, column, listener);
        prefixTab = new PivotTab(PivotType.STRING_PREFIX, column, listener);
        postfixTab = new PivotTab(PivotType.STRING_POSTFIX, column, listener);
        wordsTab = new PivotTab(PivotType.STRING_WORDS, column, listener);
    }

    @Override
    public List<PivotTab> getTabs() {
        return List.of(uniqueTab, letterTab, lengthTab, prefixTab, postfixTab, wordsTab);
    }

//    public List<ValueMatcher> getMatchers() {
//        StringColumn sc = getColumn();
//        matchers.clear();
//
//        List<String> stringList;
//        List<Integer> intList;
//
//        stringList = findStrings(uniqueView);
//        if (!stringList.isEmpty()){
//            matchers.add(new StringInMatcher(sc, stringList));
//        }
//
//        stringList = findStrings(firstLetterView);
//        if (!stringList.isEmpty()){
//            matchers.add(new FirstLetterMatcher(sc, stringList));
//        }
//
//        intList = findIntegers(lengthView);
//        if (!intList.isEmpty()){
//            matchers.add(new StringLengthMatcher(sc, intList));
//        }
//
//        stringList = findStrings(prefixView);
//        if (!stringList.isEmpty()){
//            matchers.add(new PrefixMatcher(sc, stringList));
//        }
//
//        stringList = findStrings(postfixView);
//        if (!stringList.isEmpty()){
//            matchers.add(new PostfixMatcher(sc, stringList));
//        }
//
//        intList = findIntegers(wordsView);
//        if (!intList.isEmpty()){
//            matchers.add(new WordCountMatcher(sc, intList));
//        }
//
//        return matchers;
//    }

    @Override
    public void loadCSV(final CSV csv) {
        getTabs().forEach(PivotTab::clear);
        StringColumn sc = getColumn();

        FrequencyDistribution<String> uniqueDistribution = new FrequencyDistribution<>(sc);
        FrequencyDistribution<String> firstLetterDistribution = new FrequencyDistribution<>(sc);
        FrequencyDistribution<Integer> lengthDistribution = new FrequencyDistribution<>(sc);
        FrequencyDistribution<String> prefixDistribution = new FrequencyDistribution<>(sc);
        FrequencyDistribution<String> postfixDistribution = new FrequencyDistribution<>(sc);
        FrequencyDistribution<Integer> wordsDistribution = new FrequencyDistribution<>(sc);

        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            String s = r.get(sc);
            if (s == null) {
                uniqueDistribution.addValue(null);
                firstLetterDistribution.addValue(null);
                prefixDistribution.addValue(null);
                lengthDistribution.addValue(null);
                postfixDistribution.addValue(null);
                wordsDistribution.addValue(null);
            } else {
                uniqueDistribution.addValue(s);
                firstLetterDistribution.addValue(StringColumn.getFirstLetter(s));
                lengthDistribution.addValue(s.length());
                prefixDistribution.addValue(StringColumn.getPrefix(s));
                postfixDistribution.addValue(StringColumn.getPostfix(s));
                wordsDistribution.addValue(StringColumn.getWordCount(s));
            }
        }

        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(), PivotType.STRING,
                    this.getListener()));
        }
        for (String key : uniqueDistribution.getKeys()) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    key, key, uniqueDistribution.getCount(key),
                    getColumn(), PivotType.STRING,
                    this.getListener()));
        }


        // Letter
        if (firstLetterDistribution.getNullCount() > 0) {
            letterTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, firstLetterDistribution.getNullCount(),
                    getColumn(), PivotType.STRING_LETTER,
                    this.getListener()));
        }
        for (String key : firstLetterDistribution.getKeys()) {
            letterTab.addPivotFilter(new PivotFilter(false,
                    key, key, firstLetterDistribution.getCount(key),
                    getColumn(), PivotType.STRING_LETTER,
                    this.getListener()));
        }



        // Length
        if (lengthDistribution.getNullCount() > 0) {
            lengthTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, lengthDistribution.getNullCount(),
                    getColumn(), PivotType.STRING_LENGTH,
                    this.getListener()));
        }
        for (Integer key : lengthDistribution.getKeys()) {
            lengthTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, lengthDistribution.getCount(key),
                    getColumn(), PivotType.STRING_LENGTH,
                    this.getListener()));
        }

        // Prefix
        if (prefixDistribution.getNullCount() > 0) {
            prefixTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, prefixDistribution.getNullCount(),
                    getColumn(), PivotType.STRING_PREFIX,
                    this.getListener()));
        }
        for (String key : prefixDistribution.getKeys()) {
            prefixTab.addPivotFilter(new PivotFilter(false,
                    key, key, prefixDistribution.getCount(key),
                    getColumn(), PivotType.STRING_PREFIX,
                    this.getListener()));
        }


        // Postfix
        if (postfixDistribution.getNullCount() > 0) {
            postfixTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, postfixDistribution.getNullCount(),
                    getColumn(), PivotType.STRING_POSTFIX,
                    this.getListener()));
        }
        for (String key : postfixDistribution.getKeys()) {
            postfixTab.addPivotFilter(new PivotFilter(false,
                    key, key, postfixDistribution.getCount(key),
                    getColumn(), PivotType.STRING_POSTFIX,
                    this.getListener()));
        }



        // Word count
        if (wordsDistribution.getNullCount() > 0) {
            wordsTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, wordsDistribution.getNullCount(),
                    getColumn(), PivotType.STRING_WORDS,
                    this.getListener()));
        }
        for (Integer key : wordsDistribution.getKeys()) {
            wordsTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, wordsDistribution.getCount(key),
                    getColumn(), PivotType.STRING_WORDS,
                    this.getListener()));
        }

    }

    public static List<String> findStrings(final PivotTableView view){
        List<String> list = new ArrayList<>();
        for (PivotFilter omd : view.getItems()){
            if (omd.isSelected()){
                String i =  (String) omd.valueProperty().getValue();
                list.add(i);
            }
        }
        return list;
    }

}
