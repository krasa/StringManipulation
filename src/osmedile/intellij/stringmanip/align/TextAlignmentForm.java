package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static shaded.org.apache.commons.lang3.StringUtils.isEmpty;

public class TextAlignmentForm {
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

	public TextAlignmentForm(ColumnAlignerModel lastModel) {
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
				final TextAlignmentHistoryForm textAlignmentHistoryForm = new TextAlignmentHistoryForm();

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
	}

	protected void init(ColumnAlignerModel lastSeparators) {
		_setData(lastSeparators);
		init(lastSeparators.getSeparators());
	}


	private void createUIComponents() {
		textfields = new JPanel();
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
