package osmedile.intellij.stringmanip.styles;

public class ToSnakeCaseAction extends AbstractCaseConvertingAction {
	public ToSnakeCaseAction() {
	}

	public ToSnakeCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);
		if (from == Style.SNAKE_CASE) {
			return Style.CAMEL_CASE.transform(from, s);
		} else {
			return Style.SNAKE_CASE.transform(from, s);
		}
	}

}
