package osmedile.intellij.stringmanip.sort.support;

import javax.swing.border.Border;

import com.intellij.ui.components.JBTextField;

public class MyJBTextField extends JBTextField {
	Border myBorder = SortTypeDialog.VALID_BORDER;

	public void setMyBorder(Border myBorder) {
		this.myBorder = myBorder;
	}

	@Override
	public Border getBorder() {
		if (isEnabled()) {
			return myBorder;
		} else {
			return super.getBorder();
		}
	}
}
