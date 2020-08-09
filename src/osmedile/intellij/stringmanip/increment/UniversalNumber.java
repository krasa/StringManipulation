package osmedile.intellij.stringmanip.increment;

import org.apache.commons.lang3.ArrayUtils;

import static java.lang.Character.isDigit;

public class UniversalNumber {
	public static final String UNIVERSAL_NUMBER_REGEX = "[+-]?\\d+([\\., ]\\d+)*";

	char[] chars;
	private UniversalNumberSeparator separator;

	public static String increment(String s) {
		return new UniversalNumber(s).increment();
	}

	public UniversalNumber(String textPart) {
		chars = textPart.toCharArray();
		separator = new UniversalNumberSeparator(this);
	}

	public String increment() {
		if (isNegative()) {
			shiftDown(chars.length - 1);
		} else {
			shiftUp(chars.length - 1);
		}
		if (isZero() && isNegative()) {
			chars = ArrayUtils.remove(chars, 0);
		}
		return new String(chars);
	}


	public String decrement() {
		if (isZero() || isNegative()) {
			shiftUp(chars.length - 1);
			if (!isNegative()) {
				if (chars[0] == '+') {
					chars[0] = '-';
				} else {
					chars = ArrayUtils.add(chars, 0, '-');
				}
			}
		} else {
			shiftDown(chars.length - 1);
		}
		return new String(chars);
	}

	private void shiftUp(int start) {
		if (start == -1) {
			addExtraPlace(0);
			return;
		}
		for (int i = start; i >= 0; i--) {
			char aChar = chars[i];
			if (isDigit(aChar)) {
				int num = Character.getNumericValue(aChar);
				chars[i] = Character.forDigit((num + 1) % 10, 10);
				if (num == 9) {
					shiftUp(i - 1);
					break;
				}
				break;
			} else if (i == 0 && aChar == '-') {
				addExtraPlace(1);
				break;
			}
		}
	}

	//todo what is the proper name?
	private void addExtraPlace(int index) {
		if (separator.needSeparator()) {
			Character character = separator.guessSeparator();
			if (character != null) {
				chars = ArrayUtils.add(chars, index, character);
			}
		}
		chars = ArrayUtils.add(chars, index, '1');
	}


	private void shiftDown(int start) {
		for (int i = start; i >= 0; i--) {
			char aChar = chars[i];
			if (isDigit(aChar)) {
				int num = Character.getNumericValue(aChar);
				if (canShiftDownNext(i)) {
					chars[i] = Character.forDigit(num == 0 ? 9 : num - 1, 10);
					if (num == 0) {
						shiftDown(i - 1);
						break;
					}
				} else {
					if (num - 1 == 0 && canRemove(i)) {
						chars = ArrayUtils.remove(chars, i);
						if (separator.canRemoveSeparator(i)) {
							chars = ArrayUtils.remove(chars, i);
						}
					} else {
						chars[i] = Character.forDigit(num - 1, 10);
					}
				}
				break;
			}
		}
	}


	private boolean canRemove(int i) {
		for (int j = i + 1; j < chars.length; j++) {
			char aChar = chars[j];
			if (isDigit(aChar)) {
				return true;
			} else if (separator.canRemoveSeparator(j)) {
				continue;
			} else {
				return false;
			}
		}
		return false;
	}

	private boolean canShiftDownNext(int i) {
		for (int j = i - 1; j >= 0; j--) {
			char aChar = chars[j];
			if (isDigit(aChar)) {
				return true;
			}
		}
		return false;
	}

	private boolean isNegative() {
		return chars[0] == '-';
	}

	private boolean isZero() {
		boolean zero = false;
		for (int i = 0; i < chars.length; i++) {
			char aChar = chars[i];
			if (isDigit(aChar)) {
				int num = Character.getNumericValue(aChar);
				if (num != 0) {
					return false;
				} else {
					zero = true;
				}
			}
		}
		return zero;
	}
}
