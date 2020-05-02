package com.github.krasa.stringmanipulation.utils;

import com.github.krasa.stringmanipulation.utils.common.FilterUtils;
import org.junit.Assert;
import org.junit.Test;

import static com.github.krasa.stringmanipulation.utils.common.FilterUtils.filterNull;

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