package osmedile.intellij.stringmanip.styles;

import org.apache.commons.lang.StringUtils;

public class SwapCaseAction extends AbstractCaseConvertingAction {
    public SwapCaseAction() {
    }

    public SwapCaseAction(boolean setupHandler) {
        super(setupHandler);
    }

    @Override
    public String transformByLine(String s) {
        return StringUtils.swapCase(s);
    }
}
