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
	protected Style[] supportedStyles() {
		return new Style[]{Style.WORD_LOWERCASE, Style.CAMEL_CASE};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from != Style.WORD_LOWERCASE) {
			return Style.WORD_LOWERCASE.transform(s);
		} else {
			return Style.CAMEL_CASE.transform(s);
		}
	}
}
