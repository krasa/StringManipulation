package com.github.krasa.stringmanipulation.intellij.config;

import com.github.krasa.stringmanipulation.intellij.CaseSwitchingSettings;
import com.github.krasa.stringmanipulation.intellij.Donate;
import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;

public class CaseSwitchingSettingsForm {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(CaseSwitchingSettingsForm.class);

	private JCheckBox separatorBefore;
	private JCheckBox separatorAfter;
	private JPanel root;
	private JPanel donatePanel;

	public CaseSwitchingSettingsForm() {
		donatePanel.add(Donate.newDonateButton(donatePanel));
	}

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
