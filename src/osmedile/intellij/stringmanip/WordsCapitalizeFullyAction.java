package osmedile.intellij.stringmanip;

import org.apache.commons.lang.WordUtils;

public class WordsCapitalizeFullyAction extends AbstractStringManipAction {

	public String transform(String s) {
		return WordUtils.capitalizeFully(s);
	}
}
