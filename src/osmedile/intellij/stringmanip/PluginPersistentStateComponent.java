package osmedile.intellij.stringmanip;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@State(name = "StringManipulationState", storages = {@Storage(id = "StringManipulationState", file = "$APP_CONFIG$/stringManipulation.xml")})
public class PluginPersistentStateComponent implements PersistentStateComponent<PluginPersistentStateComponent> {

	public static final int LIMIT = 20;
	private List<ColumnAlignerModel> history = new ArrayList<ColumnAlignerModel>();

	public List<ColumnAlignerModel> getHistory() {
		return new ArrayList<ColumnAlignerModel>(history);
	}

	public void setHistory(List<ColumnAlignerModel> history) {
		this.history = history;
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
