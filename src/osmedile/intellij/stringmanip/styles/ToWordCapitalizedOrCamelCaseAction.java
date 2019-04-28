package osmedile.intellij.stringmanip.styles;

public class ToWordCapitalizedOrCamelCaseAction extends AbstractCaseConvertingAction {
	public ToWordCapitalizedOrCamelCaseAction() {
	}

	public ToWordCapitalizedOrCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);
		if (from == Style.WORD_CAPITALIZED || from == Style._SINGLE_WORD_CAPITALIZED) {
			return Style.CAMEL_CASE.transform(from, s);
		} else {
			return Style.WORD_CAPITALIZED.transform(from, s);
		}
	}
}
