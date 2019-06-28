package osmedile.intellij.stringmanip;

import org.junit.Assert;
import org.junit.Test;

public class SwitchFilePathSeparatorsTest {

    protected SwitchFilePathSeparators action;

    @Test
    public void testSwitchFilePathSeparators_BackslashToSlash() {
        action = new SwitchFilePathSeparators(false);
        Assert.assertEquals("Foo bar Wee All", action.transformByLine("Foo bar Wee All"));
        // first found determine op
        Assert.assertEquals("Foo/bar/foo", action.transformByLine("Foo\\bar\\foo"));
        // second works same
        Assert.assertEquals("Foo/bar/foo", action.transformByLine("Foo\\bar\\foo"));
        // don't change action
        Assert.assertEquals("Foo/bar/foo", action.transformByLine("Foo/bar/foo"));
    }

    @Test
    public void testSwitchFilePathSeparators_BackslashToSlashMix() {
        action = new SwitchFilePathSeparators(false);
        Assert.assertEquals("Foo bar Wee All", action.transformByLine("Foo bar Wee All"));
        // first found determine op
        Assert.assertEquals("Foo/bar/foo/bar", action.transformByLine("Foo\\bar\\foo/bar"));
        // second works same
        Assert.assertEquals("Foo/bar/foo/bar", action.transformByLine("Foo\\bar\\foo/bar"));
        // don't change action
        Assert.assertEquals("Foo/bar/foo", action.transformByLine("Foo/bar/foo"));
    }

    @Test
    public void testSwitchFilePathSeparators_BackslashToSlashSingle() {
        action = new SwitchFilePathSeparators(false);
        Assert.assertEquals("Foo/bar/foo/bar", action.transformByLine("Foo\\bar\\foo\\bar"));
    }


    @Test
    public void testSwitchFilePathSeparators_SlashToBackSlash() {
        action = new SwitchFilePathSeparators(false);
        // first found determine op
        Assert.assertEquals("Foo\\bar\\foo", action.transformByLine("Foo/bar/foo"));
        // second works same
        Assert.assertEquals("Foo\\bar\\foo", action.transformByLine("Foo/bar/foo"));
        // don't change action
        Assert.assertEquals("Foo\\bar\\foo", action.transformByLine("Foo\\bar\\foo"));
    }
    @Test
    public void testSwitchFilePathSeparators_SlashToBackSlashMix() {
        action = new SwitchFilePathSeparators(false);
        Assert.assertEquals("Foo bar Wee All", action.transformByLine("Foo bar Wee All"));
        // first found determine op
        Assert.assertEquals("Foo\\bar\\foo\\bar", action.transformByLine("Foo/bar/foo\\bar"));
        // second works same
        Assert.assertEquals("Foo\\bar\\foo\\bar", action.transformByLine("Foo/bar/foo\\bar"));
        // don't change action
        Assert.assertEquals("Foo\\bar\\foo", action.transformByLine("Foo\\bar\\foo"));
    }

    @Test
    public void testSwitchFilePathSeparators_SlashToBackSlashSingle() {
        action = new SwitchFilePathSeparators(false);
        Assert.assertEquals("Foo\\bar\\foo", action.transformByLine("Foo/bar/foo"));
    }

}
