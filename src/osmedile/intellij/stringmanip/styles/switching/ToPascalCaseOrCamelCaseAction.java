package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToPascalCaseOrCamelCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToPascalCaseOrCamelCaseAction() {
	}

	public ToPascalCaseOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (containsAnyMainStyleExcept(Style.PASCAL_CASE, actionContext)) {
			return Style.PASCAL_CASE.transform(s);
		} else if (contains(Style.PASCAL_CASE, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.PASCAL_CASE.transform(s);
		}
	}

}
