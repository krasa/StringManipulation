package osmedile.intellij.stringmanip;

import org.junit.Test;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.SwitchStyleAction;

public class SwitchStyleActionTest {

    @Test
    public void testTransform() throws Exception {
        transform("fooBar", 10);
    }


    @Test
    public void testTransformVariations() throws Exception {
        for (Style style : Style.values()) {
            for (Style style1 : Style.values()) {
                for (String s : style1.example) {
                    String transform = style.transform(style1, s);
                    Style style2 = Style.from(transform);
                    if (style.example.length==0) {
                        continue;
                    }
                    boolean b1 = !style.example[0].equals(transform);

                    if (style2 != style) {
                        System.err.println(style1.name() + " -> " + style.name() + "(actual " + style2.name() + "): " + s + " -> " + transform);

                    } else if (b1) {
                        System.err.println(style1.name() + " -> " + style.name() + ": " + s + " -> " + transform);
                    } else {
                        System.out.println(style1.name() + " -> " + style.name() + ": " + s + " -> " + transform);
                    }
                }
            }
        }
    }

    private void transform(String fooBar, final int i1) {
        for (int i = 0; i < i1; i++) {
            String fooBar1 = fooBar;
            fooBar = new SwitchStyleAction(false).transform(fooBar);
            System.err.println(fooBar1 + " -> " + fooBar);
        }
    }
}
