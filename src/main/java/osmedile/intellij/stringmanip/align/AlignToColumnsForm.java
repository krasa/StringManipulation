package osmedile.intellij.stringmanip.align;

import com.google.common.base.Joiner;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CommonActionsPanel;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.sort.support.SortException;
import osmedile.intellij.stringmanip.sort.support.SortTypeDialog;
import osmedile.intellij.stringmanip.utils.DialogUtils;
import osmedile.intellij.stringmanip.utils.IdeUtils;
import osmedile.intellij.stringmanip.utils.PreviewDialog;
import osmedile.intellij.stringmanip.utils.StringUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static osmedile.intellij.stringmanip.utils.ActionUtils.safeParse;
import static osmedile.intellij.stringmanip.utils.DialogUtils.disableByAny;
import static osmedile.intellij.stringmanip.utils.DialogUtils.enabledByAny;

public class AlignToColumnsForm extends PreviewDialog {
	private static final Logger LOG = Logger.getInstance(AlignToColumnsForm.class);
	private final List<String> previewLines;
	public JPanel root;
	private JPanel textfields;
	private JButton resetButton;
	private JCheckBox addSpaceBeforeSeparatorCheckBox;
	private JCheckBox trimLines;
	private JCheckBox trimValues;
	private JCheckBox addSpaceAfterSeparatorCheckBox;
	private JRadioButton alignSeparatorLeft;
	private JRadioButton alignSeparatorRight;
	private JButton historyButton;
	private JCheckBox sequentially;
	private JPanel myPreviewPanel;
	private JPanel sortSubPanel;
	private JTextField sortColumnsOrder;
	private JCheckBox skipFirstRow;
	private JCheckBox sortOnly;
	private JTextField maxSeparators;
	private JCheckBox keepLeadingIndent;
	private JCheckBox sbcCaseWorkaround;
	private JButton donateButton;
	private JTextArea debugTextArea;
	private JTextField debugRowNumber;
	private JPanel debugRowActions;
	protected JPanel mainPanel;
	private JCheckBox alignRightNumbers;
	private JCheckBox alignRightAll;
	private JCheckBox alignDecimalSeparator;
	private JTextField decimalSeparator;
	private JTextField alignRightColumnsIndexes;
	private JLabel alignRightColumnsLabel;

	private EditorImpl myPreviewEditor;
	private SortTypeDialog sortTypeForm;

	public AlignToColumnsForm(ColumnAlignerModel lastModel, Editor editor) {
		super(editor);
		previewLines = PreviewDialog.getPreviewLines(editor);
		Donate.initDonateButton(donateButton);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				init(new ColumnAlignerModel());
				textfields.revalidate();
				textfields.repaint();
			}
		});
		debugRowPanel();
		alignRightNumbers.setToolTipText("Detected by regex: " + StringUtil.NUMBERS.pattern());

		sortTypeForm = new SortTypeDialog(lastModel.getSortSettings(), false, editor);
		sortTypeForm.donatePanel.setVisible(false);
		sortTypeForm.reverse.setVisible(false);
		sortTypeForm.shuffle.setVisible(false);
		sortSubPanel.add(sortTypeForm.coreWithoutPreview);
		init(lastModel);
		historyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final AlignToColumnsHistoryForm alignToColumnsHistoryForm = new AlignToColumnsHistoryForm(editor);

				DialogWrapper dialogWrapper = new DialogWrapper(AlignToColumnsForm.this.root, false) {
					{
						init();
						setTitle(StringManipulationBundle.message("history"));
					}

					@Override
					protected void dispose() {
						super.dispose();
						Disposer.dispose(alignToColumnsHistoryForm);
					}

					@Nullable
					@Override
					protected String getDimensionServiceKey() {
						return "StringManipulation.TextAlignmentHistoryForm";
					}

					@Nullable
					@Override
					protected JComponent createCenterPanel() {
						return alignToColumnsHistoryForm.root;
					}

					@Override
					protected void doOKAction() {
						super.doOKAction();
					}
				};

				boolean b = dialogWrapper.showAndGet();
				if (b) {
					ColumnAlignerModel model = alignToColumnsHistoryForm.getModel();
					if (model != null) {
						init(model);
					}
				}

			}
		});
		DialogUtils.addListeners(this, this::updateComponents);
		DialogUtils.addListeners(sortTypeForm, this::updateComponents);
	}

	private void debugRowPanel() {
		AnActionButton plus = new AnActionButton("Increment", CommonActionsPanel.Buttons.UP.getIcon()) {
			@Override
			public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
				int i;
				i = safeParse(debugRowNumber.getText(), 1);
				i++;
				debugRowNumber.setText(String.valueOf(i));
			}
		};
		plus.setContextComponent(getRoot());
		plus.addCustomUpdater(anActionEvent -> true);

		AnActionButton minus = new AnActionButton("Decrement", CommonActionsPanel.Buttons.DOWN.getIcon()) {
			@Override
			public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
				int i;
				i = safeParse(debugRowNumber.getText(), 1);
				i--;
				if (i <= 1) {
					i = 1;
				}
				debugRowNumber.setText(String.valueOf(i));
			}
		};
		minus.setContextComponent(getRoot());
		minus.addCustomUpdater(anActionEvent -> true);

		DefaultActionGroup actionGroup = new DefaultActionGroup();
		ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("StringManipulation-Align", actionGroup, true);
		actionToolbar.setTargetComponent(root);
		actionGroup.addAction(minus);
		actionGroup.addAction(plus);
		actionToolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
		debugRowActions.add(actionToolbar.getComponent(), BorderLayout.CENTER);
	}

	@Override
	public void dispose() {
		super.dispose();
		sortTypeForm.dispose();
	}

	private void updateComponents() {
		disableByAny(new JComponent[]{addSpaceBeforeSeparatorCheckBox, addSpaceAfterSeparatorCheckBox, alignSeparatorRight, alignSeparatorLeft, trimValues, trimLines,
						alignRightNumbers, alignRightAll, alignRightColumnsIndexes, alignRightColumnsLabel}
				, sortOnly);
		enabledByAny(new JComponent[]{keepLeadingIndent}, trimValues, trimLines);
		enabledByAny(new JComponent[]{alignDecimalSeparator, decimalSeparator}, alignRightAll, alignRightNumbers, alignRightColumnsIndexes);

		disableByAny(new JComponent[]{alignRightNumbers, alignRightColumnsIndexes, alignRightColumnsLabel}, alignRightAll);

		SwingUtilities.invokeLater(this::submitRenderPreview);
	}


	private volatile ColumnAlignerModel lastPrevieModel;
	@Override
	protected void renderPreviewAsync(Object input) {
		String x = null;
		try {
			ColumnAlignerModel newModel = getModel();
			if (lastPrevieModel != null && lastPrevieModel.equals(newModel)) {
				return;
			}
			if (!sortTypeForm.validateRegex()) {
				return;
			}
			lastPrevieModel = newModel;
			ColumnAligner columnAligner = new ColumnAligner(lastPrevieModel);
			List<String> result = columnAligner.align(previewLines);

			SwingUtilities.invokeLater(() -> paintDebug(columnAligner));

			x = Joiner.on("\n").join(result);
		} catch (SortException e) {
			LOG.warn(e);
			x = e.getMessage();
		} catch (Throwable e) {
			LOG.error(e);
			x = e.toString();
		}

		setPreviewTextOnEDT(x, myPreviewEditor, myPreviewPanel, null);
	}

	private void paintDebug(ColumnAligner columnAligner) {
		String text = debugRowNumber.getText();

		StringBuilder sb = new StringBuilder();
		List<String> debug = columnAligner.getDebugValues(safeParse(text, 1));
		for (String s : debug) {
			sb.append(s).append("\n");
		}

		DefaultCaret caret = (DefaultCaret) debugTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		debugTextArea.setText(sb.toString());
	}

	protected void init(ColumnAlignerModel lastSeparators) {
		_setData(lastSeparators);
		init(lastSeparators.getSeparators());
		sortTypeForm.init(lastSeparators.getSortSettings());
		updateComponents();
	}


	private void createUIComponents() {
		textfields = new JPanel();
		textfields.setLayout(new BoxLayout(textfields, BoxLayout.Y_AXIS));
		textfields.setAlignmentX(Component.LEFT_ALIGNMENT);

		myPreviewEditor = IdeUtils.createEditorPreview("", false, this);
		myPreviewPanel = (JPanel) myPreviewEditor.getComponent();
		myPreviewPanel.setPreferredSize(new Dimension(0, 200));
	}

	private void init(List<String> lastSeparators) {
		textfields.removeAll();
		if (lastSeparators != null && !lastSeparators.isEmpty()) {
			for (String lastSeparator : lastSeparators) {
				if (isEmpty(lastSeparator)) {
					continue;
				}
				addTextField(lastSeparator);
			}
			addTextField(null);
		} else {
			addTextField("|");
			addTextField(null);
		}
	}

	private JBTextField addTextField(final String lastSeparator) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		final JBTextField tf = new JBTextField(lastSeparator);
		tf.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(DocumentEvent e) {
				updateComponents();
			}
		});
		tf.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						tf.selectAll();
					}
				});
			}
		});
		tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, tf.getMinimumSize().height));
		tf.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				add();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				add();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				add();
			}

			public void add() {
				if (isNotEmpty(tf.getText()) && isLast(panel)) {
					addTextField(null);
				}
			}
		});

		JButton remove = new JButton(StringManipulationBundle.message("remove"));
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textfields.getComponentCount() == 1) {
					tf.setText("");
				} else {
					textfields.remove(panel);
					textfields.revalidate();
					textfields.repaint();
				}
				submitRenderPreview();
			}
		});
		panel.add(tf);
		panel.add(remove);
		textfields.add(panel);
		textfields.revalidate();
		textfields.repaint();
		return tf;
	}

	private boolean isLast(JPanel panel) {
		return textfields.getComponent(textfields.getComponentCount() - 1) == panel;
	}

	private List<String> getSeparators() {
		ArrayList<String> strings = new ArrayList<String>(textfields.getComponentCount());
		Component[] components = textfields.getComponents();
		for (Component component : components) {
			JPanel panel = (JPanel) component;
			JBTextField field = (JBTextField) panel.getComponent(0);
			String text = field.getText();
			if (!isEmpty(text)) {
				strings.add(text);
			}
		}
		return strings;
	}

	@NotNull
	public JComponent getPreferredFocusedComponent() {
		JComponent component = (JComponent) textfields.getComponent(0);
		component = (JComponent) component.getComponent(0);
		return component;

	}

	@NotNull
	@Override
	public JComponent getRoot() {
		return root;
	}

	public ColumnAlignerModel getModel() {
		ColumnAlignerModel model = new ColumnAlignerModel();
		_getData(model);
		model.setSeparators(getSeparators());
		return model;
	}

	public void disableControls() {
		resetButton.setVisible(false);
		historyButton.setVisible(false);
	}

	public void _setData(ColumnAlignerModel data) {
		alignSeparatorLeft.setSelected(data.getAlignBy() == ColumnAlignerModel.Align.VALUES);
		alignSeparatorRight.setSelected(data.getAlignBy() == ColumnAlignerModel.Align.SEPARATORS);

		setData(data);
	}

	public void _getData(ColumnAlignerModel data) {
		if (alignSeparatorLeft.isSelected()) {
			data.setAlignBy(ColumnAlignerModel.Align.VALUES);
		} else {
			data.setAlignBy(ColumnAlignerModel.Align.SEPARATORS);
		}

		data.setSortSettings(sortTypeForm.getSettings());
		getData(data);

		data.setKeepLeadingIndent(keepLeadingIndent.isSelected() && keepLeadingIndent.isEnabled());
	}

	public void setData(ColumnAlignerModel data) {
		sortColumnsOrder.setText(data.getColumnSortOrder());
		skipFirstRow.setSelected(data.isSkipFirstRow());
		addSpaceBeforeSeparatorCheckBox.setSelected(data.isSpaceBeforeSeparator());
		addSpaceAfterSeparatorCheckBox.setSelected(data.isSpaceAfterSeparator());
		trimValues.setSelected(data.isTrimValues());
		trimLines.setSelected(data.isTrimLines());
		sequentially.setSelected(data.isSequentialProcessing());
		maxSeparators.setText(data.getMaxSeparatorsPerLine());
		decimalSeparator.setText(data.getDecimalPlaceSeparator());
		sortOnly.setSelected(data.isSortOnly());
		keepLeadingIndent.setSelected(data.isKeepLeadingIndent());
		sbcCaseWorkaround.setSelected(data.isSbcCaseWorkaround());
		alignRightNumbers.setSelected(data.isRightAlignNumbers());
		alignRightAll.setSelected(data.isRightAlignAll());
		alignRightColumnsIndexes.setText(data.getAlignRightColumnIndexes());
		alignDecimalSeparator.setSelected(data.isAlignDecimalSeparator());
	}

	public void getData(ColumnAlignerModel data) {
		data.setColumnSortOrder(sortColumnsOrder.getText());
		data.setSkipFirstRow(skipFirstRow.isSelected());
		data.setSpaceBeforeSeparator(addSpaceBeforeSeparatorCheckBox.isSelected());
		data.setSpaceAfterSeparator(addSpaceAfterSeparatorCheckBox.isSelected());
		data.setTrimValues(trimValues.isSelected());
		data.setTrimLines(trimLines.isSelected());
		data.setSequentialProcessing(sequentially.isSelected());
		data.setMaxSeparatorsPerLine(maxSeparators.getText());
		data.setDecimalPlaceSeparator(decimalSeparator.getText());
		data.setSortOnly(sortOnly.isSelected());
		data.setKeepLeadingIndent(keepLeadingIndent.isSelected());
		data.setSbcCaseWorkaround(sbcCaseWorkaround.isSelected());
		data.setRightAlignNumbers(alignRightNumbers.isSelected());
		data.setRightAlignAll(alignRightAll.isSelected());
		data.setAlignRightColumnIndexes(alignRightColumnsIndexes.getText());
		data.setAlignDecimalSeparator(alignDecimalSeparator.isSelected());
	}

	public boolean isModified(ColumnAlignerModel data) {
		if (sortColumnsOrder.getText() != null ? !sortColumnsOrder.getText().equals(data.getColumnSortOrder()) : data.getColumnSortOrder() != null)
			return true;
		if (alignRightColumnsIndexes.getText() != null ? !alignRightColumnsIndexes.getText().equals(data.getAlignRightColumnIndexes()) : data.getAlignRightColumnIndexes() != null)
			return true;
		if (skipFirstRow.isSelected() != data.isSkipFirstRow()) return true;
		if (addSpaceBeforeSeparatorCheckBox.isSelected() != data.isSpaceBeforeSeparator()) return true;
		if (addSpaceAfterSeparatorCheckBox.isSelected() != data.isSpaceAfterSeparator()) return true;
		if (trimValues.isSelected() != data.isTrimValues()) return true;
		if (trimLines.isSelected() != data.isTrimLines()) return true;
		if (sequentially.isSelected() != data.isSequentialProcessing()) return true;
		if (maxSeparators.getText() != null ? !maxSeparators.getText().equals(data.getMaxSeparatorsPerLine()) : data.getMaxSeparatorsPerLine() != null)
			return true;
		if (decimalSeparator.getText() != null ? !decimalSeparator.getText().equals(data.getDecimalPlaceSeparator()) : data.getDecimalPlaceSeparator() != null)
			return true;
		if (sortOnly.isSelected() != data.isSortOnly()) return true;
		if (keepLeadingIndent.isSelected() != data.isKeepLeadingIndent()) return true;
		if (sbcCaseWorkaround.isSelected() != data.isSbcCaseWorkaround()) return true;
		if (alignRightNumbers.isSelected() != data.isRightAlignNumbers()) return true;
		if (alignRightAll.isSelected() != data.isRightAlignAll()) return true;
		if (alignDecimalSeparator.isSelected() != data.isAlignDecimalSeparator()) return true;
		return false;
	}
}
