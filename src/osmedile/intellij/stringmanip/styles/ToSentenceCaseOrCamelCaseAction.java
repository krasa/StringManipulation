package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToSentenceCaseOrCamelCaseAction extends AbstractCaseConvertingAction {
	public ToSentenceCaseOrCamelCaseAction() {
	}

	public ToSentenceCaseOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getStyle(actionContext, s);
		if (from == Style.SENTENCE_CASE || from == Style._SINGLE_WORD_CAPITALIZED) {
			return Style.CAMEL_CASE.transform( s);
		} else {
			return Style.SENTENCE_CASE.transform( s);
		}
	}
}
