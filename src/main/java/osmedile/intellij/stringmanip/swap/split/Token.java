package osmedile.intellij.stringmanip.swap.split;

public class Token {
	public String content;
	public Type type;

	public Token(Type type, String content) {
		this.content = content;
		this.type = type;
	}

	public void lowerCaseFirst() {
		content = lowerCaseFirstLetter(content);
	}

	public void capitalize() {
		content = capitalizeFirstLetter(content);
	}

	public static String capitalizeFirstLetter(String data) {
		char firstLetter = Character.toTitleCase(data.substring(0, 1).charAt(0));
		String restLetters = data.substring(1);
		return firstLetter + restLetters;
	}

	public static String lowerCaseFirstLetter(String data) {
		char firstLetter = Character.toLowerCase(data.substring(0, 1).charAt(0));
		String restLetters = data.substring(1);
		return firstLetter + restLetters;
	}

	@Override
	public String toString() {
		return "Token{" +
				"content='" + content + '\'' +
				", type=" + type +
				'}';
	}

	public void reprocess() {
		int i = 0;
		if (type == Type.WORD) {
			for (char c : content.toCharArray()) {
				if (Character.isLetterOrDigit(c)) {
					i++;
					break;
				}
			}
			if (i == 0) {
				type = Type.DELIMITER;
			}
		}
	}
}
