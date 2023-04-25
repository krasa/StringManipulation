package osmedile.intellij.stringmanip.styles;


import java.util.Map;

public class ToLowerCaseAction extends AbstractCaseConvertingAction {
	public ToLowerCaseAction() {
	}

	public ToLowerCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return s.toLowerCase();
	}
}
