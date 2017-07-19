package osmedile.intellij.stringmanip.utils;

import org.junit.Assert;
import org.junit.Test;

public class EncodingUtilsTest {

    @Test
    public void encodeUrlWithout() throws Exception {
        Assert.assertEquals("foo%20bar", testEncodeNew("foo bar"));
        Assert.assertEquals("foo%20%2Bbar%20%20bar", testEncodeNew("foo +bar  bar"));
        Assert.assertEquals("foo%20%2Bbar%20%20bar", testEncodeNew("foo +bar  bar"));
    }

    protected String testEncodeNew(String foo_bar) {
        String encoded = EncodingUtils.encodeUrlRFC3986(foo_bar);
        Assert.assertEquals(foo_bar, EncodingUtils.decodeUrlRFC3986(encoded));
        return encoded;
    }

    @Test
    public void encodeUrl() throws Exception {
        Assert.assertEquals("foo+bar", testEncode("foo bar"));
        Assert.assertEquals("foo%2Bbar", testEncode("foo+bar"));
    }

    protected String testEncode(String foo_bar) {
        String encoded = EncodingUtils.encodeUrl(foo_bar);
        Assert.assertEquals(foo_bar, EncodingUtils.decodeUrl(encoded));
        return encoded;
    }

    @Test
    public void decodeUrl() throws Exception {
        Assert.assertEquals("foo bar", EncodingUtils.decodeUrl("foo%20bar"));
        Assert.assertEquals("foo +bar", EncodingUtils.decodeUrl("foo%20%2Bbar"));

        Assert.assertEquals("foo bar", EncodingUtils.decodeUrl("foo+bar"));
        Assert.assertEquals("foo+bar", EncodingUtils.decodeUrl("foo%2Bbar"));
    }


}