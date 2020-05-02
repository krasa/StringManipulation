package com.github.krasa.stringmanipulation.intellij.align;

import com.github.krasa.stringmanipulation.intellij.config.PluginPersistentStateComponent;
import com.github.krasa.stringmanipulation.utils.align.ArrayListModel;
import com.github.krasa.stringmanipulation.utils.align.ColumnAlignerModel;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlignToColumnsHistoryForm {
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
}
