package osmedile.intellij.stringmanip.swap;

import org.apache.commons.lang3.StringUtils;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.swap.split.Splitter;
import osmedile.intellij.stringmanip.swap.split.Token;
import osmedile.intellij.stringmanip.swap.split.Type;

import java.util.ArrayList;
import java.util.List;


public class Swapper {

	private final List<Token> tokens;
	private final String selectedText;
	private boolean swapCamel;

	public Swapper(String selectedText, boolean splitByCase) {
		this.selectedText = selectedText;
		Splitter splitter = new Splitter(this.selectedText.toCharArray(), splitByCase, false);
		if (splitter.spacesBetweenWords > 0) {
			splitter = new Splitter(this.selectedText.toCharArray(), splitByCase, true);
		}

		if (!splitByCase && onlyOneWord(splitter.tokens)) {
			tokens = splitByCase(splitter.tokens, false);
		} else {
			tokens = splitter.tokens;
		}
	}


	private List<Token> splitByCase(List<Token> originalTokens, boolean splitOnlyByWhitespace) {
		List<Token> newTokens = new ArrayList<>();
		for (int i = 0; i < originalTokens.size(); i++) {
			Token token = originalTokens.get(i);
			if (token.type == Type.WORD) {
				if (Style.from(token.content) == Style.CAMEL_CASE) {
					swapCamel = true;
				}
				List<Token> tokens = new Splitter(token.content.toCharArray(), true, splitOnlyByWhitespace).tokens;
				newTokens.addAll(tokens);
			} else {
				newTokens.add(token);
			}
		}
		return newTokens;
	}

	private boolean onlyOneWord(List<Token> tokens) {
		int word = 0;
		for (Token token : tokens) {
			if (token.type == Type.WORD) {
				word++;
			}
		}

		return word == 1;
	}

	public String swap() {
		return swapInternal();
	}

	public String swapCamel() {
		swapCamel = true;
		return swapInternal();
	}

	private String swapInternal() {
		List<String> result = new ArrayList<>();

		Token nextWord = null;
		for (int i = tokens.size() - 1; i >= 0; i--) {
			Token token = tokens.get(i);
			if (token.type == Type.WORD) {
				nextWord = token;
				break;
			}
		}

		if (nextWord == null) {
			return selectedText;
		}

		if (swapCamel) {
			nextWord.lowerCaseFirst();
		}

		for (Token token : tokens) {
			if (token.type == Type.WORD) {
				result.add(nextWord.content);
				nextWord = token;
				if (swapCamel) {
					nextWord.capitalize();
				}
			} else {
				result.add(token.content);
			}
		}

		return StringUtils.join(result, "");
	}
}