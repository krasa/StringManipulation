package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToWordCapitalizedOrCamelCaseAction extends AbstractCaseConvertingAction {
	public ToWordCapitalizedOrCamelCaseAction() {
	}

	public ToWordCapitalizedOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getStyle(actionContext, s);
		if (from == Style.WORD_CAPITALIZED || from == Style._SINGLE_WORD_CAPITALIZED) {
			return Style.CAMEL_CASE.transform( s);
		} else {
			return Style.WORD_CAPITALIZED.transform( s);
		}
	}
}
