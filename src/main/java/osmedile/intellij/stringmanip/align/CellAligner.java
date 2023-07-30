package osmedile.intellij.stringmanip.align;

import org.apache.commons.lang3.StringUtils;
import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.List;

public class CellAligner {

	private final ColumnAlignerModel model;
	private int maxCurrentTokenLength;

	public CellAligner(ColumnAlignerModel model, List<ColumnAlignerLine> lines) {
		this.model = model;
		maxCurrentTokenLength = 0;

		String decimalPlaceSeparator = model.getDecimalPlaceSeparator();
		if (model.isAlignDecimalSeparator() && !decimalPlaceSeparator.isBlank()) {
			int maxDecimalLength = 0;

			for (ColumnAlignerLine line : lines) {
				if (line.hasToken()) {
					String token = line.currentToken();
					if (StringUtil.isNumber(token)) {
						maxDecimalLength = Math.max(maxDecimalLength, decimalLength(token, decimalPlaceSeparator));
					}
				}
			}


			if (maxDecimalLength > 0) {
				for (ColumnAlignerLine line : lines) {
					if (line.hasToken()) {
						String token = line.currentToken();
						if (StringUtil.isNumber(token)) {
							int decimalLength = decimalLength(token, decimalPlaceSeparator);
							if (decimalLength < maxDecimalLength) {
								line.setCurrentToken(String.format("%-" + (token.length() + (maxDecimalLength - decimalLength)) + "s", token));
							}
						}
					}
				}
			}
		}


		for (ColumnAlignerLine line : lines) {
			maxCurrentTokenLength = Math.max(maxCurrentTokenLength, line.currentTokenLength());
		}

	}

	private static int decimalLength(String token, String decimalPlaceSeparator) {
		int decimalLength = 0;
		int i = StringUtils.lastIndexOf(token, decimalPlaceSeparator);
		if (i != -1) {
			decimalLength = token.length() - i;
		}
		return decimalLength;
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
