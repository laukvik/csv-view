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

    public boolean contains(PivotFilter filter){
        return this.filters.contains(filter);
    }

    public void add(PivotFilter filter){
        if (filters.contains(filter)){
            return;
        }
        filters.add(filter);
    }

    public void remove(PivotFilter filter){
        filters.remove(filter);
    }

    public List<PivotFilter> getFilters() {
        return filters;
    }
}
