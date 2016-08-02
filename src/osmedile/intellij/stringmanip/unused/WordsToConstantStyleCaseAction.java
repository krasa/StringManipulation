package osmedile.intellij.stringmanip.unused;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

/**
 * Action which trim selected text.
 *
 * @author Olivier Smedile
 * @version $Id: WordsToConstantStyleCaseAction.java 43 2008-03-30 11:48:31Z osmedile $
 */
public class WordsToConstantStyleCaseAction extends AbstractStringManipAction {

	@Override
	public String transformByLine(String s) {
        return StringUtil.wordsToConstantCase(s);
    }
}