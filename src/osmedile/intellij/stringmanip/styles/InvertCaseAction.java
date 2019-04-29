package osmedile.intellij.stringmanip.styles;

import shaded.org.apache.commons.lang3.StringUtils;

public class InvertCaseAction extends AbstractCaseConvertingAction {
    public InvertCaseAction() {
    }

    public InvertCaseAction(boolean setupHandler) {
        super(setupHandler);
    }

    @Override
    public String transformByLine(String s) {
        return StringUtils.swapCase(s);
    }
}
