package osmedile.intellij.stringmanip.unused;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.Map;

public class WordsToConstantStyleCaseAction extends AbstractStringManipAction<Object> {

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
        return StringUtil.wordsToConstantCase(s);
    }
}