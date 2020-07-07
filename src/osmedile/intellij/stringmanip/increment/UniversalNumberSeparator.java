package osmedile.intellij.stringmanip.increment;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;

public class UniversalNumberSeparator {
	private char[] chars;
	private final List<Character> separators;

	public UniversalNumberSeparator(char[] chars) {
		this.chars = chars;
		separators = getSeparators();
	}

	Character guessSeparator() {
		if (separators.contains(' ')) {
			return ' ';
		}
		return null;
	}

	private List<Character> getSeparators() {
		int digits = 0;
		List<Character> separators = new ArrayList<>();

		for (int i = 0; i < chars.length; i++) {
			char aChar = chars[i];
			if (digits > 0 && !isDigit(aChar)) {
				separators.add(aChar);
			} else if (isDigit(aChar)) {
				digits++;
			}
		}
		return separators;
	}

	boolean needSeparator() {
		Character separator = guessSeparator();
		int consecutiveDigits = 0;
		if (separator != null) {
			for (int i = 0; i < chars.length; i++) {
				char aChar = chars[i];
				if (isDigit(aChar)) {
					consecutiveDigits++;
				} else if (separator != aChar) {
					return false;
				} else {
					break;
				}
			}

		}
		return separator != null && consecutiveDigits == 3;
	}
}
