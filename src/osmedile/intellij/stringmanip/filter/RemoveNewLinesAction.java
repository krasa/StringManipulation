package osmedile.intellij.stringmanip.filter;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

public class RemoveNewLinesAction extends AbstractStringManipAction {

	public String transformByLine(String s) {
		return s.replaceAll("\n", "");
	}
}