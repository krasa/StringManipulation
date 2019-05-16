package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.CaseSwitchingSettings;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;
import osmedile.intellij.stringmanip.styles.custom.DefaultActions;

import java.util.*;

@State(name = "StringManipulationState", storages = {@Storage("stringManipulation.xml")})
public class PluginPersistentStateComponent implements PersistentStateComponent<PluginPersistentStateComponent> {
	private static final Logger LOG = Logger.getInstance(PluginPersistentStateComponent.class);

	public static final int LIMIT = 20;
	private List<ColumnAlignerModel> history = new ArrayList<ColumnAlignerModel>();
	private List<CustomActionModel> customActionModels = DefaultActions.defaultActions();

	private int lastSelectedAction = 0;
	private int version = 0;
	private SortSettings sortSettings = new SortSettings();
	private CaseSwitchingSettings caseSwitchingSettings = new CaseSwitchingSettings();

	public int getLastSelectedAction() {
		return lastSelectedAction;
	}

	public void setLastSelectedAction(int lastSelectedAction) {
		this.lastSelectedAction = lastSelectedAction;
	}

	public List<CustomActionModel> getCustomActionModels() {
		return customActionModels;
	}

	public void setCustomActionModels(List<CustomActionModel> customActionModels) {
		this.customActionModels = customActionModels;
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
		fixActions();
	}

	private void fixActions() {
		for (CustomActionModel customActionModel : customActionModels) {
			Set<Style> styles = new HashSet<>();
			styles.addAll(Arrays.asList(Style.values()));

			List<CustomActionModel.Step> steps = customActionModel.getSteps();
			for (CustomActionModel.Step step : steps) {
				Style stepStyle = step.getStyleAsEnum();
				styles.remove(stepStyle);
			}

			if (!styles.isEmpty()) {
				Iterator<CustomActionModel.Step> stepIterator = steps.iterator();
				while (stepIterator.hasNext()) {
					CustomActionModel.Step next = stepIterator.next();
					Style stepStyle = next.getStyleAsEnum();
					if (stepStyle == null) {
						stepIterator.remove();
					}
				}
				for (Style style : styles) {
					Boolean enabled = DefaultActions.DEFAULT_AS_MAP.get(style);
					if (enabled == null) {
						enabled = !style.name().startsWith("_");
					}
					steps.add(new CustomActionModel.Step(enabled, style));
				}
			}
		}
	}

	public void resetDefaultActions() {
		boolean exists = false;
		for (CustomActionModel customActionModel : customActionModels) {
			if (DefaultActions.SWITCH_STYLE_ACTION.equals(customActionModel.getId())) {
				exists = true;
				DefaultActions.resetDefaultSwitchCase(customActionModel);
			}
		}
		if (!exists) {
			customActionModels.add(DefaultActions.defaultSwitchCase());
		}
	}

}
