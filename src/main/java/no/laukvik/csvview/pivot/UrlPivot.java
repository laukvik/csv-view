package no.laukvik.csvview.pivot;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.UrlColumn;
import no.laukvik.csv.statistics.FrequencyDistribution;

import java.net.URL;
import java.util.List;

/**
 * scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
 */
public class UrlPivot extends AbstractPivot<UrlColumn> {

    private PivotTab uniqueTab;
    private PivotTab protocolTab;
    private PivotTab hostTab;
    private PivotTab portTab;
    private PivotTab pathTab;
    private PivotTab fileTab;
    private PivotTab prefixTab;
    private PivotTab postfixTab;
    private PivotTab queryTab;
    private PivotTab anchorTab;

    public UrlPivot(final UrlColumn column, final PivotFilterListener listener) {
        super(column, listener);
        uniqueTab = new PivotTab(PivotType.URL, column, listener);
        protocolTab = new PivotTab(PivotType.URL_PROTOCOL, column, listener);
        hostTab = new PivotTab(PivotType.URL_HOST, column, listener);
        portTab = new PivotTab(PivotType.URL_PORT, column, listener);
        pathTab = new PivotTab(PivotType.URL_PATH, column, listener);
        fileTab = new PivotTab(PivotType.URL_FILE, column, listener);
        prefixTab = new PivotTab(PivotType.URL_PREFIX, column, listener);
        postfixTab = new PivotTab(PivotType.URL_POSTFIX, column, listener);
        queryTab = new PivotTab(PivotType.URL_QUERY, column, listener);
        anchorTab = new PivotTab(PivotType.URL_ANCHOR, column, listener);
    }

    @Override
    public List<PivotTab> getTabs() {
        return List.of(uniqueTab, protocolTab, hostTab, portTab, pathTab, fileTab, prefixTab, postfixTab, queryTab, anchorTab);
    }

//    public List<ValueMatcher> getMatchers() {
//        UrlColumn sc = (UrlColumn) getColumn();
//        matchers.clear();
//
//        List<URL> urlList;
//        List<String> stringList;
//        List<Integer> intList;
//
//        stringList = findStrings(uniqueView);
//        if (!stringList.isEmpty()){
//            urlList = new ArrayList<>();
//            for (String s : stringList){
//                try {
//                    urlList.add(new URL(s));
//                } catch (MalformedURLException e) {
//                }
//            }
//            matchers.add(new UrlMatcher(sc, urlList));
//        }
//
//        stringList = findStrings(protocolView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlProtocolMatcher(sc, stringList));
//        }
//
//        stringList = findStrings(hostView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlHostMatcher(sc, stringList));
//        }
//
//        stringList = findStrings(pathView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlPathMatcher(sc, stringList));
//        }
//
//        stringList = findStrings(fileView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlFileMatcher(sc, stringList));
//        }
//
//        stringList = findStrings(prefixView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlFilePrefixMatcher(sc, stringList));
//        }
//        stringList = findStrings(postfixView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlFilePostfixMatcher(sc, stringList));
//        }
//
//
//        stringList = findStrings(queryView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlQueryMatcher(sc, stringList));
//        }
//
//        stringList = findStrings(anchorView);
//        if (!stringList.isEmpty()){
//            matchers.add(new UrlAnchorMatcher(sc, stringList));
//        }
//
//        intList = findIntegers(portView);
//        if (!intList.isEmpty()){
//            matchers.add(new UrlPortMatcher(sc, intList));
//        }
//
//        return matchers;
//    }

    public void loadCSV(final CSV csv) {
        getTabs().forEach(PivotTab::clear);
        UrlColumn uc = getColumn();

        FrequencyDistribution<String> uniqueDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> protocolDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> hostDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<Integer> portDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> pathDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> fileDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> postfixDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> prefixDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> queryDistribution = new FrequencyDistribution<>(uc);
        FrequencyDistribution<String> fragmentDistribution = new FrequencyDistribution<>(uc);

        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            URL u = r.get(uc);
            if (u == null) {
                uniqueDistribution.addValue(null);
                protocolDistribution.addValue(null);
                hostDistribution.addValue(null);
                portDistribution.addValue(null);
                pathDistribution.addValue(null);
                fileDistribution.addValue(null);
                postfixDistribution.addValue(null);
                prefixDistribution.addValue(null);
                queryDistribution.addValue(null);
                fragmentDistribution.addValue(null);
            } else {
                uniqueDistribution.addValue(u.toExternalForm());
                hostDistribution.addValue(u.getHost());
                portDistribution.addValue(UrlColumn.getPort(u));
                pathDistribution.addValue(UrlColumn.getPath(u));
                fileDistribution.addValue(UrlColumn.getFilename(u));
                postfixDistribution.addValue(UrlColumn.getPostfix(u));
                prefixDistribution.addValue(UrlColumn.getPrefix(u));
                queryDistribution.addValue(u.getQuery());
                fragmentDistribution.addValue(UrlColumn.getAnchor(u));
            }
        }

        // Unique
        if (uniqueDistribution.getNullCount() > 0) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, uniqueDistribution.getNullCount(),
                    getColumn(), PivotType.URL,
                    this.getListener()));
        }
        for (String key : uniqueDistribution.getKeys()) {
            uniqueTab.addPivotFilter(new PivotFilter(false,
                    key, key, uniqueDistribution.getCount(key),
                    getColumn(), PivotType.URL,
                    this.getListener()));
        }

        // Protocol
        if (protocolDistribution.getNullCount() > 0) {
            protocolTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, protocolDistribution.getNullCount(),
                    getColumn(), PivotType.URL_PROTOCOL,
                    this.getListener()));
        }
        for (String key : protocolDistribution.getKeys()) {
            protocolTab.addPivotFilter(new PivotFilter(false,
                    key, key, protocolDistribution.getCount(key),
                    getColumn(), PivotType.URL_PROTOCOL,
                    this.getListener()));
        }


        // Host
        if (hostDistribution.getNullCount() > 0) {
            hostTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, hostDistribution.getNullCount(),
                    getColumn(), PivotType.URL_HOST,
                    this.getListener()));
        }
        for (String key : hostDistribution.getKeys()) {
            hostTab.addPivotFilter(new PivotFilter(false,
                    key, key, hostDistribution.getCount(key),
                    getColumn(), PivotType.URL_HOST,
                    this.getListener()));
        }



        // Path
        if (pathDistribution.getNullCount() > 0) {
            pathTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, pathDistribution.getNullCount(),
                    getColumn(), PivotType.URL_PATH,
                    this.getListener()));
        }
        for (String key : pathDistribution.getKeys()) {
            pathTab.addPivotFilter(new PivotFilter(false,
                    key, key, pathDistribution.getCount(key),
                    getColumn(), PivotType.URL_PATH,
                    this.getListener()));
        }


        // File
        if (fileDistribution.getNullCount() > 0) {
            fileTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, fileDistribution.getNullCount(),
                    getColumn(), PivotType.URL_PATH,
                    this.getListener()));
        }
        for (String key : fileDistribution.getKeys()) {
            fileTab.addPivotFilter(new PivotFilter(false,
                    key, key, fileDistribution.getCount(key),
                    getColumn(), PivotType.URL_PATH,
                    this.getListener()));
        }

        // Prefix
        if (prefixDistribution.getNullCount() > 0) {
            prefixTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, prefixDistribution.getNullCount(),
                    getColumn(), PivotType.URL_PREFIX,
                    this.getListener()));
        }
        for (String key : prefixDistribution.getKeys()) {
            prefixTab.addPivotFilter(new PivotFilter(false,
                    key, key, prefixDistribution.getCount(key),
                    getColumn(), PivotType.URL_PREFIX,
                    this.getListener()));
        }
        // Postfix
        if (postfixDistribution.getNullCount() > 0) {
            postfixTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, postfixDistribution.getNullCount(),
                    getColumn(), PivotType.URL_POSTFIX,
                    this.getListener()));
        }
        for (String key : postfixDistribution.getKeys()) {
            postfixTab.addPivotFilter(new PivotFilter(false,
                    key, key, postfixDistribution.getCount(key),
                    getColumn(), PivotType.URL_POSTFIX,
                    this.getListener()));
        }

        // Port
        if (portDistribution.getNullCount() > 0) {
            portTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, portDistribution.getNullCount(),
                    getColumn(), PivotType.URL_POSTFIX,
                    this.getListener()));
        }
        for (Integer key : portDistribution.getKeys()) {
            portTab.addPivotFilter(new PivotFilter(false,
                    Integer.toString(key), key, portDistribution.getCount(key),
                    getColumn(), PivotType.URL_POSTFIX,
                    this.getListener()));
        }

        // QueryModel
        if (queryDistribution.getNullCount() > 0) {
            queryTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, queryDistribution.getNullCount(),
                    getColumn(), PivotType.URL_QUERY,
                    this.getListener()));
        }
        for (String key : queryDistribution.getKeys()) {
            queryTab.addPivotFilter(new PivotFilter(false,
                    key, key, queryDistribution.getCount(key),
                    getColumn(), PivotType.URL_QUERY,
                    this.getListener()));
        }


        // Fragment/anchor
        if (fragmentDistribution.getNullCount() > 0) {
            anchorTab.addPivotFilter(new PivotFilter(false,
                    EMPTY, null, fragmentDistribution.getNullCount(),
                    getColumn(), PivotType.URL_ANCHOR,
                    this.getListener()));
        }
        for (String key : fragmentDistribution.getKeys()) {
            anchorTab.addPivotFilter(new PivotFilter(false,
                    key, key, fragmentDistribution.getCount(key),
                    getColumn(), PivotType.URL_ANCHOR,
                    this.getListener()));
        }
    }

}
