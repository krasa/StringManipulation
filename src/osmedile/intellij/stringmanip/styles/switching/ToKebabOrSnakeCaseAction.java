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
	protected Style[] supportedStyles() {
		return new Style[]{Style.KEBAB_LOWERCASE, Style.SNAKE_CASE};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from == Style.KEBAB_LOWERCASE) {
			return Style.SNAKE_CASE.transform(s);
		}
		return Style.KEBAB_LOWERCASE.transform(s);
	}

}
