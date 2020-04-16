package osmedile.intellij.stringmanip.utils;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DialogUtils {

	public static void enabledBy(@NotNull JComponent[] targets, @NotNull JToggleButton... control) {
		boolean b = true;
		for (JToggleButton jToggleButton : control) {
			b = b && (jToggleButton.isEnabled() && jToggleButton.isSelected());
		}
		for (JComponent target : targets) {
			target.setEnabled(b);
		}
	}

	public static void enabledByAny(@NotNull JComponent[] targets, @NotNull JToggleButton... control) {
		boolean b = false;
		for (JToggleButton jToggleButton : control) {
			b = b || (jToggleButton.isEnabled() && jToggleButton.isSelected());
		}
		for (JComponent target : targets) {
			target.setEnabled(b);
		}
	}

	public static void disableByAny(@NotNull JComponent[] targets, @NotNull JToggleButton... control) {
		boolean disable = false;
		for (JToggleButton jToggleButton : control) {
			disable = disable || (jToggleButton.isEnabled() && jToggleButton.isSelected());
		}
		if (disable) {
			for (JComponent target : targets) {
				target.setEnabled(false);
			}
		}
	}

	public static void visibleByAny(@NotNull JComponent[] targets, @NotNull JToggleButton... control) {
		boolean b = false;
		for (JToggleButton jToggleButton : control) {
			b = b || (jToggleButton.isEnabled() && jToggleButton.isSelected());
		}
		for (JComponent target : targets) {
			target.setVisible(b);
		}
	}
}
