package osmedile.intellij.stringmanip.styles;

import org.apache.commons.lang.WordUtils;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class WordsCapitalizeFullyAction extends AbstractStringManipAction {

    public String transform(String s) {
        return WordUtils.capitalizeFully(s);
    }
}
