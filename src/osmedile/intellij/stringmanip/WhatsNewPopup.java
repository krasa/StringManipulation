package osmedile.intellij.stringmanip;

import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import javax.swing.event.HyperlinkEvent;


public class WhatsNewPopup {
	public static final NotificationGroup NOTIFICATION = new NotificationGroup("String Manipulation",
		NotificationDisplayType.STICKY_BALLOON, true);
	
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(WhatsNewPopup.class);
	protected static final int CURRENT_VERSION = 2;

	public static void whatsNewCheck(Editor editor) {
		popup(PluginPersistentStateComponent.getInstance(), editor.getProject());
	}

	public static void popup(PluginPersistentStateComponent stateComponent, final Project project) {
		if (stateComponent.getVersion() < CURRENT_VERSION) {
			stateComponent.setVersion(CURRENT_VERSION);

			ApplicationManager.getApplication().invokeLater(() -> {
				Notification notification = NOTIFICATION.createNotification("String Manipulation popup", "You can now customize 'Switch Case' action or add your own via <a href=\"#\">Settings | Other Settings | String Manipulation</a>", NotificationType.INFORMATION,
					new NotificationListener.UrlOpeningListener(true) {
						@Override
						protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
							ApplicationManager.getApplication().invokeLater(() -> {
								ShowSettingsUtil.getInstance().showSettingsDialog(project, "String Manipulation");

							});
						}
					});

				Notifications.Bus.notify(notification);
			});
		}
	}
}
