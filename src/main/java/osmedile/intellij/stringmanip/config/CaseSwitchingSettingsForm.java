package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.diagnostic.Logger;
import osmedile.intellij.stringmanip.CaseSwitchingSettings;

import javax.swing.*;

public class CaseSwitchingSettingsForm {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(CaseSwitchingSettingsForm.class);

	private JCheckBox separatorBefore;
	private JCheckBox separatorAfter;
	private JPanel root;
	private JCheckBox betweenUpper;

	public CaseSwitchingSettingsForm() {
		;
	}

	public void setData(CaseSwitchingSettings data) {
		separatorBefore.setSelected(data.isSeparatorBeforeDigit());
		separatorAfter.setSelected(data.isSeparatorAfterDigit());
		betweenUpper.setSelected(data.isPutSeparatorBetweenUpperCases());
	}

	public void getData(CaseSwitchingSettings data) {
		data.setSeparatorBeforeDigit(separatorBefore.isSelected());
		data.setSeparatorAfterDigit(separatorAfter.isSelected());
		data.setPutSeparatorBetweenUpperCases(betweenUpper.isSelected());
	}

	public boolean isModified(CaseSwitchingSettings data) {
		if (separatorBefore.isSelected() != data.isSeparatorBeforeDigit()) return true;
		if (separatorAfter.isSelected() != data.isSeparatorAfterDigit()) return true;
		if (betweenUpper.isSelected() != data.isPutSeparatorBetweenUpperCases()) return true;
		return false;
	}

	public JPanel getRoot() {
		return root;
	}

}
