package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.ui.table.ComponentsListFocusTraversalPolicy;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.Donate;
import osmedile.intellij.stringmanip.utils.PreviewDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RemoveDuplicatesDialog extends PreviewDialog implements Disposable {
	private static final Logger LOG = Logger.getInstance(RemoveDuplicatesDialog.class);

	public JPanel contentPane;

	public JRadioButton lines;

	public JPanel donatePanel;
	public JPanel coreWithoutPreview;
	private JRadioButton selections;
	private JRadioButton linesKeepEmpty;


	public RemoveDuplicatesDialog(@NotNull RemoveDuplicatesSettings settings, @NotNull Editor editor) {
		super(editor);
		init(settings);

		contentPane.setFocusTraversalPolicy(new ComponentsListFocusTraversalPolicy() {
			@NotNull
			@Override
			protected List<Component> getOrderedComponents() {
				List<Component> jRadioButtons = new ArrayList<Component>();
				jRadioButtons.add(selections);
				jRadioButtons.add(lines);
				jRadioButtons.add(linesKeepEmpty);

				return jRadioButtons;
			}
		});
		donatePanel.add(Donate.newDonateButton());
	}

	@Override
	protected void renderPreviewAsync(Object input) {
	}

	@NotNull
	@Override
	public JComponent getPreferredFocusedComponent() {
		return contentPane;
	}

	@NotNull
	@Override
	public JComponent getRoot() {
		return contentPane;
	}


	public void init(RemoveDuplicatesSettings settings) {
		setData(settings);
		switch (settings.getType()) {
			case REMOVE_LINE:
				lines.setSelected(true);
				break;
			case REMOVE_LINE_KEEP_EMPTY:
				linesKeepEmpty.setSelected(true);
				break;
			case REMOVE_SELECTION:
				selections.setSelected(true);
				break;
		}
	}


	public RemoveDuplicatesSettings getSettings() {
		RemoveDuplicatesSettings settings = new RemoveDuplicatesSettings();
		getData(settings);
		if (lines.isSelected()) {
			settings.setType(RemoveDuplicatesSettings.Type.REMOVE_LINE);
		} else if (linesKeepEmpty.isSelected()) {
			settings.setType(RemoveDuplicatesSettings.Type.REMOVE_LINE_KEEP_EMPTY);
		} else if (selections.isSelected()) {
			settings.setType(RemoveDuplicatesSettings.Type.REMOVE_SELECTION);
		}
		return settings;
	}


	private void createUIComponents() {
	}


	public void setData(RemoveDuplicatesSettings data) {
	}

	public void getData(RemoveDuplicatesSettings data) {
	}

	public boolean isModified(RemoveDuplicatesSettings data) {
		return false;
	}

	@Override
	public void dispose() {

	}
}
