package osmedile.intellij.stringmanip.styles;

public class ToHyphenCaseAction extends AbstractCaseConvertingAction {

	public ToHyphenCaseAction() {
	}

	public ToHyphenCaseAction(boolean b) {
		super(b);
	}

	@Override
	public String transformByLine(String s) {
		Style from = Style.from(s);
		if (from == Style.HYPHEN_LOWERCASE) {
			return Style.UNDERSCORE_LOWERCASE.transform(from, s);
		}
		return Style.HYPHEN_LOWERCASE.transform(from, s);
	}

}
