package osmedile.intellij.stringmanip.swap.split;

import java.util.ArrayList;
import java.util.List;

public class Splitter {

	StringBuilder temp = new StringBuilder();
	Type lastType = null;
	public List<Token> tokens = new ArrayList<>();

	public Splitter(char[] charArray, boolean splitByCase) {
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (Character.isWhitespace(c)) {
				process(c, Type.SPACE);
			} else if (Character.isLetterOrDigit(c)) {
				if (splitByCase) {
					if (Character.isUpperCase(c)) {
						addToken();
					}
				}
				process(c, Type.WORD);
			} else {
				process(c, Type.DELIMITER);
			}
		}

		if (temp.length() > 0) {
			tokens.add(new Token(lastType, temp.toString()));
		}


	}


	private void process(char c, Type type) {
		if (lastType != type) {
			addToken();
		}

		temp.append(c);
		lastType = type;
	}

	private void addToken() {
		if (temp.length() > 0) {
			tokens.add(new Token(lastType, temp.toString()));
			temp.setLength(0);
		}
	}

}
