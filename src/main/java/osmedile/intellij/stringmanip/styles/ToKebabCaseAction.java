package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToKebabCaseAction extends AbstractCaseConvertingAction {
	public ToKebabCaseAction() {
	}

	public ToKebabCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return Style.KEBAB_LOWERCASE.transform(s);
	}
}
