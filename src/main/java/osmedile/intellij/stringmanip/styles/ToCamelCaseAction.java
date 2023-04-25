package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToCamelCaseAction extends AbstractCaseConvertingAction {
	public ToCamelCaseAction() {
	}

	public ToCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		if (Style.from(s) != Style.CAMEL_CASE) {
			return Style.CAMEL_CASE.transform(s);
		}
		return s;
	}
}
