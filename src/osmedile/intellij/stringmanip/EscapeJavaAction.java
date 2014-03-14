package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.utils.StringEscapeUtil;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeJavaAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class EscapeJavaAction extends AbstractStringManipAction {

    public String transform(String s) {
        return StringEscapeUtil.escapeJava(s);
    }
}
