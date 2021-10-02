package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.CaseSwitchingSettings;
import osmedile.intellij.stringmanip.CharacterSwitchingSettings;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;
import osmedile.intellij.stringmanip.escaping.normalize.NormalizationSettings;
import osmedile.intellij.stringmanip.filter.GrepSettings;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.sort.tokens.SortTokensModel;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;
import osmedile.intellij.stringmanip.styles.custom.DefaultActions;
import osmedile.intellij.stringmanip.utils.PreviewUtils;

import java.util.*;

@State(name = "StringManipulationState", storages = {@Storage("stringManipulation.xml")})
public class PluginPersistentStateComponent implements PersistentStateComponent<PluginPersistentStateComponent> {
	private static final Logger LOG = Logger.getInstance(PluginPersistentStateComponent.class);

	public static final int LIMIT = 20;
	private List<ColumnAlignerModel> columnAlignerHistory = new ArrayList<ColumnAlignerModel>();
	private List<CustomActionModel> customActionModels = DefaultActions.defaultActions();

	private int lastSelectedAction = 0;
	private int version = 0;
	private SortSettings sortSettings = new SortSettings();
	private CaseSwitchingSettings caseSwitchingSettings = new CaseSwitchingSettings();
	private CharacterSwitchingSettings characterSwitchingSettings = new CharacterSwitchingSettings();
	private SortTokensModel sortTokensModel;
	private boolean doNotAddSelection;
	private NormalizationSettings normalizationSettings = new NormalizationSettings();

	private List<GrepSettings> grepHistory = new ArrayList<>();

	public PluginPersistentStateComponent() {
	}

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

	public CharacterSwitchingSettings getCharacterSwitchingSettings() {
		return characterSwitchingSettings;
	}

	public void setCharacterSwitchingSettings(CharacterSwitchingSettings characterSwitchingSettings) {
		this.characterSwitchingSettings = characterSwitchingSettings;
	}

	public SortSettings getSortSettings() {
		return sortSettings;
	}

	public void setSortSettings(SortSettings sortSettings) {
		this.sortSettings = sortSettings;
	}

	public List<ColumnAlignerModel> getColumnAlignerHistory() {
		return columnAlignerHistory;
	}

	public void setColumnAlignerHistory(List<ColumnAlignerModel> columnAlignerHistory) {
		this.columnAlignerHistory = columnAlignerHistory;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@NotNull
	@Transient
	public ColumnAlignerModel guessModel(Editor editor) {
		String s = PreviewUtils.getTextForPreview(editor);
		if (columnAlignerHistory.size() > 0) {
			for (int i = columnAlignerHistory.size() - 1; i >= 0; i--) {
				ColumnAlignerModel columnAlignerModel = columnAlignerHistory.get(i);
				List<String> separators = columnAlignerModel.getSeparators();
				for (String separator : separators) {
					if (s.contains(separator)) {
						return columnAlignerModel;
					}
				}
			}
		}


		ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel();
		List<String> separators = columnAlignerModel.getSeparators();
		//TODO configurable?
		addSeparator(s, "|", false, separators);
		addSeparator(s, ";", false, separators);
		addSeparator(s, "->", false, separators);
		addSeparator(s, "<-", false, separators);
		addSeparator(s, "-", true, separators);
		addSeparator(s, ":", true, separators);
		addSeparator(s, ",", true, separators);
		addSeparator(s, " ", true, separators);
		return columnAlignerModel;
	}

	@Transient
	public void addToHistory(ColumnAlignerModel columnAlignerModel) {
		List<ColumnAlignerModel> newList = new ArrayList<ColumnAlignerModel>(columnAlignerHistory.size() + 1);

		int startIndex = columnAlignerHistory.size() >= LIMIT ? 1 : 0;
		for (int i = startIndex; i < columnAlignerHistory.size(); i++) {
			ColumnAlignerModel alignerModel = columnAlignerHistory.get(i);
			if (!alignerModel.equals(columnAlignerModel)) {
				newList.add(alignerModel);
			}
		}

		columnAlignerModel.setAdded(new Date());
		newList.add(columnAlignerModel);

		columnAlignerHistory = newList;
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

			Iterator<CustomActionModel.Step> stepIterator = steps.iterator();
			while (stepIterator.hasNext()) {
				CustomActionModel.Step next = stepIterator.next();
				Style stepStyle = next.getStyleAsEnum();
				if (stepStyle == null) {
					stepIterator.remove();
				}
			}
			if (!styles.isEmpty()) {
				for (Style style : styles) {
					Boolean enabled = DefaultActions.DEFAULT_AS_MAP.get(style);
					if (enabled == null) {
						enabled = false;
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

	public SortTokensModel guessSortTokensModel(Editor editor) {
		String s = PreviewUtils.getTextForPreview(editor);

		SortTokensModel model = sortTokensModel != null ? sortTokensModel : new SortTokensModel();
		List<String> separators = model.getSeparators();
		separators.removeIf(next -> next.length() == 0 || !s.contains(next));

		//TODO configurable?
		addSeparator(s, "|", true, separators);
		addSeparator(s, ",", true, separators);
		addSeparator(s, "->", true, separators);
		addSeparator(s, "<-", true, separators);
		addSeparator(s, "-", true, separators);
		addSeparator(s, ";", true, separators);
		addSeparator(s, ":", true, separators);
		addSeparator(s, " ", true, separators);
		return model;
	}

	private void addSeparator(String s, String separator, boolean skipIfNotEmpty, List<String> separators) {
		if (!separators.isEmpty() && skipIfNotEmpty) {
			return;
		}
		if (s.contains(separator)) {
			separators.add(separator);
		}
	}

	public void storeModel(SortTokensModel settings) {
		this.sortTokensModel = settings;
	}

	public boolean isDoNotAddSelection() {
		return doNotAddSelection;
	}

	public void setDoNotAddSelection(final boolean doNotAddSelection) {
		this.doNotAddSelection = doNotAddSelection;
	}

	public void setNormalizeSettings(NormalizationSettings newSettings) {
		normalizationSettings = newSettings;
	}

	public NormalizationSettings getNormalizeSettings() {
		return normalizationSettings;
	}


	@Transient
	public void addToHistory(GrepSettings grepSettings) {
		List<GrepSettings> newList = new ArrayList<>(grepHistory.size() + 1);

		int startIndex = grepHistory.size() >= LIMIT ? 1 : 0;
		for (int i = startIndex; i < grepHistory.size(); i++) {
			GrepSettings settings = grepHistory.get(i);
			if (!settings.equals(grepSettings)) {
				newList.add(settings);
			}
		}

		grepSettings.setAdded(new Date());
		newList.add(grepSettings);

		grepHistory = newList;
	}

	@NotNull
	@Transient
	public GrepSettings guessSettings(String text) {
		GrepSettings settings = new GrepSettings();
		if (grepHistory.size() > 0) {
			settings = grepHistory.get(grepHistory.size() - 1);
			for (int i = grepHistory.size() - 1; i >= 0; i--) {
				GrepSettings s = grepHistory.get(i);
				if (text.equals(s.getPattern())) {
					settings = s;
					break;
				}
			}
		} else {
			settings.setPattern(text);
		}
		return settings;
	}

	public void setGrepHistory(List<GrepSettings> grepHistory) {
		this.grepHistory = grepHistory;
	}

	public List<GrepSettings> getGrepHistory() {
		return grepHistory;
	}

}
