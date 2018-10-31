package osmedile.intellij.stringmanip.styles;

public class ToCamelCaseAction extends AbstractCaseConvertingAction {
	public ToCamelCaseAction() {
	}

	public ToCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);
        return Style.WORD_CAPITALIZED.transform(from, s);
	}
}
