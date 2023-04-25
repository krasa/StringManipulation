package osmedile.intellij.stringmanip.utils;

import org.junit.Assert;
import org.junit.Test;

import static osmedile.intellij.stringmanip.utils.FilterUtils.filterNull;

public class FilterUtilsTest {
    @Test
    public void keepDuplicates() throws Exception {
        Assert.assertArrayEquals(new String[]{"a", "b",}, filterNull(FilterUtils.keepDuplicates(new String[]{"a", "a", "a", "b", "b", "c"})));
    }

    @Test
    public void filterDuplicates() throws Exception {
        Assert.assertArrayEquals(new String[]{"a", "b", "c"}, filterNull(FilterUtils.filterDuplicates(new String[]{"a", "a", "a", "b", "b", "c"})));
    }

}