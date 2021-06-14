package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToPascalCaseAction extends AbstractCaseConvertingAction {
    public ToPascalCaseAction() {
    }

    public ToPascalCaseAction(boolean setupHandler) {
        super(setupHandler);
    }

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
        return Style.PASCAL_CASE.transform(s);
    }

}
