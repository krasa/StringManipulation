package osmedile.intellij.stringmanip.config;

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
import osmedile.intellij.stringmanip.CaseSwitchingSettings;
import osmedile.intellij.stringmanip.DonationNagger;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.action.DefaultActions;
import osmedile.intellij.stringmanip.styles.action.StyleActionModel;
import osmedile.intellij.stringmanip.styles.action.StyleStep;

import javax.swing.event.HyperlinkEvent;
import java.util.*;

import static osmedile.intellij.stringmanip.DonationNagger.NOTIFICATION;

@State(name = "StringManipulationState", storages = {@Storage("stringManipulation.xml")})
public class PluginPersistentStateComponent implements PersistentStateComponent<PluginPersistentStateComponent> {

	public static final int LIMIT = 20;
	private List<ColumnAlignerModel> history = new ArrayList<ColumnAlignerModel>();
	private List<StyleActionModel> styleActionModels = new ArrayList<>();
	private int lastSelectedAction = 0;
	private DonationNagger donationNagger = new DonationNagger();
	private int version = 0;
	private SortSettings sortSettings = new SortSettings();
	private CaseSwitchingSettings caseSwitchingSettings = new CaseSwitchingSettings();

	public int getLastSelectedAction() {
		return lastSelectedAction;
	}

	public void setLastSelectedAction(int lastSelectedAction) {
		this.lastSelectedAction = lastSelectedAction;
	}

	public List<StyleActionModel> getStyleActionModels() {
		return styleActionModels;
	}

	public void setStyleActionModels(List<StyleActionModel> styleActionModels) {
		this.styleActionModels = styleActionModels;
	}

	public CaseSwitchingSettings getCaseSwitchingSettings() {
		return caseSwitchingSettings;
	}

	public void setCaseSwitchingSettings(CaseSwitchingSettings caseSwitchingSettings) {
		this.caseSwitchingSettings = caseSwitchingSettings;
	}

	public SortSettings getSortSettings() {
		return sortSettings;
	}

	public void setSortSettings(SortSettings sortSettings) {
		this.sortSettings = sortSettings;
	}

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

	private static PluginPersistentStateComponent unitTestComponent;

	public static PluginPersistentStateComponent getInstance() {
		if (ApplicationManager.getApplication() == null) {
			if (unitTestComponent == null) {
				unitTestComponent = new PluginPersistentStateComponent();
			}
			return unitTestComponent;
		}
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
		fixSteps();
	}

	private void fixSteps() {
		for (StyleActionModel styleActionModel : styleActionModels) {

			Set<Style> styles = new HashSet<>();
			styles.addAll(Arrays.asList(Style.values()));

			List<StyleStep> steps = styleActionModel.getSteps();
			for (StyleStep step : steps) {
				Style stepStyle = step.getStyleAsEnum();
				styles.remove(stepStyle);
			}

			if (!styles.isEmpty()) {
				Iterator<StyleStep> stepIterator = steps.iterator();
				while (stepIterator.hasNext()) {
					StyleStep next = stepIterator.next();
					Style stepStyle = next.getStyleAsEnum();
					if (stepStyle == null) {
						stepIterator.remove();
					}
				}
				for (Style style : styles) {
					Boolean enabled = DefaultActions.DEFAULT_AS_MAP.get(style);
					if (enabled == null) {
						enabled = true;
					}
					steps.add(new StyleStep(enabled, style));
				}
			}
		}
	}

	public void resetDefaultActions() {
		boolean exists = false;
		for (StyleActionModel styleActionModel : styleActionModels) {
			if (DefaultActions.SWITCH_STYLE_ACTION.equals(styleActionModel.getId())) {
				exists = true;
				DefaultActions.resetDefaultSwitchCase(styleActionModel);
			}
		}
		if (!exists) {
			styleActionModels.add(DefaultActions.defaultSwitchCase());
		}
	}

}
