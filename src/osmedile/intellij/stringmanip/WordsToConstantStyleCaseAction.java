package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.utils.StringUtil;

/**
 * Action which trim selected text.
 *
 * @author Olivier Smedile
 * @version $Id: WordsToConstantStyleCaseAction.java 43 2008-03-30 11:48:31Z osmedile $
 */
public class WordsToConstantStyleCaseAction extends AbstractStringManipAction {

    public String transform(String s) {
        return StringUtil.wordsToConstantCase(s);
    }
}