package osmedile.intellij.stringmanip.filter;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class TrimAction extends AbstractStringManipAction<Object> {

	@Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        return s == null ? null : s.trim();
    }
}