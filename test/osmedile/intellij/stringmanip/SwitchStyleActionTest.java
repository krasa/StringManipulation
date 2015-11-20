package osmedile.intellij.stringmanip;

import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.SwitchStyleAction;

import java.util.ArrayList;
import java.util.List;

/*+1 and -1 are magic*/
public class SwitchStyleActionTest {

    private static final int DUMMY_STYLES = 2;//ALL_UPPER_CASE and UNKNOWN do not transform

    @Test
    public void testTransform5() throws Exception {
        String input = "foobar";
        String result = transform(input, Style.values().length - DUMMY_STYLES + 1);
        Assert.assertEquals(input, result);
    }
    @Test
    public void testTransform() throws Exception {
        String input = "fooBar";
        String result = transform(input, Style.values().length - DUMMY_STYLES);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testTransform2() throws Exception {
        String input = "\"foo bar\"";
        String result = transform(input, Style.values().length - DUMMY_STYLES - 1);
        Assert.assertEquals(input, result);
    }


    @Test
    public void testTransform4() throws Exception {
        String input = "\"Foo Bar\"";
        String result = transform(input, Style.values().length - DUMMY_STYLES - 1);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testTransform3() throws Exception {
        String input = "foo Bar";
        String result = transform(input, Style.values().length -DUMMY_STYLES+1 );
        Assert.assertEquals("fooBar", result);
    }

    @Test
    public void testTransformVariations() throws Exception {
        List<String> failed = new ArrayList<String>();
        for (Style expectedStyle : Style.values()) {
            for (Style inputStyle : Style.values()) {
                for (String inputExample : inputStyle.example) {
                    String transform = expectedStyle.transform(inputStyle, inputExample);
                    Style actualStyle = Style.from(transform);
                    if (expectedStyle.example.length == 0) {
                        continue;
                    }
                    boolean doesNotMatch = !expectedStyle.example[0].equals(transform);

                    if (actualStyle != expectedStyle) {
                        failed.add(inputStyle.name() + " -> " + expectedStyle.name() + "(actual " + actualStyle.name() + "): " + inputExample
                                + " -> " + transform);
                    } else if (doesNotMatch) {
                        failed.add(inputStyle.name() + " -> " + expectedStyle.name() + ": " + inputExample + " -> " + transform);
                    } else {
                        System.out.println(inputStyle.name() + " -> " + expectedStyle.name() + ": " + inputExample + " -> " + transform);
                    }
                }
            }
        }

        failed= allUpperCaseException(failed);
        for (String s : failed) {
            System.err.println(s);
        }
        
        Assert.assertTrue(failed.isEmpty());
    }

    private List<String> allUpperCaseException(List<String> failed) {
        List<String> strings = new ArrayList<String>();
        for (String s : failed) {
            if (s.contains("-> ALL_UPPER_CASE(")) {
               //no converting to that
            } else if (s.contains("ALL_UPPER_CASE ->")) {
                //it will be always shit, lets see it at least
                System.err.println(s);
            } else {
                strings.add(s);
            }
        }
        return strings;
    }

    private String transform(String fooBar, final int i1) {
        String result = null;
        String input = fooBar;
        for (int i = 0; i < i1; i++) {
            result = new SwitchStyleAction(false).transform(input);
            System.out.println(input + " -> " + result);
            Assert.assertNotEquals(input + " -> " + result, input, result);
            input = result;
        }
        return result;
    }
}
