package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlignToColumnsHistoryForm implements Disposable {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(AlignToColumnsHistoryForm.class);
	private final Editor editor;

	private JList list;
	private JPanel detail;
	protected JPanel root;
	private AlignToColumnsForm alignToColumnsForm;

	public AlignToColumnsHistoryForm(Editor editor) {
		this.editor = editor;
		List<ColumnAlignerModel> history = new ArrayList<>(PluginPersistentStateComponent.getInstance().getColumnAlignerHistory());
		Collections.reverse(history);
		list.setModel(new ArrayListModel<ColumnAlignerModel>(history));
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				alignToColumnsForm.init((ColumnAlignerModel) list.getSelectedValue());
				detail.revalidate();
			}
		});
		list.setSelectedIndex(0);
	}

	private void createUIComponents() {
		ColumnAlignerModel lastSeparators = new ColumnAlignerModel();
		alignToColumnsForm = new AlignToColumnsForm(lastSeparators, editor);
		alignToColumnsForm.disableControls();
		detail = alignToColumnsForm.root;
	}

	public ColumnAlignerModel getModel() {
		return alignToColumnsForm.getModel();
	}

	@Override
	public void dispose() {
		alignToColumnsForm.dispose();
	}
}
