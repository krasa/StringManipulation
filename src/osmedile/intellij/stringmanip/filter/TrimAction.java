package osmedile.intellij.stringmanip.filter;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

/**
 * Action which trim selected text.
 *
 * @author Olivier Smedile
 * @version $Id: TrimAction.java 19 2008-03-20 19:55:59Z osmedile $
 */
public class TrimAction extends AbstractStringManipAction {

	@Override
    public String transformByLine(String s) {
        return s == null ? null : s.trim();
    }
}