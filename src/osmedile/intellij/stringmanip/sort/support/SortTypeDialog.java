package osmedile.intellij.stringmanip.sort.support;

import com.google.common.base.Joiner;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.ui.ColoredSideBorder;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.table.ComponentsListFocusTraversalPolicy;
import org.apache.commons.lang3.LocaleUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import osmedile.intellij.stringmanip.utils.PreviewUtils;

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
import java.util.regex.Pattern;

import static osmedile.intellij.stringmanip.utils.DialogUtils.disableByAny;
import static osmedile.intellij.stringmanip.utils.DialogUtils.enabledByAny;

public class SortTypeDialog {
	private static final Logger LOG = Logger.getInstance(SortTypeDialog.class);

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
	private MyJBTextField levelRegex;
	private JLabel levelRegexLabel;
	private LinkLabel linkLabel;
	private MyJBTextField groupSeparatorRegex;
	private JLabel groupSeparatorRegexLabel;
	private JPanel sortStrategy2;
	private EditorImpl myPreviewEditor;

	private final Editor editor;
	public static final ColoredSideBorder ERROR_BORDER = new ColoredSideBorder(
			JBColor.RED, JBColor.RED, JBColor.RED, JBColor.RED, 1);
	public static final ColoredSideBorder VALID_BORDER = new ColoredSideBorder(
			JBColor.GREEN, JBColor.GREEN, JBColor.GREEN, JBColor.GREEN, 1);

	private void updateComponents() {
		enabledByAny(new JComponent[]{comparatorNaturalOrder, comparatorDefault, comparatorCollator}, insensitive, sensitive);
		enabledByAny(new JComponent[]{valid, languageTagLabel, languageTag}, comparatorCollator);
		enabledByAny(new JComponent[]{asc, desc}, insensitive, sensitive, hexa, length);
//		enabledByAny(new JComponent[]{preserveTrailingSpecialCharacters, trailingCharacters,preserveLeadingSpaces, ignoreLeadingSpaces, removeBlank,preserveBlank}, normalSort);
		enabledByAny(new JComponent[]{levelRegex, groupSeparatorRegex, groupSeparatorRegexLabel, levelRegexLabel}, groupSort,
				hierarchicalSort);

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
		sortStrategy2.setVisible(additionaloptions);
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
				List<Component> list = new ArrayList<Component>();
				list.add(insensitive);
				list.add(sensitive);
				list.add(length);
				list.add(hexa);
				list.add(reverse);
				list.add(shuffle);

				list.add(asc);
				list.add(desc);

				list.add(comparatorDefault);
				list.add(comparatorNaturalOrder);
				list.add(comparatorCollator);
				list.add(languageTag);

				list.add(normalSort);
				list.add(hierarchicalSort);
				list.add(groupSort);
				list.add(levelRegex);
				list.add(groupSeparatorRegex);

				list.add(ignoreLeadingSpaces);
				list.add(preserveLeadingSpaces);
				list.add(preserveTrailingSpecialCharacters);
				list.add(trailingCharacters);

				list.add(removeBlank);
				list.add(preserveBlank);

				return list;
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
		donatePanel.add(Donate.newDonateButton());

		if (this.editor != null) {
			addPreviewListeners(this);
		} else {
			myPreviewEditor = null;
			myPreviewPanel = new JPanel();
		}
		linkLabel.setListener(
				(aSource, aLinkData) -> BrowserUtil.browse((String) aLinkData),
				"https://github.com/krasa/StringManipulation/wiki/Hierarchical-sort");
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

			if (!validateRegexp2()) {
				return;
			}

			String s;
			try {
				List<String> result = sort(editor, getSettings());
				s = Joiner.on("\n").join(result);
			} catch (SortException e) {
				LOG.warn(e);
				s = e.getMessage();
			} catch (Throwable e) {
				LOG.error(e);
				s = e.toString();
			}

			String finalS = s;
			ApplicationManager.getApplication().runWriteAction(() -> {
				myPreviewEditor.getDocument().setText(finalS);
				myPreviewPanel.validate();
				myPreviewPanel.repaint();
			});
		}

	}

	protected List<String> sort(Editor editor, SortSettings settings) {
		List<String> lines = PreviewUtils.getPreviewLines(editor);
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
		groupSeparatorRegex.setText(sortSettings.getGroupSeparatorRegex());

		validateLocale();
		validateRegexp();
		validateRegexp2();

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
			levelRegex.setMyBorder(VALID_BORDER);
			levelRegex.setToolTipText("valid regex");
			return true;
		} catch (Throwable e) {
			levelRegex.setMyBorder(ERROR_BORDER);
			levelRegex.setToolTipText("invalid regex");
			return false;
		}

	}

	private boolean validateRegexp2() {
		try {
			String text = groupSeparatorRegex.getText();
			Pattern.compile(text);
			groupSeparatorRegex.setMyBorder(VALID_BORDER);
			groupSeparatorRegex.setToolTipText("valid regex");
			return true;
		} catch (Throwable e) {
			groupSeparatorRegex.setMyBorder(ERROR_BORDER);
			groupSeparatorRegex.setToolTipText("invalid regex");
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
		sortSettings.setGroupSeparatorRegex(groupSeparatorRegex.getText());
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
