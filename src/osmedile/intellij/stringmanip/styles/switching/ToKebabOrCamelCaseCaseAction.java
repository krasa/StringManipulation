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
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (containsAnyMainStyleExcept(Style.KEBAB_LOWERCASE, actionContext)) {
			return Style.KEBAB_LOWERCASE.transform(s);
		} else if (contains(Style.KEBAB_LOWERCASE, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.KEBAB_LOWERCASE.transform(s);
		}
	}
}
