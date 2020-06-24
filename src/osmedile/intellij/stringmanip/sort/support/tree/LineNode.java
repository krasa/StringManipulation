package osmedile.intellij.stringmanip.sort.support.tree;

import osmedile.intellij.stringmanip.sort.support.SortLine;
import osmedile.intellij.stringmanip.sort.support.SortLines;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.sort.support.Sortable;

import java.util.ArrayList;
import java.util.List;

public class LineNode implements Sortable {
	private final int i;
	private final int level;
	public LineNode parent;
	SortLine actual;
	public List<LineNode> children = new ArrayList<>();

	public LineNode(SortSettings sortSettings, int i, String line, int level) {
		this.i = i;
		this.level = level;
		actual = new SortLine(line, sortSettings);
	}

	@Override
	public String getTextForComparison() {
		return actual.getTextForComparison();
	}

	@Override
	public String getText() {
		return actual.getText();
	}

	public void addChild(LineNode lineNode) {
		children.add(lineNode);
		lineNode.setParent(this);
	}

	private void setParent(LineNode parent) {
		this.parent = parent;
	}

	public LineNode findParentForLevel(int level) {
		if (parent == null) {
			return null;
		}
		if (this.level == level) {
			return parent;
		}
		return parent.findParentForLevel(level);
	}

	public void deepSort(SortSettings sortSettings) {
		if (sortSettings.isSortByGroups()) {
			children = SortLines.groupSort(children, sortSettings);
		} else {
			children = SortLines.normalSort(children, sortSettings, false);
		}
		for (LineNode child : children) {
			child.deepSort(sortSettings);
		}
	}

	public void flatten(List<String> result) {
		result.add(actual.getText());
		for (LineNode child : children) {
			child.flatten(result);
		}
	}

	@Override
	public String toString() {
		return "LineNode{" +
				", actual=" + actual +
				", children=" + children +
				'}';
	}

}
