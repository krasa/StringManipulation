package osmedile.intellij.stringmanip.sort.support.tree;

import osmedile.intellij.stringmanip.sort.support.SortLines;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

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
		LineNode breakLine = null;

		private List<LineNode> prepareTree(List<String> originalLines) {
			String levelRegexp = sortSettings.getLevelRegex();
			String groupSeparatorRegex = sortSettings.getGroupSeparatorRegex();
			Pattern levelPattern = Pattern.compile(levelRegexp);
			Pattern groupSeparatorRegexPattern = Pattern.compile(groupSeparatorRegex);

			for (int i = 0; i < originalLines.size(); i++) {
				String line = originalLines.get(i);
				if (groupSeparatorRegexPattern.matcher(line).matches()) {
					lineBreaks.add(breakLine = new LineNode(sortSettings, i, line, prevLevel));
					continue;
				}
				int level = SortLines.level(line, levelPattern);

				if (currentNode == null) {
					addBreakLine(lineNodes);
					lineNodes.add(currentNode = new LineNode(sortSettings, i, line, level));
					prevLevel = level;
					continue;
				}

				if (prevLevel == level) {
					LineNode parent = currentNode.parent;
					if (parent == null) {
						addBreakLine(lineNodes);
						lineNodes.add(currentNode = new LineNode(sortSettings, i, line, level));
					} else {
						addBreakLine(parent);
						parent.addChild(currentNode = new LineNode(sortSettings, i, line, level));
					}
				} else if (prevLevel < level) {
					addBreakLine(currentNode);
					LineNode child = new LineNode(sortSettings, i, line, level);
					currentNode.addChild(child);
					currentNode = child;
				} else if (level < prevLevel) {
					LineNode parent = currentNode.findParentForLevel(level);
					if (parent == null) {
						addBreakLine(lineNodes);
						lineNodes.add(currentNode = new LineNode(sortSettings, i, line, level));
					} else {
						addBreakLine(parent);
						parent.addChild(currentNode = new LineNode(sortSettings, i, line, level));
					}
				}
				prevLevel = level;
			}


			return lineNodes;
		}

		private void addBreakLine(LineNode parent) {
			if (breakLine != null) {
				parent.addChild(breakLine);
				breakLine = null;
			}
		}

		private void addBreakLine(List<LineNode> lineNodes) {
			if (breakLine != null) {
				lineNodes.add(breakLine);
				breakLine = null;
			}
		}
	}
}
