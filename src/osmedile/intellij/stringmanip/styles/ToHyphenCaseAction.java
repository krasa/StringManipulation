package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToHyphenCaseAction extends AbstractCaseConvertingAction {

	public ToHyphenCaseAction() {
	}

	public ToHyphenCaseAction(boolean b) {
		super(b);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getStyle(actionContext, s);
		if (from == Style.KEBAB_LOWERCASE) {
			return Style.SNAKE_CASE.transform( s);
		}
		return Style.KEBAB_LOWERCASE.transform( s);
	}

}
