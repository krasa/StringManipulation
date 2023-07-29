package osmedile.intellij.stringmanip.align;

import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.List;

public class CellAligner {

	private final ColumnAlignerModel model;
	private int maxCurrentTokenLength;

	public CellAligner(ColumnAlignerModel model, List<ColumnAlignerLine> lines) {
		this.model = model;
		maxCurrentTokenLength = 0;
		for (ColumnAlignerLine line : lines) {
			maxCurrentTokenLength = Math.max(maxCurrentTokenLength, line.currentTokenLength());
		}
	}

	public String align(String str) {
		if (str.isEmpty()) {
			return str;
		}

		if (model.isRightAlign()) {
			return StringUtil.rightAlign(str, maxCurrentTokenLength);
		} else if (model.isRightAlignNumbers()) {
			return StringUtil.rightAlignNumber(str, maxCurrentTokenLength);
		} else {

			return str;
		}
	}
}
