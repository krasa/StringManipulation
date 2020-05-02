package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.util.TextRange;
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
import osmedile.intellij.stringmanip.utils.IdeUtils;

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
		List<CaretState> caretsAndSelections = editor.getCaretModel().getCaretsAndSelections();
		IdeUtils.sort(caretsAndSelections);
		StringBuilder sb = new StringBuilder();
		for (CaretState caretsAndSelection : caretsAndSelections) {
			LogicalPosition selectionStart = caretsAndSelection.getSelectionStart();
			LogicalPosition selectionEnd = caretsAndSelection.getSelectionEnd();
			String text = editor.getDocument().getText(
				new TextRange(editor.logicalPositionToOffset(selectionStart),
					editor.logicalPositionToOffset(selectionEnd)));

			sb.append(text);
			if (sb.length() > 10000) {
				break;
			}
		}
		String s = sb.toString();
		s = s.substring(0, Math.min(10000, s.length()));

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
		//TODO configurable?
		addSeparator(s, columnAlignerModel, "|", false);
		addSeparator(s, columnAlignerModel, ";", false);
		addSeparator(s, columnAlignerModel, "->", false);
		addSeparator(s, columnAlignerModel, "<-", false);
		addSeparator(s, columnAlignerModel, "-", true);
		addSeparator(s, columnAlignerModel, ":", true);
		addSeparator(s, columnAlignerModel, ",", true);
		return columnAlignerModel;
	}

	private void addSeparator(String s, ColumnAlignerModel columnAlignerModel, String separator, boolean skipIfNotEmpty) {
		if (!columnAlignerModel.getSeparators().isEmpty() && skipIfNotEmpty) {
			return;
		}
		if (s.contains(separator)) {
			columnAlignerModel.getSeparators().add(separator);
		}
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
