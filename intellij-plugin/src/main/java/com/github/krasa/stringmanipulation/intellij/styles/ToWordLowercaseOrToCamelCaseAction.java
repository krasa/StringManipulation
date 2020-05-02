package com.github.krasa.stringmanipulation.intellij.styles;

import com.github.krasa.stringmanipulation.utils.style.Style;

public class ToWordLowercaseOrToCamelCaseAction extends AbstractCaseConvertingAction {
    public ToWordLowercaseOrToCamelCaseAction() {
    }

    public ToWordLowercaseOrToCamelCaseAction(boolean setupHandler) {
        super(setupHandler);
    }

    @Override
    public String transformByLine(String s) {
        Style from = Style.from(s);
        if (from != Style.WORD_LOWERCASE) {
            return Style.WORD_LOWERCASE.transform(from, s);
        } else {
            return Style.CAMEL_CASE.transform(from, s);
        }
    }
}
