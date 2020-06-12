package osmedile.intellij.stringmanip.unused;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class WordsCamelCaseToWordsAction extends AbstractStringManipAction<Object> {
    public WordsCamelCaseToWordsAction() {
    }

    public WordsCamelCaseToWordsAction(boolean setupHandler) {
        super(setupHandler);
    }

	@Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            Character ch = s.charAt(i);
            if (i != 0 && s.charAt(i - 1) != ' ' && Character.isUpperCase(ch))
                res += " " + Character.toLowerCase(ch);
            else
                res += ch;
        }
        return res;
    }

}
