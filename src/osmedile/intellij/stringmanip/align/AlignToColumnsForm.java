package osmedile.intellij.stringmanip.align;

import com.google.common.base.Joiner;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.sort.support.SortTypeDialog;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static osmedile.intellij.stringmanip.utils.DialogUtils.disableByAny;
import static shaded.org.apache.commons.lang3.StringUtils.isEmpty;

public class AlignToColumnsForm {
	private final Editor editor;
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
	private JPanel donatePanel;
	private JPanel sortSubPanel;
	private JTextField sortColumnsOrder;
	private JCheckBox skipFirstRow;
	private JPanel debugValues;
	private JCheckBox sortOnly;
	private JTextField maxSeparators;
	private EditorImpl myEditor;
	private SortTypeDialog sortTypeForm;

	public AlignToColumnsForm(ColumnAlignerModel lastModel, Editor editor) {
		this.editor = editor;
		donatePanel.add(Donate.newDonateButton(donatePanel));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				init(new ColumnAlignerModel());
				textfields.revalidate();
				textfields.repaint();
			}
		});

		sortTypeForm = new SortTypeDialog(lastModel.getSortSettings(), false);
		sortTypeForm.donatePanel.setVisible(false);
		sortTypeForm.reverse.setVisible(false);
		sortTypeForm.shuffle.setVisible(false);
		sortSubPanel.add(sortTypeForm.contentPane);
		init(lastModel);
		historyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final AlignToColumnsHistoryForm alignToColumnsHistoryForm = new AlignToColumnsHistoryForm(editor);

				DialogWrapper dialogWrapper = new DialogWrapper(AlignToColumnsForm.this.root, false) {
					{
						init();
						setTitle("History");
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

		addPreviewListeners(this);
		addPreviewListeners(sortTypeForm);
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

	private void updateComponents() {
		preview();
		disableByAny(new JComponent[]{addSpaceBeforeSeparatorCheckBox, addSpaceAfterSeparatorCheckBox, alignSeparatorRight, alignSeparatorLeft, trimValues, trimLines}, sortOnly);
	}


	protected void preview() {
		List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
		IdeUtils.sort(caretsAndSelections);
		List<String> lines = new ArrayList<String>();
		for (CaretState caretsAndSelection : caretsAndSelections) {
			LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
			LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
			String text = editor.getDocument().getText(
				new TextRange(editor.logicalPositionToOffset(selectionStart),
					editor.logicalPositionToOffset(selectionEnd)));

			String[] split = text.split("\n");
			lines.addAll(Arrays.asList(split));
		}

		ColumnAligner columnAligner = new ColumnAligner(getModel());
		List<String> result = columnAligner.align(lines);

		debugValues.removeAll();
		List<String> debug = columnAligner.getDebugValues();
		for (String s : debug) {
			debugValues.add(new JLabel(s));
		}
		debugValues.revalidate();
		debugValues.repaint();


		ApplicationManager.getApplication().runWriteAction(() -> {
			myEditor.getDocument().setText(Joiner.on("\n").join(result));
			myPreviewPanel.validate();
			myPreviewPanel.repaint();
		});
	}

	protected void init(ColumnAlignerModel lastSeparators) {
		_setData(lastSeparators);
		init(lastSeparators.getSeparators());
		sortTypeForm.init(lastSeparators.getSortSettings());
		preview();
	}


	private void createUIComponents() {
		textfields = new JPanel();
		textfields.setLayout(new BoxLayout(textfields, BoxLayout.Y_AXIS));
		textfields.setAlignmentX(Component.LEFT_ALIGNMENT);

		debugValues = new JPanel();
		debugValues.removeAll();
		debugValues.setLayout(new BoxLayout(debugValues, BoxLayout.Y_AXIS));
		debugValues.setAlignmentX(Component.LEFT_ALIGNMENT);

		myEditor = IdeUtils.createEditorPreview("", false);
		myPreviewPanel = (JPanel) myEditor.getComponent();
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
			addTextField(",");
			addTextField(null);
		}
	}

	private JBTextField addTextField(final String lastSeparator) {
		final JBTextField comp = new JBTextField(lastSeparator);
		comp.getDocument().addDocumentListener(new DocumentAdapter() {
			@Override
			protected void textChanged(DocumentEvent e) {
				updateComponents();
			}
		});
		comp.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						comp.selectAll();
					}
				});
			}
		});
		comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getMinimumSize().height));
		comp.getDocument().addDocumentListener(new DocumentListener() {
			boolean added = lastSeparator != null;

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
				if (!added) {
					addTextField(null);
					added = true;
				}
			}
		});
		textfields.add(comp);
		textfields.revalidate();
		textfields.repaint();
		return comp;
	}

	private List<String> getSeparators() {
		ArrayList<String> strings = new ArrayList<String>(textfields.getComponentCount());
		Component[] components = textfields.getComponents();
		for (Component component : components) {
			JBTextField field = (JBTextField) component;
			strings.add(field.getText());
		}
		return strings;
	}

	public JComponent getPreferredFocusedComponent() {
		return (JComponent) textfields.getComponent(0);

	}

	public ColumnAlignerModel getModel() {
		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel();
		_getData(columnAlignerModel);
		columnAlignerModel.setSeparators(getSeparators());
		return columnAlignerModel;
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
	}

	public void setData(ColumnAlignerModel data) {
		sortColumnsOrder.setText(data.getColumnSortOrder());
		skipFirstRow.setSelected(data.isSkipFirstRow());
		addSpaceBeforeSeparatorCheckBox.setSelected(data.isSpaceBeforeSeparator());
		addSpaceAfterSeparatorCheckBox.setSelected(data.isSpaceAfterSeparator());
		trimValues.setSelected(data.isTrimValues());
		trimLines.setSelected(data.isTrimLines());
		sequentially.setSelected(data.isSequentialProcessing());
		sortOnly.setSelected(data.isSortOnly());
		maxSeparators.setText(data.getMaxSeparatorsPerLine());
	}

	public void getData(ColumnAlignerModel data) {
		data.setColumnSortOrder(sortColumnsOrder.getText());
		data.setSkipFirstRow(skipFirstRow.isSelected());
		data.setSpaceBeforeSeparator(addSpaceBeforeSeparatorCheckBox.isSelected());
		data.setSpaceAfterSeparator(addSpaceAfterSeparatorCheckBox.isSelected());
		data.setTrimValues(trimValues.isSelected());
		data.setTrimLines(trimLines.isSelected());
		data.setSequentialProcessing(sequentially.isSelected());
		data.setSortOnly(sortOnly.isSelected());
		data.setMaxSeparatorsPerLine(maxSeparators.getText());
	}

	public boolean isModified(ColumnAlignerModel data) {
		if (sortColumnsOrder.getText() != null ? !sortColumnsOrder.getText().equals(data.getColumnSortOrder()) : data.getColumnSortOrder() != null)
			return true;
		if (skipFirstRow.isSelected() != data.isSkipFirstRow()) return true;
		if (addSpaceBeforeSeparatorCheckBox.isSelected() != data.isSpaceBeforeSeparator()) return true;
		if (addSpaceAfterSeparatorCheckBox.isSelected() != data.isSpaceAfterSeparator()) return true;
		if (trimValues.isSelected() != data.isTrimValues()) return true;
		if (trimLines.isSelected() != data.isTrimLines()) return true;
		if (sequentially.isSelected() != data.isSequentialProcessing()) return true;
		if (sortOnly.isSelected() != data.isSortOnly()) return true;
		if (maxSeparators.getText() != null ? !maxSeparators.getText().equals(data.getMaxSeparatorsPerLine()) : data.getMaxSeparatorsPerLine() != null)
			return true;
		return false;
	}
}
