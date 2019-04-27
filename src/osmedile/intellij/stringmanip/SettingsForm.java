package osmedile.intellij.stringmanip;

import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;

public class SettingsForm {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(SettingsForm.class);

	private JCheckBox separatorBefore;
	private JCheckBox separatorAfter;
	private JPanel root;

	public void setData(CaseSwitchingSettings data) {
		separatorBefore.setSelected(data.isSeparatorBeforeDigit());
		separatorAfter.setSelected(data.isSeparatorAfterDigit());
	}

	public void getData(CaseSwitchingSettings data) {
		data.setSeparatorBeforeDigit(separatorBefore.isSelected());
		data.setSeparatorAfterDigit(separatorAfter.isSelected());
	}

	public boolean isModified(CaseSwitchingSettings data) {
		if (separatorBefore.isSelected() != data.isSeparatorBeforeDigit()) return true;
		if (separatorAfter.isSelected() != data.isSeparatorAfterDigit()) return true;
		return false;
	}

	public JPanel getRoot() {
		return root;
	}
}
