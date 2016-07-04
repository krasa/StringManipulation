package osmedile.intellij.stringmanip.styles;

/**
 * Action which trim selected text.
 *
 * @author Olivier Smedile
 * @version $Id: ToCamelCaseAction.java 31 2008-03-22 10:17:44Z osmedile $
 */
public class ToCamelCaseAction extends AbstractCaseConvertingAction {
	public ToCamelCaseAction() {
	}

	public ToCamelCaseAction(boolean setupHandler) {
		super(setupHandler);
	}

	public String transformByLine(String s) {
		Style from = Style.from(s);
		if (from == Style.CAMEL_CASE) {
			return Style.WORD_CAPITALIZED.transform(from, s);
		} else {
			return Style.CAMEL_CASE.transform(from, s);
		}
	}
}
