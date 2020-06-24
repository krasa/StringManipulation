package osmedile.intellij.stringmanip.sort.support.tree;

import osmedile.intellij.stringmanip.sort.support.SortLines;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import shaded.org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HierarchicalSort {
	private final List<String> originalLines;
	private final SortSettings sortSettings;

	public HierarchicalSort(List<String> originalLines, SortSettings sortSettings) {
		this.originalLines = originalLines;
		this.sortSettings = sortSettings;
	}

	public List<String> sort() {
		List<LineNode> roots = new TreeBuilder().prepareTree(originalLines);
		roots = deepSort(roots);
		return toTextLines(roots);
	}

	private List<String> toTextLines(List<LineNode> roots) {
		List<String> result = new ArrayList<>();
		for (LineNode root : roots) {
			root.flatten(result);
		}
		return result;
	}

	private List<LineNode> deepSort(List<LineNode> roots) {
		LineNode virtualRoot = new LineNode(sortSettings, -1, "", -1);
		virtualRoot.children.addAll(roots);

		virtualRoot.deepSort(sortSettings);

		return virtualRoot.children;
	}


	private class TreeBuilder {
		List<LineNode> lineNodes = new ArrayList<>();
		List<LineNode> lineBreaks = new ArrayList<>();
		int prevLevel = -1;
		LineNode currentNode = null;
		LineNode blankLine = null;

		private List<LineNode> prepareTree(List<String> originalLines) {
			String levelRegexp = sortSettings.getLevelRegex();
			Pattern pattern = Pattern.compile(levelRegexp);

			for (int i = 0; i < originalLines.size(); i++) {
				String line = originalLines.get(i);
				if (StringUtils.isBlank(line)) {
					lineBreaks.add(blankLine = new LineNode(sortSettings, i, line, prevLevel));
					continue;
				}
				int level = SortLines.level(line, pattern);

				if (currentNode == null) {
					addBlankLine(lineNodes);
					lineNodes.add(currentNode = new LineNode(sortSettings, i, line, level));
					prevLevel = level;
					continue;
				}

				if (prevLevel == level) {
					LineNode parent = currentNode.parent;
					if (parent == null) {
						addBlankLine(lineNodes);
						lineNodes.add(currentNode = new LineNode(sortSettings, i, line, level));
					} else {
						addBlankLine(parent);
						parent.addChild(currentNode = new LineNode(sortSettings, i, line, level));
					}
				} else if (prevLevel < level) {
					addBlankLine(currentNode);
					LineNode child = new LineNode(sortSettings, i, line, level);
					currentNode.addChild(child);
					currentNode = child;
				} else if (level < prevLevel) {
					LineNode parent = currentNode.findParentForLevel(level);
					if (parent == null) {
						addBlankLine(lineNodes);
						lineNodes.add(currentNode = new LineNode(sortSettings, i, line, level));
					} else {
						addBlankLine(parent);
						parent.addChild(currentNode = new LineNode(sortSettings, i, line, level));
					}
				}
				prevLevel = level;
			}


			return lineNodes;
		}

		private void addBlankLine(LineNode parent) {
			if (blankLine != null) {
				parent.addChild(blankLine);
				blankLine = null;
			}
		}

		private void addBlankLine(List<LineNode> lineNodes) {
			if (blankLine != null) {
				lineNodes.add(blankLine);
				blankLine = null;
			}
		}
	}
}
