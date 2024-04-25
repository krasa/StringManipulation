package osmedile.intellij.stringmanip.toolwindow;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class StringManipulationToolWindowFactory implements ToolWindowFactory, DumbAware {

	public static final String ID = "String Manipulation";
	public static final @NotNull
	Key<ToolWindowPanel> TOOL_WINDOW_PANEL = Key.create("ToolWindowPanel");


	@Override
	public boolean shouldBeAvailable(@NotNull Project project) {
		return false;
	}

	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		initPanel(project, toolWindow);
	}

	private static void initPanel(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		ToolWindowPanel previewPanel = new ToolWindowPanel(project, toolWindow);

		JBScrollPane toolWindowContent = new JBScrollPane(previewPanel.getRoot());
		toolWindowContent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		ContentFactory contentFactory = ContentFactory.getInstance();
		Content content = contentFactory.createContent(toolWindowContent, null, false);
		content.putUserData(TOOL_WINDOW_PANEL, previewPanel);
		toolWindow.getContentManager().addContent(content);
	}

	@Nullable
	public static ToolWindowPanel getToolWindowPanel(Project project) {
		ToolWindowManager instance = ToolWindowManagerEx.getInstance(project);
		ToolWindow toolWindow = instance.getToolWindow(StringManipulationToolWindowFactory.ID);
		if (toolWindow != null) {
			if (!toolWindow.isShowStripeButton() || !toolWindow.isAvailable()) {
				toolWindow.setShowStripeButton(true);
				toolWindow.setAvailable(true);
				toolWindow.show();
			}

			Content[] contents = toolWindow.getContentManager().getContents();
			if (contents.length > 0) {
				Content content = contents[0];
				return content.getUserData(TOOL_WINDOW_PANEL);
			} else {
				initPanel(project, toolWindow);
			}
		}
		return null;
	}

	public static void showToolWindow(Project project) {
		ToolWindow toolWindow = ToolWindowManagerEx.getInstance(project).getToolWindow(ID);
		if (toolWindow != null) {
			toolWindow.show();
		}
	}
}
