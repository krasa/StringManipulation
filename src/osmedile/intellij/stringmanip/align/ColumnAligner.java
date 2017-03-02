package osmedile.intellij.stringmanip.align;

import java.util.ArrayList;
import java.util.List;

public class ColumnAligner {
	public String reformat(String separator, String text) {
		List<ColumnAlignerLine> lines = toLines(separator, text);
		process(lines);
		return toString(lines);
	}

	private String toString(List<ColumnAlignerLine> lines) {
		StringBuilder sb = new StringBuilder();
		for (ColumnAlignerLine s : lines) {
			sb.append(s.sb.toString());
			if (s.endsWithNextLine) {
				sb.append("\n");
			}
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
			lines.add(new ColumnAlignerLine(separator, s, last ? lastTokenEndsWithNewLine : true));
		}
		return lines;
	}

	public ArrayList<String> process(List<ColumnAlignerLine> lines) {
		ArrayList<String> strings = new ArrayList<String>();
		int initialSeparatorPosition = initialSeparatorPosition(lines);
		for (ColumnAlignerLine line : lines) {
			line.appendInitialSpace(initialSeparatorPosition);
		}

		boolean process = true;
		while (process) {
			process = false;

			int maxLength = 0;
			for (ColumnAlignerLine line : lines) {
				line.appendText();
				maxLength = Math.max(maxLength, line.resultLength());
			}
			for (ColumnAlignerLine line : lines) {
				line.appendSpace(maxLength);
			}
			for (ColumnAlignerLine line : lines) {
				line.appendSeparator();
			}

			for (ColumnAlignerLine line : lines) {
				line.next();
			}

			for (ColumnAlignerLine line : lines) {
				process = process || line.hasToken();
			}
		}

		for (ColumnAlignerLine line : lines) {
			strings.add(line.sb.toString().trim());
		}
		return strings;
	}

	private int initialSeparatorPosition(List<ColumnAlignerLine> lines) {
		int i = 0;
		for (ColumnAlignerLine line : lines) {
			i = Math.max(i, line.currentTokenLength());
		}
		return i;
	}

}
