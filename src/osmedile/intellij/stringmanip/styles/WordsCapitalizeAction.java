package osmedile.intellij.stringmanip.styles;


import org.apache.commons.text.WordUtils;

public class WordsCapitalizeAction extends AbstractCaseConvertingAction {
	public WordsCapitalizeAction() {
	}

	public WordsCapitalizeAction(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(String s) {
		return WordUtils.capitalizeFully(s);
	}
}
