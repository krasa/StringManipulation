package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToSnakeCaseAction extends AbstractCaseConvertingAction {
	public ToSnakeCaseAction() {
	}

	public ToSnakeCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return Style.SNAKE_CASE.transform(s);
	}
}
