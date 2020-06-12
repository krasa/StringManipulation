package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToUpperCaseAction extends AbstractCaseConvertingAction {
	public ToUpperCaseAction() {
	}

	public ToUpperCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return s.toUpperCase();
	}
}
