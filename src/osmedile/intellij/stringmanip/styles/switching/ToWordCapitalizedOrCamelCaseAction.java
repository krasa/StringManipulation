package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToWordCapitalizedOrCamelCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToWordCapitalizedOrCamelCaseAction() {
	}

	public ToWordCapitalizedOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (containsAnythingBut(Style.WORD_CAPITALIZED, actionContext)) {
			return Style.WORD_CAPITALIZED.transform(s);
		} else if (contains(Style.WORD_CAPITALIZED, actionContext) || contains(Style._SINGLE_WORD_CAPITALIZED, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.WORD_CAPITALIZED.transform(s);
		}
	}
}
