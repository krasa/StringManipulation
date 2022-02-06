package osmedile.intellij.stringmanip.sort.tokens;

import com.google.common.base.Joiner;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBTextField;
import osmedile.intellij.stringmanip.StringManipulationBundle;
import osmedile.intellij.stringmanip.sort.support.SortException;
import osmedile.intellij.stringmanip.sort.support.SortTypeDialog;
import osmedile.intellij.stringmanip.utils.DialogUtils;
import osmedile.intellij.stringmanip.utils.IdeUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class SortTokensGui implements Disposable {
	private static final Logger LOG = Logger.getInstance(SortTokensGui.class);

	private final Editor editor;
	private final SortTypeDialog sortTypeForm;
	public JPanel root;
	private JPanel sortSubPanel;
	private JPanel textfields;
	private JPanel myPreviewPanel;
	private JRadioButton sortAllLinesTogether;
	private JRadioButton processEachLineSeparatellyRadioButton;
	private EditorImpl myEditor;

	public SortTokensGui(SortTokensModel lastModel, Editor editor) {
		this.editor = editor;
		sortTypeForm = new SortTypeDialog(lastModel.getSortSettings(), false);
//		sortTypeForm.donatePanel.setVisible(false);
		sortTypeForm.reverse.setVisible(false);
		sortTypeForm.shuffle.setVisible(false);
		sortSubPanel.add(sortTypeForm.coreWithoutPreview);
		init(lastModel);
		DialogUtils.addListeners(this, this::updateComponents);
		DialogUtils.addListeners(sortTypeForm, this::updateComponents);
//		donatePanel.add(Donate.newDonateButton(donatePanel));
	}


	private void updateComponents() {
		preview();
	}


	protected void preview() {
		String s = null;
		try {
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

			SortTokens columnAligner = new SortTokens(lines, getModel());
			List<String> result = columnAligner.sortLines();
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
			myEditor.getDocument().setText(finalS);
			myPreviewPanel.validate();
			myPreviewPanel.repaint();
		});
	}

	protected void init(SortTokensModel lastSeparators) {
		_setData(lastSeparators);
		init(lastSeparators.getSeparators());
		sortTypeForm.init(lastSeparators.getSortSettings());
		updateComponents();
	}


	private void createUIComponents() {
		textfields = new JPanel();
		textfields.setLayout(new BoxLayout(textfields, BoxLayout.Y_AXIS));
		textfields.setAlignmentX(Component.LEFT_ALIGNMENT);

		myEditor = IdeUtils.createEditorPreview("", false, this);
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
				preview();
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

	public JComponent getPreferredFocusedComponent() {
		JComponent component = (JComponent) textfields.getComponent(0);
		component = (JComponent) component.getComponent(0);
		return component;

	}

	public SortTokensModel getModel() {
		SortTokensModel columnAlignerModel = new SortTokensModel();
		_getData(columnAlignerModel);
		columnAlignerModel.setSeparators(getSeparators());
		return columnAlignerModel;
	}


	public void _setData(SortTokensModel data) {
		setData(data);
		if (data.isProcessEachLineSeparately()) {
			processEachLineSeparatellyRadioButton.setSelected(true);
		}
		if (data.isSortAllLinesTogether()) {
			sortAllLinesTogether.setSelected(true);
		}
	}

	public void _getData(SortTokensModel data) {
		data.setSortSettings(sortTypeForm.getSettings());
		data.setProcessEachLineSeparately(processEachLineSeparatellyRadioButton.isSelected());
		data.setSortAllLinesTogether(sortAllLinesTogether.isSelected());
		getData(data);
	}

	public void setData(SortTokensModel data) {
	}

	public void getData(SortTokensModel data) {
	}

	public boolean isModified(SortTokensModel data) {
		if (sortAllLinesTogether.isSelected() != data.isSortAllLinesTogether()) return true;
		if (processEachLineSeparatellyRadioButton.isSelected() != data.isProcessEachLineSeparately()) return true;
		return false;
	}

	@Override
	public void dispose() {
		sortTypeForm.dispose();
	}
}
