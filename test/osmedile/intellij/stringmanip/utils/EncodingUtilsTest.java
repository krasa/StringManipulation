package osmedile.intellij.stringmanip.utils;

import org.junit.Assert;
import org.junit.Test;

public class EncodingUtilsTest {
    @Test
    public void encodeUrlWithout() throws Exception {
        Assert.assertEquals("foo%20bar", EncodingUtils.encodeUrlWithoutPlus("foo bar"));
        Assert.assertEquals("foo%20%2Bbar", EncodingUtils.encodeUrlWithoutPlus("foo +bar"));
    }

    @Test
    public void encodeUrl() throws Exception {
        Assert.assertEquals("foo+bar", EncodingUtils.encodeUrl("foo bar"));
        Assert.assertEquals("foo%2Bbar", EncodingUtils.encodeUrl("foo+bar"));
    }


    @Test
    public void decodeUrl() throws Exception {
        Assert.assertEquals("foo bar", EncodingUtils.decodeUrl("foo%20bar"));
        Assert.assertEquals("foo +bar", EncodingUtils.decodeUrl("foo%20%2Bbar"));

        Assert.assertEquals("foo bar", EncodingUtils.decodeUrl("foo+bar"));
        Assert.assertEquals("foo+bar", EncodingUtils.decodeUrl("foo%2Bbar"));
    }

}