package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToWordLowercaseOrToCamelCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToWordLowercaseOrToCamelCaseAction() {
	}

	public ToWordLowercaseOrToCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}


	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (containsAnyMainStyleExcept(Style.WORD_LOWERCASE, actionContext)) {
			return Style.WORD_LOWERCASE.transform(s);
		} else if (contains(Style.WORD_LOWERCASE, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.WORD_LOWERCASE.transform(s);
		}
	}
}
