package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToSentenceCaseOrCamelCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToSentenceCaseOrCamelCaseAction() {
	}

	public ToSentenceCaseOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	protected Style[] supportedStyles() {
		return new Style[]{Style.SENTENCE_CASE, Style.CAMEL_CASE, Style._SINGLE_WORD_CAPITALIZED};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from == Style.SENTENCE_CASE || from == Style._SINGLE_WORD_CAPITALIZED) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.SENTENCE_CASE.transform(s);
		}
	}
}
