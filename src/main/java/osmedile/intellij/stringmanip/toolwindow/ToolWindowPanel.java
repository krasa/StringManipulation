package osmedile.intellij.stringmanip.toolwindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.replace.gui.CompositeForm;
import osmedile.intellij.stringmanip.replace.gui.ReplaceCompositeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolWindowPanel {
	private static final Logger log = LoggerFactory.getLogger(ToolWindowPanel.class);

	private final Project project;
	private final ToolWindow toolWindow;
	private JPanel root;
	private JButton reload;
	private JPanel content;
	private CompositeForm compositeForm;

	public JPanel getRoot() {
		return root;
	}

	public ToolWindowPanel(Project project, ToolWindow toolWindow) {
		this.project = project;
		this.toolWindow = toolWindow;
		initPanel();
		reload.setVisible(!ApplicationManager.getApplication().isInternal());
		reload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initPanel();

			}
		});
	}

	public void initPanel() {
			content.removeAll();
			PluginPersistentStateComponent instance = PluginPersistentStateComponent.getInstance();
			ReplaceCompositeModel lastReplaceModel = instance.getLastReplaceModel();
			if (lastReplaceModel == null) {
				lastReplaceModel = new ReplaceCompositeModel();
			}
			compositeForm = new CompositeForm(lastReplaceModel, false);
			content.add(compositeForm, BorderLayout.CENTER);
			root.revalidate();
			root.repaint();
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
	}

	public CompositeForm getReplacementCompositeForm() {
		return compositeForm;
	}
}
