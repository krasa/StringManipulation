package osmedile.intellij.stringmanip.align;

import com.google.common.base.Joiner;
import com.intellij.application.options.colors.ColorAndFontOptions;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.Donate;
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

import static shaded.org.apache.commons.lang3.StringUtils.isEmpty;

public class TextAlignmentForm {
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
	private EditorImpl myEditor;

	public TextAlignmentForm(ColumnAlignerModel lastModel, Editor editor) {
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
		init(lastModel);
		historyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final TextAlignmentHistoryForm textAlignmentHistoryForm = new TextAlignmentHistoryForm(editor);

				DialogWrapper dialogWrapper = new DialogWrapper(TextAlignmentForm.this.root, false) {
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
						return textAlignmentHistoryForm.root;
					}

					@Override
					protected void doOKAction() {
						super.doOKAction();
					}
				};

				boolean b = dialogWrapper.showAndGet();
				if (b) {
					ColumnAlignerModel model = textAlignmentHistoryForm.getModel();
					if (model != null) {
						init(model);
					}
				}

			}
		});

		for (Field field : this.getClass().getDeclaredFields()) {
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

		List<String> result = new ColumnAligner(getModel()).align(lines);


		ApplicationManager.getApplication().runWriteAction(() -> {
			myEditor.getDocument().setText(Joiner.on("\n").join(result));
			myPreviewPanel.validate();
			myPreviewPanel.repaint();
		});
	}

	protected void init(ColumnAlignerModel lastSeparators) {
		_setData(lastSeparators);
		init(lastSeparators.getSeparators());
		preview();
	}


	private void createUIComponents() {
		textfields = new JPanel();
		myEditor = (EditorImpl) createEditorPreview();
		myPreviewPanel = (JPanel) myEditor.getComponent();
		myPreviewPanel.setPreferredSize(new Dimension(0, 200));
	}

	@NotNull
	private static Editor createEditorPreview() {
		EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
		ColorAndFontOptions options = new ColorAndFontOptions();
		options.reset();
		options.selectScheme(scheme.getName());
		return createPreviewEditor("", scheme, false);
	}

	static Editor createPreviewEditor(String text, EditorColorsScheme scheme, boolean editable) {
		EditorFactory editorFactory = EditorFactory.getInstance();
		Document editorDocument = editorFactory.createDocument(text);
		EditorEx editor = (EditorEx) (editable ? editorFactory.createEditor(editorDocument) : editorFactory.createViewer(editorDocument));
		editor.setColorsScheme(scheme);
		EditorSettings settings = editor.getSettings();
		settings.setLineNumbersShown(true);
		settings.setWhitespacesShown(false);
		settings.setLineMarkerAreaShown(false);
		settings.setIndentGuidesShown(false);
		settings.setFoldingOutlineShown(false);
		settings.setAdditionalColumnsCount(0);
		settings.setAdditionalLinesCount(0);
		settings.setRightMarginShown(false);

		return editor;
	}

	private void init(List<String> lastSeparators) {
		textfields.removeAll();
		BoxLayout boxLayout = new BoxLayout(textfields, BoxLayout.Y_AXIS);
		textfields.setLayout(boxLayout);
		textfields.setAlignmentX(Component.LEFT_ALIGNMENT);
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
		getData(data);
	}

	public void setData(ColumnAlignerModel data) {
		addSpaceBeforeSeparatorCheckBox.setSelected(data.isSpaceBeforeSeparator());
		addSpaceAfterSeparatorCheckBox.setSelected(data.isSpaceAfterSeparator());
		trimValues.setSelected(data.isTrimValues());
		trimLines.setSelected(data.isTrimLines());
		sequentially.setSelected(data.isSequentialProcessing());
	}

	public void getData(ColumnAlignerModel data) {
		data.setSpaceBeforeSeparator(addSpaceBeforeSeparatorCheckBox.isSelected());
		data.setSpaceAfterSeparator(addSpaceAfterSeparatorCheckBox.isSelected());
		data.setTrimValues(trimValues.isSelected());
		data.setTrimLines(trimLines.isSelected());
		data.setSequentialProcessing(sequentially.isSelected());
	}

	public boolean isModified(ColumnAlignerModel data) {
		if (addSpaceBeforeSeparatorCheckBox.isSelected() != data.isSpaceBeforeSeparator()) return true;
		if (addSpaceAfterSeparatorCheckBox.isSelected() != data.isSpaceAfterSeparator()) return true;
		if (trimValues.isSelected() != data.isTrimValues()) return true;
		if (trimLines.isSelected() != data.isTrimLines()) return true;
		if (sequentially.isSelected() != data.isSequentialProcessing()) return true;
		return false;
	}
}
