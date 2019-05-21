package osmedile.intellij.stringmanip.sort.support;

import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.table.ComponentsListFocusTraversalPolicy;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.Donate;
import shaded.org.apache.commons.lang3.LocaleUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SortTypeDialog {
	public JPanel contentPane;

	public JRadioButton insensitive;
	protected JRadioButton sensitive;
	protected JRadioButton length;
	private JRadioButton shuffle;
	private JRadioButton reverse;
	private JRadioButton hexa;

	private JRadioButton asc;
	private JRadioButton desc;

	private JCheckBox ignoreLeadingSpaces;
	private JCheckBox preserveLeadingSpaces;
	private JCheckBox preserveTrailingSpecialCharacters;
	private JTextField trailingCharacters;
	private JRadioButton removeBlank;
	private JRadioButton preserveBlank;
	private JRadioButton comparatorNaturalOrder;
	private JRadioButton comparatorCollator;
	private JTextField languageTag;
	private JLabel languageTagLabel;
	private JLabel valid;
	private JPanel donatePanel;

	private void updateComponents() {
		enabledByAny(new JComponent[]{comparatorNaturalOrder, comparatorCollator}, insensitive, sensitive);
		enabledByAny(new JComponent[]{valid, languageTagLabel, languageTag}, comparatorCollator);
	}

	private void enabledBy(@NotNull JComponent[] targets, @NotNull JToggleButton... control) {
		boolean b = true;
		for (JToggleButton jToggleButton : control) {
			b = b && (jToggleButton.isEnabled() && jToggleButton.isSelected());
		}
		for (JComponent target : targets) {
			target.setEnabled(b);
		}
	}

	private void enabledByAny(@NotNull JComponent[] targets, @NotNull JToggleButton... control) {
		boolean b = false;
		for (JToggleButton jToggleButton : control) {
			b = b || (jToggleButton.isEnabled() && jToggleButton.isSelected());
		}
		for (JComponent target : targets) {
			target.setEnabled(b);
		}
	}

	private void visibleByAny(@NotNull JComponent[] targets, @NotNull JToggleButton... control) {
		boolean b = false;
		for (JToggleButton jToggleButton : control) {
			b = b || (jToggleButton.isEnabled() && jToggleButton.isSelected());
		}
		for (JComponent target : targets) {
			target.setVisible(b);
		}
	}


	public SortTypeDialog(SortSettings sortSettings, boolean additionaloptions) {
		preserveLeadingSpaces.setVisible(additionaloptions);
		preserveTrailingSpecialCharacters.setVisible(additionaloptions);
		trailingCharacters.setVisible(additionaloptions);
		removeBlank.setVisible(additionaloptions);
		preserveBlank.setVisible(additionaloptions);

		ignoreLeadingSpaces.setSelected(sortSettings.isIgnoreLeadingSpaces());
		preserveLeadingSpaces.setSelected(sortSettings.isPreserveLeadingSpaces());
		preserveTrailingSpecialCharacters.setSelected(sortSettings.isPreserveTrailingSpecialCharacters());
		trailingCharacters.setText(sortSettings.getTrailingChars());
		languageTag.setText(sortSettings.getCollatorLanguageTag());
		languageTag.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(@NotNull final DocumentEvent e) {
				validateLocale();
			}
		});
		validateLocale();

		for (Field field : SortTypeDialog.class.getDeclaredFields()) {
			try {
				Object o = field.get(this);
				if (o instanceof JToggleButton) {
					JToggleButton button = (JToggleButton) o;
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							updateComponents();
						}

					});
				}
//				if (o instanceof JTextField) {
//					JTextField jTextField = (JTextField) o;
//					jTextField.getDocument().addDocumentListener(new DocumentAdapter() {
//						@Override
//						protected void textChanged(DocumentEvent e) {
//							updateComponents();
//						}
//					});
//				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}

		switch (sortSettings.getBaseComparator()) {

			case NATURAL:
				comparatorNaturalOrder.setSelected(true);
				break;
			case LOCALE_COLLATOR:
				comparatorCollator.setSelected(true);
				break;
		}

		switch (sortSettings.emptyLines()) {

			case PRESERVE:
				preserveBlank.setSelected(true);
				break;
			case REMOVE:
				removeBlank.setSelected(true);
				break;
		}


		switch (sortSettings.getSortType()) {
			case SHUFFLE:
				shuffle.setSelected(true);
				break;
			case REVERSE:
				reverse.setSelected(true);
				break;
			case HEXA:
				hexa.setSelected(true);
				break;
			case CASE_SENSITIVE_A_Z:
				sensitive.setSelected(true);
				asc.setSelected(true);
				break;
			case CASE_SENSITIVE_Z_A:
				sensitive.setSelected(true);
				desc.setSelected(true);
				break;
			case CASE_INSENSITIVE_A_Z:
				insensitive.setSelected(true);
				asc.setSelected(true);
				break;
			case CASE_INSENSITIVE_Z_A:
				insensitive.setSelected(true);
				desc.setSelected(true);
				break;
			case LINE_LENGTH_SHORT_LONG:
				length.setSelected(true);
				asc.setSelected(true);
				break;
			case LINE_LENGTH_LONG_SHORT:
				length.setSelected(true);
				desc.setSelected(true);
				break;
		}

		contentPane.setFocusTraversalPolicy(new ComponentsListFocusTraversalPolicy() {
			@NotNull
			@Override
			protected java.util.List<Component> getOrderedComponents() {
				List<Component> jRadioButtons = new ArrayList<Component>();
				jRadioButtons.add(insensitive);
				jRadioButtons.add(sensitive);
				jRadioButtons.add(length);
				jRadioButtons.add(reverse);
				jRadioButtons.add(shuffle);
				jRadioButtons.add(hexa);
				jRadioButtons.add(asc);
				jRadioButtons.add(desc);
				jRadioButtons.add(removeBlank);
				jRadioButtons.add(preserveBlank);
				jRadioButtons.add(ignoreLeadingSpaces);
				jRadioButtons.add(preserveLeadingSpaces);
				jRadioButtons.add(preserveTrailingSpecialCharacters);
				jRadioButtons.add(trailingCharacters);
				jRadioButtons.add(comparatorNaturalOrder);
				jRadioButtons.add(comparatorCollator);
				jRadioButtons.add(languageTag);
				return jRadioButtons;
			}
		});
		ignoreLeadingSpaces.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!ignoreLeadingSpaces.isSelected()) {
					preserveLeadingSpaces.setSelected(false);
				}
			}
		});
		preserveLeadingSpaces.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (preserveLeadingSpaces.isSelected()) {
					ignoreLeadingSpaces.setSelected(true);
				}
			}
		});
		updateComponents();
		donatePanel.add(Donate.newDonateButton(donatePanel));
	}

	private void validateLocale() {
		Locale locale = Locale.forLanguageTag(languageTag.getText());
		boolean availableLocale = LocaleUtils.isAvailableLocale(locale);
		if (availableLocale) {
			valid.setText("valid");
			valid.setForeground(JBColor.GREEN);
		} else {
			valid.setText("invalid");
			valid.setForeground(JBColor.RED);
		}
	}


	public SortSettings getSettings() {
		SortSettings sortSettings = new SortSettings(getResult());
		sortSettings.setBlankLines(preserveBlank.isSelected() ? SortSettings.BlankLines.PRESERVE : SortSettings.BlankLines.REMOVE);
		sortSettings.setIgnoreLeadingSpaces(ignoreLeadingSpaces.isSelected());
		sortSettings.setPreserveLeadingSpaces(preserveLeadingSpaces.isSelected());
		sortSettings.setBaseComparator(comparatorNaturalOrder.isSelected() ? SortSettings.BaseComparator.NATURAL : SortSettings.BaseComparator.LOCALE_COLLATOR);
		sortSettings.setPreserveTrailingSpecialCharacters(preserveTrailingSpecialCharacters.isSelected());
		sortSettings.setTrailingChars(trailingCharacters.getText());
		sortSettings.setCollatorLanguageTag(languageTag.getText());
		return sortSettings;
	}

	public Sort getResult() {
		if (sensitive.isSelected()) {
			if (asc.isSelected()) {
				return Sort.CASE_SENSITIVE_A_Z;
			} else {
				return Sort.CASE_SENSITIVE_Z_A;
			}
		} else if (insensitive.isSelected()) {
			if (asc.isSelected()) {
				return Sort.CASE_INSENSITIVE_A_Z;
			} else {
				return Sort.CASE_INSENSITIVE_Z_A;
			}
		} else if (length.isSelected()) {
			if (asc.isSelected()) {
				return Sort.LINE_LENGTH_SHORT_LONG;
			} else {
				return Sort.LINE_LENGTH_LONG_SHORT;
			}
		} else if (reverse.isSelected()) {
			return Sort.REVERSE;
		} else if (shuffle.isSelected()) {
			return Sort.SHUFFLE;
		} else if (hexa.isSelected()) {
			return Sort.HEXA;
		}

		throw new IllegalStateException();
	}

}
