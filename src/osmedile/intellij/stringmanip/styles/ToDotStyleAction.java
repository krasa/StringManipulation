package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToDotStyleAction extends AbstractCaseConvertingAction {
	public ToDotStyleAction() {
	}

	public ToDotStyleAction(boolean b) {
		super(b);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		Style from = getStyle(actionContext, s);
		if (from == Style.DOT) {
			return Style.CAMEL_CASE.transform( s);
		} else {
			return Style.DOT.transform( s);
		}
	}

}
