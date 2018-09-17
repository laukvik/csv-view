package no.laukvik.csvview.pivot;

import no.laukvik.csv.columns.StringColumn;
import org.junit.Test;

import static org.junit.Assert.*;

public class PivotFilterTest {

    @Test
    public void equals() {
        StringColumn column = new StringColumn("Party");
        PivotFilter f1 = new PivotFilter(true, "Whig", "Whig", 5, column, PivotType.STRING, null);
        PivotFilter f2 = new PivotFilter(false, "Whig", "Whig", 3, column, PivotType.STRING, null);
        assertEquals(f1, f2);
    }

    @Test
    public void equals_should_fail() {
        StringColumn column = new StringColumn("Party");
        PivotFilter f1 = new PivotFilter(true, "Whig", null, 5, column, PivotType.STRING, null);
        PivotFilter f2 = new PivotFilter(true, "Whig", "Whig", 3, column, PivotType.STRING, null);
        assertNotEquals(f1, f2);
        assertNotEquals(f2, f1);
    }

}