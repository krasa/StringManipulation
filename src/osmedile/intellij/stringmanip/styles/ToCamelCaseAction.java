package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.StringUtil;

/**
 * Action which trim selected text.
 *
 * @author Olivier Smedile
 * @version $Id: ToCamelCaseAction.java 31 2008-03-22 10:17:44Z osmedile $
 */
public class ToCamelCaseAction extends AbstractStringManipAction {

    public String transform(String s) {
        if (!s.contains(" ")) {
            return StringUtil.camelToText(s);
        } else {
            return StringUtil.toCamelCase(s);
        }
    }
}