package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.utils.StringUtil;

/**
 * Created by ykoh on 15. 2. 2..
 */
public class RemoveNonAlphaCharsAndToUnderscoreAction extends AbstractStringManipAction {
    public String transform(String s) {
        return StringUtil.wordsToUnderscoreCase(s);
    }
}
