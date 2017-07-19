package osmedile.intellij.stringmanip.filter;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class TrimAction extends AbstractStringManipAction {

	@Override
    public String transformByLine(String s) {
        return s == null ? null : s.trim();
    }
}