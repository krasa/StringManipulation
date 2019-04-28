package osmedile.intellij.stringmanip.styles;

public class ToSentenceCaseOrCamelCaseAction extends AbstractCaseConvertingAction {
	public ToSentenceCaseOrCamelCaseAction() {
	}

	public ToSentenceCaseOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);
		if (from == Style.SENTENCE_CASE || from == Style._SINGLE_WORD_CAPITALIZED) {
			return Style.CAMEL_CASE.transform(from, s);
		} else {
			return Style.SENTENCE_CASE.transform(from, s);
		}
	}
}
