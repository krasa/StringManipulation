package osmedile.intellij.stringmanip;

public class WordsCamelCaseToWordsAction extends AbstractStringManipAction {
	public WordsCamelCaseToWordsAction() {
	}

	public WordsCamelCaseToWordsAction(boolean setupHandler) {
		super(setupHandler);
	}

	public String transform(String s) {
		String res = "";
		for (int i = 0; i < s.length(); i++) {
			Character ch = s.charAt(i);
			if (i!=0 && s.charAt(i-1)!= ' '&& Character.isUpperCase(ch))
				res += " " + Character.toLowerCase(ch);
			else
				res += ch;
		}
		return res;
	}

}
