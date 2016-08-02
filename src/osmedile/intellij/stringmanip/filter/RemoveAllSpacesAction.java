package osmedile.intellij.stringmanip.filter;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

/**
 * @author Olivier Smedile
 * @version $Id: RemoveAllSpacesAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class RemoveAllSpacesAction extends AbstractStringManipAction {

    public String transformByLine(String s) {
        return StringUtil.removeAllSpace(s);
    }
}