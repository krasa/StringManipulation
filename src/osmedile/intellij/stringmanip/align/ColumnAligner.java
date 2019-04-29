package osmedile.intellij.stringmanip.align;

import java.util.ArrayList;
import java.util.List;

import static shaded.org.apache.commons.lang3.StringUtils.isEmpty;

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
				columnAlignerLines.add(new ColumnAlignerLine(model, line, false, separator));
			}
			lines = process(columnAlignerLines);
		}
		return lines;
	}

	public String align(String text) {
		String reformat = text;
		if (model.isSequentialProcessing()) {
			for (String separator : model.getSeparators()) {
				if (isEmpty(separator)) {
					continue;
				}
				reformat = reformat(reformat, separator);
			}
		} else {
			reformat = reformat(reformat, model.getSeparators().toArray(new String[0]));
		}
		return reformat;
	}

	private String reformat(String text, String... separator) {
		List<ColumnAlignerLine> lines = toLines(text, separator);
		List<String> process = process(lines);
		StringBuilder sb = new StringBuilder();
		for (String line : process) {
			sb.append(line);
		}
		return sb.toString();
	}

	private List<ColumnAlignerLine> toLines(String text, String... separator) {
		List<ColumnAlignerLine> lines = new ArrayList<ColumnAlignerLine>();
		String[] split = text.split("\n");
		boolean lastTokenEndsWithNewLine = text.endsWith("\n");
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			boolean last = i == split.length - 1;
			lines.add(new ColumnAlignerLine(model, s, last ? lastTokenEndsWithNewLine : true, separator));
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
				line.next();
			}
			if (model.getAlignBy() == ColumnAlignerModel.Align.SEPARATORS) {
				int maxLength = getMaxLength(lines);
				for (ColumnAlignerLine line : lines) {
					line.appendSpace(maxLength);
				}
			}

			for (ColumnAlignerLine line : lines) {
				line.appendSpaceBeforeSeparator();
			}
			for (ColumnAlignerLine line : lines) {
				line.appendSeparator();
			}
			for (ColumnAlignerLine line : lines) {
				line.next();
			}
			for (ColumnAlignerLine line : lines) {
				line.appendSpaceAfterSeparator();
			}

				int maxLength = getMaxLength(lines);
				for (ColumnAlignerLine line : lines) {
					line.appendSpace(maxLength);
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

	protected void debug(List<ColumnAlignerLine> lines) {
		System.out.println("DEBUG >>>>>>>>>");
		for (ColumnAlignerLine line : lines) {
			System.out.println("'" + line.toString() + "'");
		}
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
