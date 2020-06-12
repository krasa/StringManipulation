package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToWordLowercaseOrToCamelCaseAction extends AbstractCaseConvertingAction {
    public ToWordLowercaseOrToCamelCaseAction() {
    }

    public ToWordLowercaseOrToCamelCaseAction(boolean setupHandler) {
        super(setupHandler);
    }

    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        Style from = getStyle(actionContext, s);
        if (from != Style.WORD_LOWERCASE) {
            return Style.WORD_LOWERCASE.transform( s);
        } else {
            return Style.CAMEL_CASE.transform( s);
        }
    }
}
