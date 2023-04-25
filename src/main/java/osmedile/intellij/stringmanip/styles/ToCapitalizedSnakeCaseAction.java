package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToCapitalizedSnakeCaseAction extends AbstractCaseConvertingAction {
	public ToCapitalizedSnakeCaseAction() {
	}

	public ToCapitalizedSnakeCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return Style.CAPITALIZED_SNAKE_CASE.transform(s);
	}
}
