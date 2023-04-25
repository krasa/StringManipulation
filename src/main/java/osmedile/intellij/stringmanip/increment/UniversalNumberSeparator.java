package osmedile.intellij.stringmanip.increment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;

public class UniversalNumberSeparator {

	private final List<Character> separators;
	private UniversalNumber parent;

	public UniversalNumberSeparator(UniversalNumber parent) {
		this.parent = parent;
		separators = getSeparators();
	}

	Character guessSeparator() {
		if (separators.contains(' ')) {
			return ' ';
		}
		if (separators.size() > 1 && separators.get(0) == '.' && separators.get(separators.size() - 1) == ',') {
			return '.';
		}
		return null;
	}

	private List<Character> getSeparators() {
		int digits = 0;
		List<Character> separators = new ArrayList<>();

		for (int i = 0; i < parent.chars.length; i++) {
			char aChar = parent.chars[i];
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
		return getConsecutiveDigits(separator, 0) == 3;
	}

	@NotNull
	Integer getConsecutiveDigits(Character separator, int from) {
		if (separator == null) return -1;

		int consecutiveDigits = 0;
		for (int i = from; i < parent.chars.length; i++) {
			char aChar = parent.chars[i];
			if (isDigit(aChar)) {
				consecutiveDigits++;
			} else if (separator != aChar && !(separator == '.' && aChar == ',')) {
				return -1;
			} else {
				break;
			}
		}

		return consecutiveDigits;
	}

	boolean canRemoveSeparator(int from) {
		//TODO
		Character aChar = parent.chars[from];
		Character guessed = guessSeparator();
		return isSpace(from) || (aChar == '.' && guessed != null && guessed == '.' && getConsecutiveDigits(aChar, from + 1) == 3);
	}

	private boolean isSpace(int i) {
		return parent.chars.length > i && parent.chars[i] == ' ';
	}

}
