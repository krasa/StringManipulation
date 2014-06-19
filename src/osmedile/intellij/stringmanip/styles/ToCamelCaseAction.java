package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

/**
 * Action which trim selected text.
 *
 * @author Olivier Smedile
 * @version $Id: ToCamelCaseAction.java 31 2008-03-22 10:17:44Z osmedile $
 */
public class ToCamelCaseAction extends AbstractStringManipAction {

    public String transform(String s) {
        Style from = Style.from(s);
        if (from == Style.CAMEL_CASE) {
            return Style.WORD_CAPITALIZED.transform(from, s);
        } else {
            return Style.CAMEL_CASE.transform(from, s);
        }
    }
}