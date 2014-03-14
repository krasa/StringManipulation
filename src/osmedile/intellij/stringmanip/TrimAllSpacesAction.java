package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.utils.StringUtil;

/**
 * @author Olivier Smedile
 * @version $Id: TrimAllSpacesAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class TrimAllSpacesAction extends AbstractStringManipAction {

    public String transform(String s) {
        return StringUtil.trimAllSpace(s);
    }
}