package osmedile.intellij.stringmanip.sort.support.tree;

import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.sort.support.SortLine;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrailingCharacterTransformer {
	private SortSettings sortSettings;

	private final Children root;
	Map<LineNode, Children> allParents = new HashMap<>();

	public TrailingCharacterTransformer(SortSettings sortSettings, List<LineNode> roots) {
		this.sortSettings = sortSettings;
		root = new Children(roots);
	}

	String transform(LineNode realNode, LineNode textNode) {
		if (sortSettings.isPreserveTrailingSpecialCharacters()) {
			if (realNode.parent != null) {
				int i = realNode.parent.children.indexOf(realNode);
				SortLine from = get(realNode.parent, i);
				if (from != null) {
					return from.transformTo(textNode.actual).getText();
				}
			}
		}
		return textNode.actual.getText();
	}

	private SortLine get(LineNode parent, int i) {
		if (parent == null) {
			LineNode lineNode = root.get(i);
			return toLine(lineNode);
		}

		Children children = allParents.get(parent);
		if (children != null) {
			LineNode lineNode = children.get(i);
			return toLine(lineNode);
		}
		return null;
	}

	@NotNull
	private SortLine toLine(LineNode lineNode) {
		LineNode closingLine = lineNode.closingLine;
		if (closingLine != null) {
			return new SortLine(closingLine.getText(), sortSettings);
		}
		return new SortLine(lineNode.getText(), sortSettings);
	}


	private class Children {
		private List<LineNode> children = new ArrayList<>();

		public Children(List<LineNode> children) {
			for (int i = 0; i < children.size(); i++) {
				LineNode child = children.get(i);
				allParents.put(child, new Children(child.children));
				this.children.add(child);
			}
		}

		public LineNode get(int i) {
			return children.get(i);
		}
	}
}
