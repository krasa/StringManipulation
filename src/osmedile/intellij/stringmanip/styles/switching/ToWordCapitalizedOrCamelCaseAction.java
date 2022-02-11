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
	protected Style[] supportedStyles() {
		return new Style[]{Style.WORD_CAPITALIZED, Style._SINGLE_WORD_CAPITALIZED, Style.CAMEL_CASE};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from == Style.WORD_CAPITALIZED || from == Style._SINGLE_WORD_CAPITALIZED) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.WORD_CAPITALIZED.transform(s);
		}
	}
}
