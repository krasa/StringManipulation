package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToScreamingSnakeCaseAction extends AbstractCaseConvertingAction {
	public ToScreamingSnakeCaseAction() {
	}

	public ToScreamingSnakeCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return Style.SCREAMING_SNAKE_CASE.transform(s);
	}
}
