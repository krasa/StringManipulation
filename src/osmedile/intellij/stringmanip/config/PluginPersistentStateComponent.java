package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.CaseSwitchingSettings;
import osmedile.intellij.stringmanip.CharacterSwitchingSettings;
import osmedile.intellij.stringmanip.UniversalActionModel;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;
import osmedile.intellij.stringmanip.border.BorderSettings;
import osmedile.intellij.stringmanip.escaping.normalize.NormalizationSettings;
import osmedile.intellij.stringmanip.filter.GrepSettings;
import osmedile.intellij.stringmanip.replace.gui.ReplaceCompositeModel;
import osmedile.intellij.stringmanip.replace.gui.ReplaceItemModel;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.sort.tokens.SortTokensModel;
import osmedile.intellij.stringmanip.styles.Style;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;
import osmedile.intellij.stringmanip.styles.custom.DefaultActions;
import osmedile.intellij.stringmanip.utils.Cloner;
import osmedile.intellij.stringmanip.utils.PreviewDialog;

import java.util.*;

@State(name = "StringManipulationState", storages = {@Storage("stringManipulation.xml")})
public class PluginPersistentStateComponent implements PersistentStateComponent<PluginPersistentStateComponent> {
	private static final Logger LOG = Logger.getInstance(PluginPersistentStateComponent.class);

	public static final int MAX_HISTORY = 20;
	private List<ColumnAlignerModel> columnAlignerHistory = new ArrayList<ColumnAlignerModel>();
	private List<CustomActionModel> customActionModels = DefaultActions.defaultActions();
	private List<UniversalActionModel> universalActions = new ArrayList<>();

	private int lastSelectedAction = 0;
	private int version = 0;
	private Map<String, SortSettings> sortSettingsMap = new HashMap<>();
	@Deprecated
	private SortSettings sortSettings = new SortSettings();

	private CaseSwitchingSettings caseSwitchingSettings = new CaseSwitchingSettings();
	private CharacterSwitchingSettings characterSwitchingSettings = new CharacterSwitchingSettings();
	private SortTokensModel sortTokensModel = new SortTokensModel();
	private boolean doNotAddSelection;
	private NormalizationSettings normalizationSettings = new NormalizationSettings();

	private List<GrepSettings> grepHistory = new ArrayList<>();
	private List<ReplaceCompositeModel> replaceHistory = new ArrayList<>();
	private UniversalActionModel lastAction;
	private boolean repeatLastActionWithoutDialog = true;
	private BorderSettings borderSettings = new BorderSettings();
	private boolean normalizeCaseSwitching = true;

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

	public Map<String, SortSettings> getSortSettingsMap() {
		return sortSettingsMap;
	}

	public void setSortSettingsMap(Map<String, SortSettings> sortSettingsMap) {
		this.sortSettingsMap = sortSettingsMap;
	}

	public SortSettings getSortSettings() {
		return sortSettings;
	}

	public void setSortSettings(SortSettings sortSettings) {
		this.sortSettings = sortSettings;
	}

	public SortTokensModel getSortTokensModel() {
		return sortTokensModel;
	}

	public void setSortTokensModel(SortTokensModel sortTokensModel) {
		this.sortTokensModel = sortTokensModel;
	}

	public NormalizationSettings getNormalizationSettings() {
		return normalizationSettings;
	}

	public void setNormalizationSettings(NormalizationSettings normalizationSettings) {
		this.normalizationSettings = normalizationSettings;
	}

	public SortSettings getSortSettings(String storeKey) {
		SortSettings settings = sortSettingsMap.get(storeKey);
		if (settings == null) {
			return new SortSettings();
		}
		return settings;
	}

	public void storeSortSettings(String storeKey, SortSettings sortSettings) {
		this.sortSettingsMap.put(storeKey, sortSettings);
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
		String s = PreviewDialog.getTextForPreview(editor);
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

		int startIndex = columnAlignerHistory.size() >= MAX_HISTORY ? 1 : 0;
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
		return ApplicationManager.getApplication().getService(PluginPersistentStateComponent.class);
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
		String s = PreviewDialog.getTextForPreview(editor);

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

		int startIndex = grepHistory.size() >= MAX_HISTORY ? 1 : 0;
		for (int i = startIndex; i < grepHistory.size(); i++) {
			GrepSettings settings = grepHistory.get(i);
			if (!settings.equals(grepSettings)) {
				newList.add(settings);
			}
		}

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
		}
		GrepSettings clone = Cloner.deepClone(settings);
		if (StringUtils.isNotEmpty(text)) {
			clone.setPattern(text);
		}
		clone.quick = false;
		return clone;
	}

	@NotNull
	@Transient
	public GrepSettings guessSettings(String text, boolean inverted) {
		GrepSettings settings = null;
		//matching
		if (grepHistory.size() > 0) {
			for (int i = grepHistory.size() - 1; i >= 0; i--) {
				GrepSettings s = grepHistory.get(i);
				if (text.equals(s.getPattern()) && s.isInverted() == inverted) {
					settings = s;
					break;
				}
			}
		}
		//last
		if (grepHistory.size() > 0) {
			for (int i = grepHistory.size() - 1; i >= 0; i--) {
				GrepSettings s = grepHistory.get(i);
				if (s.isInverted() == inverted) {
					settings = s;
					break;
				}
			}
		}

		if (settings == null) {
			settings = new GrepSettings();
		}

		GrepSettings clone = Cloner.deepClone(settings);
		if (StringUtils.isNotEmpty(text)) {
			clone.setPattern(text);
		}
		clone.setInverted(inverted);
		clone.quick = false;
		return clone;
	}

	public void setGrepHistory(List<GrepSettings> grepHistory) {
		this.grepHistory = grepHistory;
	}

	public List<GrepSettings> getGrepHistory() {
		return grepHistory;
	}

	public List<ReplaceCompositeModel> getReplaceHistory() {
		return replaceHistory;
	}

	public void setReplaceHistory(List<ReplaceCompositeModel> replaceHistory) {
		this.replaceHistory = replaceHistory;
	}

	public List<ReplaceItemModel> getReplaceModels() {
		ArrayList<ReplaceItemModel> replaceItemModels = new ArrayList<>();
		for (ReplaceCompositeModel replaceCompositeModel : replaceHistory) {
			replaceItemModels.addAll(replaceCompositeModel.getItems());
		}
		return replaceItemModels;
	}


	public void addToHistory(ReplaceCompositeModel compositeModel) {
		if (!compositeModel.isAnyEnabledAndValid()) {
			return;
		}
		compositeModel = Cloner.deepClone(compositeModel);
		compositeModel.setDate(new Date());
		compositeModel.removeEmpty();
		ReplaceCompositeModel finalCompositeModel = compositeModel;
		replaceHistory.removeIf(m -> Objects.equals(m.getItems(), finalCompositeModel.getItems()));
		replaceHistory.add(compositeModel);
		while (replaceHistory.size() > MAX_HISTORY) {
			replaceHistory.remove(0);
		}
	}

	public ReplaceCompositeModel getLastReplaceModel() {
		if (!replaceHistory.isEmpty()) {
			return replaceHistory.get(replaceHistory.size() - 1);
		}
		return null;
	}

	public UniversalActionModel getLastAction() {
		return lastAction;
	}

	public void setLastAction(UniversalActionModel lastAction) {
		this.lastAction = lastAction;
	}

	public boolean isRepeatLastActionWithoutDialog() {
		return repeatLastActionWithoutDialog;
	}

	public void setRepeatLastActionWithoutDialog(final boolean repeatLastActionWithoutDialog) {
		this.repeatLastActionWithoutDialog = repeatLastActionWithoutDialog;
	}

	public BorderSettings getBorderSettings() {
		return borderSettings;
	}

	public void setBorderSettings(BorderSettings borderSettings) {
		this.borderSettings = borderSettings;
	}

	public boolean isNormalizeCaseSwitching() {
		return normalizeCaseSwitching;
	}

	public void setNormalizeCaseSwitching(final boolean normalizeCaseSwitching) {
		this.normalizeCaseSwitching = normalizeCaseSwitching;
	}
}
