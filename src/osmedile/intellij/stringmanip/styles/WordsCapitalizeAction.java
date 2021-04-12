package osmedile.intellij.stringmanip.styles;


import org.apache.commons.text.WordUtils;

import java.util.Map;

public class WordsCapitalizeAction extends AbstractCaseConvertingAction {
	public WordsCapitalizeAction() {
	}

	public WordsCapitalizeAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		return WordUtils.capitalizeFully(s, new char[]{' ', '.'});
	}
}
