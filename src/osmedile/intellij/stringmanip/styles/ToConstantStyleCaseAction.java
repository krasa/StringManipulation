package osmedile.intellij.stringmanip.styles;

public class ToConstantStyleCaseAction extends AbstractCaseConvertingAction {
	public ToConstantStyleCaseAction() {
	}

	public ToConstantStyleCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	public String transformByLine(String s) {
		Style from = Style.from(s);
		if (from == Style.SCREAMING_SNAKE_CASE) {
			return Style.CAMEL_CASE.transform(from, s);
		} else {
			return Style.SCREAMING_SNAKE_CASE.transform(from, s);
		}
	}

}
