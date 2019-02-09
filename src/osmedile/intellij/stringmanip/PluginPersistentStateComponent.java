package osmedile.intellij.stringmanip;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;

import javax.swing.event.HyperlinkEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static osmedile.intellij.stringmanip.DonationNagger.NOTIFICATION;

@State(name = "StringManipulationState", storages = {@Storage("stringManipulation.xml")})
public class PluginPersistentStateComponent implements PersistentStateComponent<PluginPersistentStateComponent> {

	public static final int LIMIT = 20;
	private List<ColumnAlignerModel> history = new ArrayList<ColumnAlignerModel>();
	private DonationNagger donationNagger = new DonationNagger();
	private int version = 0;

	public List<ColumnAlignerModel> getHistory() {
		return new ArrayList<ColumnAlignerModel>(history);
	}

	public void setHistory(List<ColumnAlignerModel> history) {
		this.history = history;
	}

	public DonationNagger getDonationNagger() {
		return donationNagger;
	}

	public void setDonationNagger(DonationNagger donationNagger) {
		this.donationNagger = donationNagger;
	}

	public void actionExecuted() {
		getDonationNagger().actionExecuted();
	}

	public void popup(final Project project) {
		if (version < 1) {
			version = 1;
			ApplicationManager.getApplication().invokeLater(() -> {
				Notification notification = NOTIFICATION.createNotification("String Manipulation popup", "You can now customize actions in the popup via <a href=\"#\">Settings | Appearance & Behavior | Menus and Toolbars</a>", NotificationType.INFORMATION,
					new NotificationListener.UrlOpeningListener(true) {
						@Override
						protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
							ApplicationManager.getApplication().invokeLater(() -> {
								ShowSettingsUtil.getInstance().showSettingsDialog(project, "Menus and Toolbars");

							});
						}
					});

				Notifications.Bus.notify(notification);
			});
		}
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@NotNull
	@Transient
	public ColumnAlignerModel getLastModel() {
		if (history.size() > 0) {
			return history.get(history.size() - 1);
		}
		return new ColumnAlignerModel();
	}

	@Transient
	public void addToHistory(ColumnAlignerModel columnAlignerModel) {
		List<ColumnAlignerModel> newList = new ArrayList<ColumnAlignerModel>(history.size() + 1);

		int startIndex = history.size() >= LIMIT ? 1 : 0;
		for (int i = startIndex; i < history.size(); i++) {
			ColumnAlignerModel alignerModel = history.get(i);
			if (!alignerModel.equals(columnAlignerModel)) {
				newList.add(alignerModel);
			}
		}

		columnAlignerModel.setAdded(new Date());
		newList.add(columnAlignerModel);

		history = newList;
	}

	public static PluginPersistentStateComponent getInstance() {
		return ServiceManager.getService(PluginPersistentStateComponent.class);
	}

	@Nullable
	@Override
	public PluginPersistentStateComponent getState() {
		return this;
	}

	@Override
	public void loadState(PluginPersistentStateComponent o) {
		XmlSerializerUtil.copyBean(o, this);

	}

}
