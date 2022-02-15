package osmedile.intellij.stringmanip.replace.gui;

import com.intellij.find.FindModel;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.replace.MyFindUtil;

import java.util.*;

public class ReplaceCompositeModel {
	List<ReplaceItemModel> items = new ArrayList<ReplaceItemModel>();
	Date date;

	public ReplaceCompositeModel() {
	}

	@NotNull
	public String replace(Project project, String s) {
		Set<String> exclusiveGroupsUsed = new HashSet<>();

		DocumentImpl document = new DocumentImpl(s);
		for (ReplaceItemModel replaceItemModel : getItems()) {
			if (replaceItemModel.isEnabled()) {
				FindModel model = replaceItemModel.toFindModel();
				String exclusiveGroup = replaceItemModel.getExclusiveGroup();
				if (StringUtils.isNotBlank(exclusiveGroup)) {
					if (exclusiveGroupsUsed.contains(exclusiveGroup)) {
						continue;
					}
				}
				if (model != null) {
					if (MyFindUtil.doReplace(project, model, 0, document)) {
						exclusiveGroupsUsed.add(exclusiveGroup);
					}
				}
			}
		}
		return document.getText();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ReplaceCompositeModel(ReplaceItemModel selectedItem) {
		add(selectedItem);
		date = new Date();
	}


	public List<ReplaceItemModel> getItems() {
		return items;
	}

	public void setItems(List<ReplaceItemModel> items) {
		this.items = items;
	}

	public void add(ReplaceItemModel replaceItemModel) {
		items.add(replaceItemModel);
	}


	public boolean isValid() {
		for (ReplaceItemModel model : items) {
			if (model.isValid()) {
				return true;
			}
		}
		return false;

	}

	public ReplaceItemModel newItem() {
		String exclusiveGroup = nextExclusiveGroup();
		ReplaceItemModel e = new ReplaceItemModel();
		e.setExclusiveGroup(exclusiveGroup);
		items.add(e);
		return e;
	}

	@NotNull
	private String nextExclusiveGroup() {
		int max = 0;
		for (ReplaceItemModel item : items) {
			int i = 0;
			try {
				i = Integer.parseInt(item.getExclusiveGroup());
			} catch (Throwable e) {
				i = 0;
			}
			max = Math.max(max, i);
		}
		return String.valueOf(max + 1);
	}

	public boolean remove(ReplaceItemModel grepModel) {
		return items.remove(grepModel);
	}

	public void moveUp(ReplaceItemModel model) {
//		removeEmpty();
		int i = items.indexOf(model);
		if (i > 0) {
			Collections.swap(items, i, i - 1);
		}
	}

	public void moveDown(ReplaceItemModel model) {
//		removeEmpty();
		int i = items.indexOf(model);
		if (i >= 0 && i + 1 < items.size()) {//removeEmptyItems could remove it
			Collections.swap(items, i, i + 1);
		}
	}

	public void removeEmpty() {
		items.removeIf(ReplaceItemModel::isEmpty);
	}

	@Override
	public String toString() {
		return "ReplaceCompositeModel{" +
				"items=" + items +
				", date=" + date +
				'}';
	}

	public boolean isAnyEnabledAndValid() {
		for (ReplaceItemModel item : items) {
			if (item.isEnabled() && item.isValid()) {
				return true;
			}
		}
		return false;
	}
}
