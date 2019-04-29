package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.diagnostic.Logger;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Collections;
import java.util.List;

public class TextAlignmentHistoryForm {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(TextAlignmentHistoryForm.class);

	private JList list;
	private JPanel preview;
	protected JPanel root;
	private TextAlignmentForm textAlignmentForm;

	public TextAlignmentHistoryForm() {
		List<ColumnAlignerModel> history = PluginPersistentStateComponent.getInstance().getHistory();
		Collections.reverse(history);
		list.setModel(new ArrayListModel<ColumnAlignerModel>(history));
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				textAlignmentForm.init((ColumnAlignerModel) list.getSelectedValue());
				preview.revalidate();
			}
		});
		list.setSelectedIndex(0);
	}

	private void createUIComponents() {
		ColumnAlignerModel lastSeparators = new ColumnAlignerModel();
		textAlignmentForm = new TextAlignmentForm(lastSeparators);
		textAlignmentForm.disableControls();
		preview = textAlignmentForm.root;
	}

	public ColumnAlignerModel getModel() {
		return textAlignmentForm.getModel();
	}
}
