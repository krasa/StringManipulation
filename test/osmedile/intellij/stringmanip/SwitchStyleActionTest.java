package osmedile.intellij.stringmanip;

import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.SwitchStyleAction;

import java.util.ArrayList;
import java.util.List;

public class SwitchStyleActionTest {

    @Test
    public void testTransform() throws Exception {
        String input = "fooBar";
        String result = transform(input, Style.values().length - 1);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testTransformVariations() throws Exception {
        List<String> failed = new ArrayList<String>();
        for (Style style : Style.values()) {
            for (Style style1 : Style.values()) {
                for (String s : style1.example) {
                    String transform = style.transform(style1, s);
                    Style style2 = Style.from(transform);
                    if (style.example.length == 0) {
                        continue;
                    }
                    boolean doesNotMatch = !style.example[0].equals(transform);

                    if (style2 != style) {
                        failed.add(style1.name() + " -> " + style.name() + "(actual " + style2.name() + "): " + s
                                + " -> " + transform);

                    } else if (doesNotMatch) {
                        failed.add(style1.name() + " -> " + style.name() + ": " + s + " -> " + transform);
                    } else {
                        System.out.println(style1.name() + " -> " + style.name() + ": " + s + " -> " + transform);
                    }
                }
            }
        }

        for (String s : failed) {
            System.err.println(s);
        }
        Assert.assertTrue(failed.isEmpty());
    }

    private String transform(String fooBar, final int i1) {
        String result = fooBar;
        for (int i = 0; i < i1; i++) {
            String fooBar1 = fooBar;
            fooBar = new SwitchStyleAction(false).transform(fooBar);
            System.out.println(fooBar1 + " -> " + fooBar);
            result = fooBar;
        }
        return result;
    }
}
