package osmedile.intellij.stringmanip.styles;

public class ToScreamingSnakeCaseAction extends AbstractCaseConvertingAction {
	public ToScreamingSnakeCaseAction() {
	}

	public ToScreamingSnakeCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);

		if (from == Style.SCREAMING_SNAKE_CASE || from == Style._ALL_UPPER_CASE) {
			return Style.CAMEL_CASE.transform(from, s);
		} else {
			return Style.SCREAMING_SNAKE_CASE.transform(from, s);
		}
	}

}
