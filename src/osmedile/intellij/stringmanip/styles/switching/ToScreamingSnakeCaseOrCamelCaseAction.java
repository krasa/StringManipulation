package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToScreamingSnakeCaseOrCamelCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToScreamingSnakeCaseOrCamelCaseAction() {
	}

	public ToScreamingSnakeCaseOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (contains(Style.CAMEL_CASE, actionContext)) {
			return Style.SCREAMING_SNAKE_CASE.transform(s);
		} else if (contains(Style.SCREAMING_SNAKE_CASE, actionContext) || contains(Style._ALL_UPPER_CASE, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.SCREAMING_SNAKE_CASE.transform(s);
		}
	}

}
