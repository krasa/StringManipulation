package osmedile.intellij.stringmanip.align;

import java.util.ArrayList;
import java.util.List;

import static osmedile.intellij.stringmanip.utils.StringUtils.isEmpty;

public class ColumnAligner {
	private final ColumnAlignerModel model;

	public ColumnAligner() {
		model = new ColumnAlignerModel();
	}

	public ColumnAligner(ColumnAlignerModel model) {
		this.model = model;
	}

	public ColumnAligner(String s) {
		model = new ColumnAlignerModel(s);
	}

	public List<String> align(List<String> lines) {
		for (String separator : model.getSeparators()) {
			if (isEmpty(separator)) {
				continue;
			}
			List<ColumnAlignerLine> columnAlignerLines = new ArrayList<ColumnAlignerLine>();
			for (String line : lines) {
				columnAlignerLines.add(new ColumnAlignerLine(model, separator, line, false));
			}
			lines = process(columnAlignerLines);
		}
		return lines;
	}

	public String align(String text) {
		String reformat = text;
		for (String separator : model.getSeparators()) {
			if (isEmpty(separator)) {
				continue;
			}
			reformat = reformat(separator, reformat);
		}
		return reformat;
	}

	private String reformat(String separator, String text) {
		List<ColumnAlignerLine> lines = toLines(separator, text);
		List<String> process = process(lines);
		StringBuilder sb = new StringBuilder();
		for (String line : process) {
			sb.append(line);
		}
		return sb.toString();
	}

	private List<ColumnAlignerLine> toLines(String separator, String text) {
		List<ColumnAlignerLine> lines = new ArrayList<ColumnAlignerLine>();
		String[] split = text.split("\n");
		boolean lastTokenEndsWithNewLine = text.endsWith("\n");
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			boolean last = i == split.length - 1;
			lines.add(new ColumnAlignerLine(model, separator, s, last ? lastTokenEndsWithNewLine : true));
		}
		return lines;
	}

	public List<String> process(List<ColumnAlignerLine> lines) {
		int initialSeparatorPosition = initialSeparatorPosition(lines);
		for (ColumnAlignerLine line : lines) {
			line.appendInitialSpace(initialSeparatorPosition);
		}

		boolean process = true;
		while (process) {
			process = false;

			for (ColumnAlignerLine line : lines) {
				line.appendText();
			}
			for (ColumnAlignerLine line : lines) {
                line.appendSpace(getMaxLength(lines));
			}
			for (ColumnAlignerLine line : lines) {
				line.appendSpaceBeforeSeparator();
			}                    
			for (ColumnAlignerLine line : lines) {
                line.appendSpace(getMaxLength(lines));
			}
			for (ColumnAlignerLine line : lines) {
				line.appendSeparator();
			}
			for (ColumnAlignerLine line : lines) {
				line.appendSpaceAfterSeparator();
			}
			for (ColumnAlignerLine line : lines) {
                line.appendSpace(getMaxLength(lines));
			}
			for (ColumnAlignerLine line : lines) {
				line.next();
			}

			for (ColumnAlignerLine line : lines) {
				process = process || line.hasToken();
			}
		}

		List<String> strings = new ArrayList<String>();
		for (ColumnAlignerLine line : lines) {
			strings.add(line.getString());
		}
		return strings;
	}

    protected int getMaxLength(List<ColumnAlignerLine> lines) {
        int maxLength = 0;
		for (ColumnAlignerLine line : lines) {
			maxLength = Math.max(maxLength, line.resultLength());
		}
		return maxLength;
	}


	private int initialSeparatorPosition(List<ColumnAlignerLine> lines) {
		int i = 0;
		for (ColumnAlignerLine line : lines) {
			i = Math.max(i, line.currentTokenLength());
		}
		return i;
	}

}
