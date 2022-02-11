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
	protected Style[] supportedStyles() {
		return new Style[]{Style.PASCAL_CASE, Style.CAMEL_CASE, Style._SINGLE_WORD_CAPITALIZED};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from == Style.PASCAL_CASE || from == Style._SINGLE_WORD_CAPITALIZED) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.PASCAL_CASE.transform(s);
		}
	}

}
