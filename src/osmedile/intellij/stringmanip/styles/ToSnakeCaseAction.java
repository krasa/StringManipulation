package osmedile.intellij.stringmanip.styles;

public class ToSnakeCaseAction extends AbstractCaseConvertingAction {
	public ToSnakeCaseAction() {
	}

	public ToSnakeCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	public String transform(String s) {
		Style from = Style.from(s);
		if (from == Style.UNDERSCORE_LOWERCASE) {
			return Style.CAMEL_CASE.transform(from, s);
		} else {
			return Style.UNDERSCORE_LOWERCASE.transform(from, s);
		}
	}

}
