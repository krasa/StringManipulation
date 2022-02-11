package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToSnakeCaseOrCamelCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToSnakeCaseOrCamelCaseAction() {
	}

	public ToSnakeCaseOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}


	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (containsAnythingBut(Style.SNAKE_CASE, actionContext)) {
			return Style.SNAKE_CASE.transform(s);
		} else if (contains(Style.SNAKE_CASE, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.SNAKE_CASE.transform(s);
		}
	}

}
