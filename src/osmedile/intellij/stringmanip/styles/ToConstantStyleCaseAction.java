package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class ToConstantStyleCaseAction extends AbstractStringManipAction {
    public ToConstantStyleCaseAction() {
    }

    public ToConstantStyleCaseAction(boolean setupHandler) {
        super(setupHandler);
    }

    public String transform(String s) {
        Style from = Style.from(s);
        if (from == Style.UNDERSCORE_UPPERCASE) {
            return Style.CAMEL_CASE.transform(from, s);
        } else {
            return Style.UNDERSCORE_UPPERCASE.transform(from, s);
        }
    }

}
