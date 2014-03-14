package osmedile.intellij.stringmanip;

import org.apache.commons.lang.WordUtils;

public class WordsCapitalizeAction extends AbstractStringManipAction {

	public String transform(String s) {
		return WordUtils.capitalize(s);
	}
}
