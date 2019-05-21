package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Collections;
import java.util.List;

public class TextAlignmentHistoryForm {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(TextAlignmentHistoryForm.class);
	private final Editor editor;

	private JList list;
	private JPanel detail;
	protected JPanel root;
	private TextAlignmentForm textAlignmentForm;

	public TextAlignmentHistoryForm(Editor editor) {
		this.editor = editor;
		List<ColumnAlignerModel> history = PluginPersistentStateComponent.getInstance().getHistory();
		Collections.reverse(history);
		list.setModel(new ArrayListModel<ColumnAlignerModel>(history));
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				textAlignmentForm.init((ColumnAlignerModel) list.getSelectedValue());
				detail.revalidate();
			}
		});
		list.setSelectedIndex(0);
	}

	private void createUIComponents() {
		ColumnAlignerModel lastSeparators = new ColumnAlignerModel();
		textAlignmentForm = new TextAlignmentForm(lastSeparators, editor);
		textAlignmentForm.disableControls();
		detail = textAlignmentForm.root;
	}

	public ColumnAlignerModel getModel() {
		return textAlignmentForm.getModel();
	}
}
