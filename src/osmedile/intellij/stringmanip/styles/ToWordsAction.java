package osmedile.intellij.stringmanip.styles;

public class ToWordsAction extends AbstractCaseConvertingAction {
	public ToWordsAction() {
	}

	public ToWordsAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);
        return Style.WORD_CAPITALIZED.transform(from, s);
	}
}
