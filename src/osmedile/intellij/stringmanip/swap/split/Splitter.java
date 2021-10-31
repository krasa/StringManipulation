package osmedile.intellij.stringmanip.swap.split;

import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Splitter {

	StringBuilder temp = new StringBuilder();
	Type lastType = null;
	public List<Token> tokens = new ArrayList<>();
	public int words = 0;
	private int spaceAfterFirstWord = 0;
	public int spacesBetweenWords = 0;


	public Splitter(char[] charArray, boolean splitByCase, boolean splitOnlyByWhitespace) {
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (Character.isWhitespace(c)) {
				process(c, Type.SPACE);
			} else if (Character.isLetterOrDigit(c) || (splitOnlyByWhitespace && StringUtil.isSeparator(c))) {
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

		for (Token token : tokens) {
			token.reprocess();
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

			switch (lastType) {

				case SPACE:
					if (words != 0) {
						spaceAfterFirstWord++;
					}
					break;
				case DELIMITER:
					break;
				case WORD:
					spacesBetweenWords = spaceAfterFirstWord;
					words++;
					break;
				default:
					throw new RuntimeException(lastType.toString());
			}
			temp.setLength(0);
		}
	}

}
