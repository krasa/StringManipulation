package osmedile.intellij.stringmanip.styles;

public class CamelCaseToHyphenLowerCaseAction extends AbstractCaseConvertingAction {
	public CamelCaseToHyphenLowerCaseAction() {
	}

	public CamelCaseToHyphenLowerCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);
		if (from == Style.HYPHEN_LOWERCASE) {
			return Style.CAMEL_CASE.transform(from, s);
		} else {
			return Style.HYPHEN_LOWERCASE.transform(from, s);
		}
	}
}
