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
		Style from = getStyle(actionContext, s);
		if (from == Style.SNAKE_CASE) {
			return Style.CAMEL_CASE.transform( s);
		} else {
			return Style.SNAKE_CASE.transform( s);
		}
	}

}
