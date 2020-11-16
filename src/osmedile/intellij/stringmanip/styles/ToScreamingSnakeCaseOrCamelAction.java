package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToScreamingSnakeCaseOrCamelAction extends AbstractCaseConvertingAction {
	public ToScreamingSnakeCaseOrCamelAction() {
	}

	public ToScreamingSnakeCaseOrCamelAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getStyle(actionContext, s);

		if (from == Style.SCREAMING_SNAKE_CASE || from == Style._ALL_UPPER_CASE) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.SCREAMING_SNAKE_CASE.transform(s);
		}
	}

}
