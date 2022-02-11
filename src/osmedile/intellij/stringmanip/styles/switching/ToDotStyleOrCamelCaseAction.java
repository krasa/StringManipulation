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
	protected Style[] supportedStyles() {
		return new Style[]{Style.DOT, Style.CAMEL_CASE};
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getFirstStyle(actionContext, s);
		if (from == Style.DOT) {
			return Style.CAMEL_CASE.transform(s);
		} else {
			return Style.DOT.transform(s);
		}
	}

}
