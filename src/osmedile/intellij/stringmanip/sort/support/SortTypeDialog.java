package osmedile.intellij.stringmanip.sort.support;

import com.google.common.base.Joiner;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.impl.EditorHyperlinkSupport;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredSideBorder;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.table.ComponentsListFocusTraversalPolicy;
import org.apache.commons.lang3.LocaleUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import osmedile.intellij.stringmanip.utils.PreviewDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static osmedile.intellij.stringmanip.utils.DialogUtils.enabledByAny;

public class SortTypeDialog extends PreviewDialog<SortSettings> {
	private static final Logger LOG = Logger.getInstance(SortTypeDialog.class);
	public static final TextAttributes HIGHLIGHT_ATTRIBUTES_CLOSING_LINE = new TextAttributes(null, JBColor.ORANGE, null, null, 0);
	public static final TextAttributes HIGHLIGHT_ATTRIBUTES_SEPARATOR_LINE = new TextAttributes(null, JBColor.PINK, null, null, 0);
	public static final TextAttributes HIGHLIGHT_ATTRIBUTES_LEVEL = new TextAttributes(null, JBColor.CYAN, null, null, 0);

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
	private JCheckBox trailingCharacters_checkbox;
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
	private JLabel levelRegex_label;
	private LinkLabel linkLabel;
	private MyJBTextField groupSeparatorRegex;
	private JLabel groupSeparatorRegex_label;
	private JPanel sortStrategy2;
	private JButton groupSeparatorRegex_reset;
	private JButton levelRegex_reset;
	private MyJBTextField groupClosingLineRegex;
	private JButton groupClosingLineRegex_reset;
	private JCheckBox groupClosingLineRegex_checkbox;
	private JButton groupClosingLineRegex_highlight;
	private JButton levelRegex_highlight;
	private JButton groupSeparatorRegex_highlight;
	private JRadioButton jsonSort;
	private EditorImpl myPreviewEditor;

	private final Editor editor;
	public static final ColoredSideBorder ERROR_BORDER = new ColoredSideBorder(
			JBColor.RED, JBColor.RED, JBColor.RED, JBColor.RED, 1);
	public static final ColoredSideBorder VALID_BORDER = new ColoredSideBorder(
			JBColor.GREEN, JBColor.GREEN, JBColor.GREEN, JBColor.GREEN, 1);

	private Runnable activeHighlight;
	private int lastHighlights;
	private List<String> sourceTextForPreview;

	private void updateComponents() {
		enabledByAny(new JComponent[]{comparatorNaturalOrder, comparatorDefault, comparatorCollator}, insensitive, sensitive);
		enabledByAny(new JComponent[]{valid, languageTagLabel, languageTag}, comparatorCollator);
		enabledByAny(new JComponent[]{asc, desc}, insensitive, sensitive, hexa, length);
		enabledByAny(new JComponent[]{
				shuffle,
				reverse,
				length,
				hexa,
				groupSort},
				normalSort,hierarchicalSort);
		enabledByAny(new JComponent[]{
				trailingCharacters_checkbox,
				trailingCharacters,
				},
				normalSort,hierarchicalSort);

		enabledByAny(new JComponent[]{preserveBlank,preserveLeadingSpaces, ignoreLeadingSpaces, removeBlank}, normalSort);
		enabledByAny(new JComponent[]{
				groupSeparatorRegex, groupSeparatorRegex_label, groupSeparatorRegex_reset, groupSeparatorRegex_highlight,
				levelRegex, levelRegex_label, levelRegex_reset, levelRegex_highlight,

		}, groupSort, hierarchicalSort);

		enabledByAny(new JComponent[]{
						groupClosingLineRegex, groupClosingLineRegex_checkbox, groupClosingLineRegex_reset, groupClosingLineRegex_highlight

				},
				hierarchicalSort);

//		disableByAny(new JComponent[]{preserveLeadingSpaces, ignoreLeadingSpaces, removeBlank, preserveBlank}, jsonSort, hierarchicalSort);
//		disableByAny(new JComponent[]{preserveBlank}, jsonSort, hierarchicalSort, groupSort);

//		disableByAny(new JComponent[]{
//				shuffle,
//				reverse,
//				length,
//				hexa,
//				trailingCharacters_checkbox,
//				trailingCharacters,
//				groupSort
//				},
//				jsonSort);

		submitRenderPreview();
	}


	public SortTypeDialog(SortSettings sortSettings, boolean additionaloptions) {
		this(sortSettings, additionaloptions, null);
	}

	public SortTypeDialog(SortSettings sortSettings, boolean additionaloptions, Editor editor) {
		this.editor = editor;
		sourceTextForPreview = getPreviewLines(editor);

		preserveLeadingSpaces.setVisible(additionaloptions);
		trailingCharacters_checkbox.setVisible(additionaloptions);
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

		levelRegex_reset.addActionListener(e -> levelRegex.setText(SortSettings.LEVEL_REGEX));
		groupSeparatorRegex_reset.addActionListener(e -> groupSeparatorRegex.setText(SortSettings.GROUP_SEPARATOR_REGEX));
		groupClosingLineRegex_reset.addActionListener(e -> groupClosingLineRegex.setText(SortSettings.GROUP_CLOSING_LINE_REGEX));

		groupClosingLineRegex_highlight.addActionListener(new HighlightListener(() -> highlight(HIGHLIGHT_ATTRIBUTES_CLOSING_LINE, groupClosingLineRegex, true)));
		groupSeparatorRegex_highlight.addActionListener(new HighlightListener(() -> highlight(HIGHLIGHT_ATTRIBUTES_SEPARATOR_LINE, groupSeparatorRegex, true)));
		levelRegex_highlight.addActionListener(new HighlightListener(() -> highlight(HIGHLIGHT_ATTRIBUTES_LEVEL, levelRegex, false)));

		hierarchicalSort.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					groupSort.setSelected(true);
				}
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
				list.add(jsonSort);
				list.add(groupSort);
				list.add(levelRegex);
				list.add(groupSeparatorRegex);
				list.add(groupClosingLineRegex_checkbox);
				list.add(groupClosingLineRegex);

				list.add(ignoreLeadingSpaces);
				list.add(preserveLeadingSpaces);
				list.add(trailingCharacters_checkbox);
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

	private void highlight(TextAttributes attributes, MyJBTextField regex, boolean wholeLine) {
		AtomicInteger matched = new AtomicInteger();
		Pattern compile = null;
		try {
			compile = Pattern.compile(regex.getText());
		} catch (Exception e) {
			return;
		}

		Project project = editor.getProject();
		myPreviewEditor.getMarkupModel().removeAllHighlighters();
		EditorHyperlinkSupport myHyperlinks = new EditorHyperlinkSupport(myPreviewEditor, project);
		int lineCount = myPreviewEditor.getDocument().getLineCount();
		if (lineCount > 0) {
			Pattern finalCompile = compile;
			myHyperlinks.highlightHyperlinks((s, i) -> {
				int length = s.length();
				if (wholeLine) {
					if (finalCompile.matcher(s).matches()) {
						matched.incrementAndGet();
						return new Filter.Result(i - length, i, null, attributes);
					}
				} else {
					Matcher matcher = finalCompile.matcher(s);
					if (matcher.find()) {
						matched.incrementAndGet();

						final int start = matcher.start();
						final int end = matcher.end();
						int offset = i - length;
						return new Filter.Result(offset + start, offset + end, null, attributes);
					}
				}
				return null;
			}, 0, lineCount - 1);
		}
		lastHighlights = matched.get();
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

	@Override
	protected void renderPreviewAsync() {
		if (this.editor == null) {
			return;
		}
		if (!validateRegexp()) return;

		String s;
		SortSettings settings = null;
		try {
			settings = getSettings();
			List<String> result = sortPreview(editor, settings);
			s = Joiner.on("\n").join(result);
		} catch (SortException e) {
			LOG.warn(e);
			s = e.getMessage();
		} catch (Throwable e) {
			LOG.error(e);
			s = e.toString();
//			String s1 = IdeUtils.stacktraceToString(e);
//			s += "\n\n" + s1;
		}
		setPreviewTextOnEDT(s, myPreviewEditor, myPreviewPanel, settings);
	}

	@NotNull
	@Override
	public JComponent getPreferredFocusedComponent() {
		return insensitive;
	}

	@NotNull
	@Override
	public JComponent getRoot() {
		return contentPane;
	}

	@Override
	protected void inPreviewWriteAction(EditorImpl previewEditor, SortSettings settings) {
		previewEditor.getMarkupModel().removeAllHighlighters();
		if (activeHighlight != null && (groupClosingLineRegex_highlight.isEnabled() || groupSeparatorRegex_highlight.isEnabled() || levelRegex_highlight.isEnabled())) {
			activeHighlight.run();
		}
		if (settings != null && settings.isJsonSort()) {
//			CommandProcessor.getInstance().executeCommand(editor.getProject(), () -> {
			String text = myPreviewEditor.getDocument().getText();
			String reformat = JsonSort.reformat(text, editor.getProject());
			myPreviewEditor.getDocument().setText(reformat);
//			},"StringManipulation", myPreviewEditor.getComponent());
		}
	}


	protected List<String> sortPreview(Editor editor, SortSettings settings) {
		return new SortLines(editor.getProject(), sourceTextForPreview, settings).sortLines();
	}

	public void init(SortSettings sortSettings) {
		ignoreLeadingSpaces.setSelected(sortSettings.isIgnoreLeadingSpaces());
		preserveLeadingSpaces.setSelected(sortSettings.isPreserveLeadingSpaces());
		trailingCharacters_checkbox.setSelected(sortSettings.isPreserveTrailingSpecialCharacters());
		trailingCharacters.setText(sortSettings.getTrailingChars());
		languageTag.setText(sortSettings.getCollatorLanguageTag());
		normalSort.setSelected(!sortSettings.isHierarchicalSort());
		hierarchicalSort.setSelected(sortSettings.isHierarchicalSort());
		jsonSort.setSelected(sortSettings.isJsonSort());
		groupSort.setSelected(sortSettings.isSortByGroups());
		levelRegex.setText(sortSettings.getLevelRegex());
		groupSeparatorRegex.setText(sortSettings.getGroupSeparatorRegex());
		groupClosingLineRegex_checkbox.setSelected(sortSettings.isGroupClosingLineRegexEnabled());
		groupClosingLineRegex.setText(sortSettings.getGroupClosingLineRegex());

		validateLocale();
		validateRegexp();

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
		boolean result = true;
		try {
			String text = levelRegex.getText();
			Pattern.compile(text);
			levelRegex.setMyBorder(VALID_BORDER);
			levelRegex.setToolTipText("valid regex");
		} catch (Throwable e) {
			levelRegex.setMyBorder(ERROR_BORDER);
			levelRegex.setToolTipText("invalid regex");
			result = false;
		}

		try {
			String text = groupSeparatorRegex.getText();
			Pattern.compile(text);
			groupSeparatorRegex.setMyBorder(VALID_BORDER);
			groupSeparatorRegex.setToolTipText("valid regex");
		} catch (Throwable e) {
			groupSeparatorRegex.setMyBorder(ERROR_BORDER);
			groupSeparatorRegex.setToolTipText("invalid regex");
			result = false;

		}

		try {
			String text = groupClosingLineRegex.getText();
			Pattern.compile(text);
			groupClosingLineRegex.setMyBorder(VALID_BORDER);
			groupClosingLineRegex.setToolTipText("valid regex");

		} catch (Throwable e) {
			groupClosingLineRegex.setMyBorder(ERROR_BORDER);
			groupClosingLineRegex.setToolTipText("invalid regex");
			result = false;

		}

		return result;
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
		sortSettings.setPreserveTrailingSpecialCharacters(trailingCharacters_checkbox.isSelected());
		sortSettings.setTrailingChars(trailingCharacters.getText());
		sortSettings.setCollatorLanguageTag(languageTag.getText());
		sortSettings.setSortByGroups(groupSort.isSelected());
		sortSettings.setHierarchicalSort(hierarchicalSort.isSelected());
		sortSettings.setJsonSort(jsonSort.isSelected());
		sortSettings.setLevelRegex(levelRegex.getText());
		sortSettings.setGroupSeparatorRegex(groupSeparatorRegex.getText());
		sortSettings.setGroupClosingLineRegex(groupClosingLineRegex.getText());
		sortSettings.setGroupClosingLineRegexEnabled(groupClosingLineRegex_checkbox.isSelected());
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

	private class HighlightListener implements ActionListener {
		private final Runnable runnable;

		public HighlightListener(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (lastHighlights > 0 && activeHighlight == runnable) {
				lastHighlights = 0;
				activeHighlight = null;
				myPreviewEditor.getMarkupModel().removeAllHighlighters();
				return;
			}
			activeHighlight = runnable;
			activeHighlight.run();
		}
	}
}
