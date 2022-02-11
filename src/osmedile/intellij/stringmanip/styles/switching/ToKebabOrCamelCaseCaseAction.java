package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToKebabOrCamelCaseCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToKebabOrCamelCaseCaseAction() {
	}

	public ToKebabOrCamelCaseCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	protected Style[] supportedStyles() {
		return new Style[]{Style.KEBAB_LOWERCASE, Style.CAMEL_CASE};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from == Style.KEBAB_LOWERCASE) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.KEBAB_LOWERCASE.transform(s);
		}
	}
}
