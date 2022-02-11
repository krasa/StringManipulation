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
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (containsAnythingBut(Style.SENTENCE_CASE, actionContext)) {
			return Style.SENTENCE_CASE.transform(s);
		} else if (contains(Style.SENTENCE_CASE, actionContext) || contains(Style._SINGLE_WORD_CAPITALIZED, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.SENTENCE_CASE.transform(s);
		}
	}
}
