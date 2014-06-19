package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class ToDotStyleAction extends AbstractStringManipAction {

    public String transform(String s) {
        Style from = Style.from(s);
        if (from == Style.DOT) {
            return Style.CAMEL_CASE.transform(from, s);
        } else {
            return Style.DOT.transform(from, s);
        } 
    }

}
