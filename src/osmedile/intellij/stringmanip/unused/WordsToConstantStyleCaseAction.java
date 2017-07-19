package osmedile.intellij.stringmanip.unused;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

public class WordsToConstantStyleCaseAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringUtil.wordsToConstantCase(s);
    }
}