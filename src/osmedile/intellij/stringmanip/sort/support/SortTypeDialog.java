package osmedile.intellij.stringmanip.sort.support;

import com.google.common.base.Joiner;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.table.ComponentsListFocusTraversalPolicy;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.IdeUtils;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static osmedile.intellij.stringmanip.utils.DialogUtils.disableByAny;
import static osmedile.intellij.stringmanip.utils.DialogUtils.enabledByAny;

public class SortTypeDialog {
	public static final int MAX_PREVIEW_LENGTH = 10000;
	public JPanel contentPane;

	public JRadioButton insensitive;
	protected JRadioButton sensitive;
	protected JRadioButton length;
	public JRadioButton shuffle;
	public JRadioButton reverse;
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
	public JPanel donatePanel;
	private JRadioButton comparatorDefault;
	private JPanel myPreviewPanel;
	private JPanel sortStrategy;
	private JRadioButton hierarchicalSort;
	private JCheckBox groupSort;
	private JRadioButton normalSort;
	private JPanel previewParent;
	public JPanel coreWithoutPreview;
	private JTextField levelRegex;
	private JLabel levelRegexStatus;
	private JLabel levelRegexLabel;
	private EditorImpl myPreviewEditor;

	private final Editor editor;

	private void updateComponents() {
		enabledByAny(new JComponent[]{comparatorNaturalOrder, comparatorDefault, comparatorCollator}, insensitive, sensitive);
		enabledByAny(new JComponent[]{valid, languageTagLabel, languageTag}, comparatorCollator);
		enabledByAny(new JComponent[]{asc, desc}, insensitive, sensitive, hexa, length);
//		enabledByAny(new JComponent[]{preserveTrailingSpecialCharacters, trailingCharacters,preserveLeadingSpaces, ignoreLeadingSpaces, removeBlank,preserveBlank}, normalSort);
		enabledByAny(new JComponent[]{levelRegex, levelRegexStatus, levelRegexLabel}, groupSort, hierarchicalSort);

		disableByAny(new JComponent[]{preserveTrailingSpecialCharacters, trailingCharacters, preserveLeadingSpaces, ignoreLeadingSpaces, removeBlank, preserveBlank}, hierarchicalSort);
		disableByAny(new JComponent[]{preserveBlank}, hierarchicalSort, groupSort);

		preview();
	}


	public SortTypeDialog(SortSettings sortSettings, boolean additionaloptions) {
		this(sortSettings, additionaloptions, null);
	}

	public SortTypeDialog(SortSettings sortSettings, boolean additionaloptions, Editor editor) {
		this.editor = editor;
		preserveLeadingSpaces.setVisible(additionaloptions);
		preserveTrailingSpecialCharacters.setVisible(additionaloptions);
		trailingCharacters.setVisible(additionaloptions);
		removeBlank.setVisible(additionaloptions);
		preserveBlank.setVisible(additionaloptions);
		sortStrategy.setVisible(additionaloptions);
		previewParent.setVisible(editor != null);
		languageTag.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(@NotNull final DocumentEvent e) {
				validateLocale();
			}
		});

		init(sortSettings);

		contentPane.setFocusTraversalPolicy(new ComponentsListFocusTraversalPolicy() {
			@NotNull
			@Override
			protected java.util.List<Component> getOrderedComponents() {
				List<Component> jRadioButtons = new ArrayList<Component>();
				jRadioButtons.add(insensitive);
				jRadioButtons.add(sensitive);
				jRadioButtons.add(length);
				jRadioButtons.add(hexa);
				jRadioButtons.add(reverse);
				jRadioButtons.add(shuffle);

				jRadioButtons.add(asc);
				jRadioButtons.add(desc);

				jRadioButtons.add(comparatorDefault);
				jRadioButtons.add(comparatorNaturalOrder);
				jRadioButtons.add(comparatorCollator);
				jRadioButtons.add(languageTag);

				jRadioButtons.add(normalSort);
				jRadioButtons.add(hierarchicalSort);
				jRadioButtons.add(groupSort);
				jRadioButtons.add(levelRegex);

				jRadioButtons.add(ignoreLeadingSpaces);
				jRadioButtons.add(preserveLeadingSpaces);
				jRadioButtons.add(preserveTrailingSpecialCharacters);
				jRadioButtons.add(trailingCharacters);

				jRadioButtons.add(removeBlank);
				jRadioButtons.add(preserveBlank);

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

		if (this.editor != null) {
			addPreviewListeners(this);
		} else {
			myPreviewEditor = null;
			myPreviewPanel = new JPanel();
		}
	}

	private void addPreviewListeners(Object object) {
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				Object o = field.get(object);
				if (o instanceof JToggleButton) {
					JToggleButton button = (JToggleButton) o;
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							updateComponents();
						}

					});
				}
				if (o instanceof JTextField) {
					JTextField jTextField = (JTextField) o;
					jTextField.getDocument().addDocumentListener(new DocumentAdapter() {
						@Override
						protected void textChanged(DocumentEvent e) {
							updateComponents();
						}
					});
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void preview() {
		if (this.editor != null) {
			if (!validateRegexp()) {
				return;
			}
			List<String> result = sort(editor, getSettings());

			ApplicationManager.getApplication().runWriteAction(() -> {
				myPreviewEditor.getDocument().setText(Joiner.on("\n").join(result));
				myPreviewPanel.validate();
				myPreviewPanel.repaint();
			});
		}
	}

	protected List<String> sort(Editor editor, SortSettings settings) {
		List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
		IdeUtils.sort(caretsAndSelections);
		int length = 0;
		List<String> lines = new ArrayList<String>();
		for (CaretState caretsAndSelection : caretsAndSelections) {
			LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
			LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
			String text = editor.getDocument().getText(
					new TextRange(editor.logicalPositionToOffset(selectionStart),
							editor.logicalPositionToOffset(selectionEnd)));

			length += text.length();
			String[] split = text.split("\n");
			lines.addAll(Arrays.asList(split));
			if (length > MAX_PREVIEW_LENGTH) {
				break;
			}
		}

		List<String> result = new SortLines(lines, settings).sortLines();
		return result;
	}

	public void init(SortSettings sortSettings) {
		ignoreLeadingSpaces.setSelected(sortSettings.isIgnoreLeadingSpaces());
		preserveLeadingSpaces.setSelected(sortSettings.isPreserveLeadingSpaces());
		preserveTrailingSpecialCharacters.setSelected(sortSettings.isPreserveTrailingSpecialCharacters());
		trailingCharacters.setText(sortSettings.getTrailingChars());
		languageTag.setText(sortSettings.getCollatorLanguageTag());
		normalSort.setSelected(!sortSettings.isHierarchicalSort());
		hierarchicalSort.setSelected(sortSettings.isHierarchicalSort());
		groupSort.setSelected(sortSettings.isSortByGroups());
		levelRegex.setText(sortSettings.getLevelRegex());

		validateLocale();
		validateRegexp();

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

			case NORMAL:
				comparatorDefault.setSelected(true);
				break;
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

	private boolean validateRegexp() {
		try {
			String text = levelRegex.getText();
			Pattern.compile(text);
			levelRegexStatus.setText("valid");
			levelRegexStatus.setForeground(JBColor.GREEN);
			return true;
		} catch (Throwable e) {
			levelRegexStatus.setText("invalid");
			levelRegexStatus.setForeground(JBColor.RED);
			return false;
		}
	}


	public SortSettings getSettings() {
		SortSettings sortSettings = new SortSettings(getResult());
		sortSettings.setBlankLines(preserveBlank.isSelected() ? SortSettings.BlankLines.PRESERVE : SortSettings.BlankLines.REMOVE);
		sortSettings.setIgnoreLeadingSpaces(ignoreLeadingSpaces.isSelected());
		sortSettings.setPreserveLeadingSpaces(preserveLeadingSpaces.isSelected());
		if (comparatorNaturalOrder.isSelected()) {
			sortSettings.setBaseComparator(SortSettings.BaseComparator.NATURAL);
		} else if (comparatorCollator.isSelected()) {
			sortSettings.setBaseComparator(SortSettings.BaseComparator.LOCALE_COLLATOR);
		} else {
			sortSettings.setBaseComparator(SortSettings.BaseComparator.NORMAL);
		}
		sortSettings.setPreserveTrailingSpecialCharacters(preserveTrailingSpecialCharacters.isSelected());
		sortSettings.setTrailingChars(trailingCharacters.getText());
		sortSettings.setCollatorLanguageTag(languageTag.getText());
		sortSettings.setSortByGroups(groupSort.isSelected());
		sortSettings.setHierarchicalSort(hierarchicalSort.isSelected());
		sortSettings.setLevelRegex(levelRegex.getText());
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


	private void createUIComponents() {
		myPreviewEditor = IdeUtils.createEditorPreview("", false);
		myPreviewPanel = (JPanel) myPreviewEditor.getComponent();
		myPreviewPanel.setPreferredSize(new Dimension(0, 200));
	}
}
