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
	protected Style[] supportedStyles() {
		return new Style[]{Style.SCREAMING_SNAKE_CASE, Style._ALL_UPPER_CASE, Style.CAMEL_CASE};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);

		if (from == Style.SCREAMING_SNAKE_CASE || from == Style._ALL_UPPER_CASE) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.SCREAMING_SNAKE_CASE.transform(s);
		}
	}

}
