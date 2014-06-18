package osmedile.intellij.stringmanip.styles;

import org.apache.commons.lang.WordUtils;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class WordsCapitalizeAction extends AbstractStringManipAction {

    public String transform(String s) {
        return WordUtils.capitalize(s);
    }
}
