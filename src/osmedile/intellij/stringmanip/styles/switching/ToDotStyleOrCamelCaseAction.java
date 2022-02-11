package osmedile.intellij.stringmanip.styles.switching;

import osmedile.intellij.stringmanip.styles.Style;

import java.util.Map;

public class ToDotStyleOrCamelCaseAction extends AbstractSwitchingCaseConvertingAction {
	public ToDotStyleOrCamelCaseAction() {
	}

	public ToDotStyleOrCamelCaseAction(boolean b) {
		super(b);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (contains(Style.CAMEL_CASE, actionContext)) {
			return Style.DOT.transform(s);
		} else if (contains(Style.DOT, actionContext)) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.DOT.transform(s);
		}
	}

}
