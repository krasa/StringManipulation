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
		TrailingCharacterTransformer trailingCharacterTransformer = new TrailingCharacterTransformer(sortSettings, roots);
		roots = deepSort(roots);
		return toTextLines(roots, trailingCharacterTransformer);
	}

	private List<String> toTextLines(List<LineNode> roots, TrailingCharacterTransformer trailingCharacterTransformer) {
		List<String> result = new ArrayList<>();
		for (LineNode root : roots) {
			root.flatten(result, trailingCharacterTransformer);
		}
		return result;
	}

	private List<LineNode> deepSort(List<LineNode> roots) {
		LineNode virtualRoot = new LineNode(sortSettings, "", -1);
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
			Pattern levelPattern = Pattern.compile(sortSettings.getLevelRegex());
			Pattern groupSeparatorRegexPattern = Pattern.compile(sortSettings.getGroupSeparatorRegex());
			Pattern groupClosingLineRegexPattern = null;
			if (sortSettings.isGroupClosingLineRegexEnabled()) {
				groupClosingLineRegexPattern = Pattern.compile(sortSettings.getGroupClosingLineRegex());
			}

			for (int i = 0; i < originalLines.size(); i++) {
				String line = originalLines.get(i);
				if (groupSeparatorRegexPattern.matcher(line).matches()) {
					lineBreaks.add(breakLine = new LineNode(sortSettings, line, prevLevel));
					continue;
				}
				int level = SortLines.level(line, levelPattern);


				if (currentNode != null && groupClosingLineRegexPattern != null && groupClosingLineRegexPattern.matcher(line).matches()) {
					LineNode node = currentNode.findNodeForLevel(level);
					node.setClosingLine(new LineNode(sortSettings, line, prevLevel));
					currentNode = node;
					prevLevel = currentNode.getLevel();
					continue;
				}

				if (currentNode == null) {
					addBreakLine(lineNodes);
					lineNodes.add(currentNode = new LineNode(sortSettings, line, level));
					prevLevel = level;
					continue;
				}

				if (prevLevel == level) {
					LineNode parent = currentNode.parent;
					if (parent == null) {
						addBreakLine(lineNodes);
						lineNodes.add(currentNode = new LineNode(sortSettings, line, level));
					} else {
						addBreakLine(parent);
						parent.addChild(currentNode = new LineNode(sortSettings, line, level));
					}
				} else if (prevLevel < level) {
					addBreakLine(currentNode);
					LineNode child = new LineNode(sortSettings, line, level);
					currentNode.addChild(child);
					currentNode = child;
				} else if (level < prevLevel) {
					LineNode parent = currentNode.findParentOfLevel(level);
					if (parent == null) {
						addBreakLine(lineNodes);
						lineNodes.add(currentNode = new LineNode(sortSettings, line, level));
					} else {
						addBreakLine(parent);
						parent.addChild(currentNode = new LineNode(sortSettings, line, level));
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
