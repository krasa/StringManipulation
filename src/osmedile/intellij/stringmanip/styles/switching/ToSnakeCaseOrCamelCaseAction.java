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
	protected Style[] supportedStyles() {
		return new Style[]{Style.SNAKE_CASE, Style.CAMEL_CASE};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from == Style.SNAKE_CASE) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.SNAKE_CASE.transform(s);
		}
	}

}
