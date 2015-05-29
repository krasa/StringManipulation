package osmedile.intellij.stringmanip.styles;

import org.apache.commons.lang.WordUtils;

public class WordsCapitalizeAction extends AbstractCaseConvertingAction {

    public String transform(String s) {
        return WordUtils.capitalize(s);
    }
}
