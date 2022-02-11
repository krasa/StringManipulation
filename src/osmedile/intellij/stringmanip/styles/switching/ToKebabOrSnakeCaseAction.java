package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToKebabOrSnakeCaseAction extends AbstractSwitchingCaseConvertingAction {

	public ToKebabOrSnakeCaseAction() {
	}

	public ToKebabOrSnakeCaseAction(boolean b) {
		super(b);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (contains(Style.SNAKE_CASE, actionContext)) {
			return Style.KEBAB_LOWERCASE.transform(s);
		} else if (contains(Style.KEBAB_LOWERCASE, actionContext)) {
			return Style.SNAKE_CASE.transform(s);
		} else {
			return Style.KEBAB_LOWERCASE.transform(s);
		}
	}

}
