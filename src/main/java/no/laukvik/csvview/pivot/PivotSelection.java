package no.laukvik.csvview.pivot;

import java.util.ArrayList;
import java.util.List;

public class PivotSelection {

    private List<PivotFilter> filters;

    public PivotSelection() {
        this.filters = new ArrayList<>();
    }

    public void clear(){
        filters.clear();
    }

    public void add(PivotFilter filter){
        filters.add(filter);
    }

    public void remove(PivotFilter filter){
        filters.remove(filter);
    }

    public List<PivotFilter> getFilters() {
        return filters;
    }
}
