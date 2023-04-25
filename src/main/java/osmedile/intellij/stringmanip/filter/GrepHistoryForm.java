package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.align.ArrayListModel;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrepHistoryForm {
	private final Editor editor;

	private JList<GrepSettings> list;
	private JPanel detail;
	protected JPanel root;
	private GrepDialog alignToColumnsForm;

	public GrepHistoryForm(Editor editor) {
		this.editor = editor;
		List<GrepSettings> history = new ArrayList<>(PluginPersistentStateComponent.getInstance().getGrepHistory());
		Collections.reverse(history);
		list.setModel(new ArrayListModel<GrepSettings>(history));
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				alignToColumnsForm.init((GrepSettings) list.getSelectedValue());
				detail.revalidate();
			}
		});
		;
		list.setSelectedIndex(0);
	}

	private void createUIComponents() {
		alignToColumnsForm = new GrepDialog();
		detail = alignToColumnsForm.settingsPanel;
	}

	public GrepSettings getModel() {
		return alignToColumnsForm.getSettings();
	}
}
