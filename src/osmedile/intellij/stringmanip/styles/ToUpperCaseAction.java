package osmedile.intellij.stringmanip.styles;

public class ToUpperCaseAction extends AbstractCaseConvertingAction {
	public ToUpperCaseAction() {
	}

	public ToUpperCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		return s.toUpperCase();
	}
}
