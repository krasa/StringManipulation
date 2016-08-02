package osmedile.intellij.stringmanip.filter;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

/**
 * @author Olivier Smedile
 * @version $Id: TrimAllSpacesAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class TrimAllSpacesAction extends AbstractStringManipAction {

	@Override
    public String transformByLine(String s) {
        return StringUtil.trimAllSpace(s);
    }
}