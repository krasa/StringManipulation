package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class CamelCaseToHyphenLowerCaseAction extends AbstractCaseConvertingAction {
	public CamelCaseToHyphenLowerCaseAction() {
	}

	public CamelCaseToHyphenLowerCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getStyle(actionContext, s);
		if (from == Style.KEBAB_LOWERCASE) {
			return Style.CAMEL_CASE.transform( s);
		} else {
			return Style.KEBAB_LOWERCASE.transform( s);
		}
	}
}
