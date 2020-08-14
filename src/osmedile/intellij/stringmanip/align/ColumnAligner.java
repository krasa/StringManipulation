package osmedile.intellij.stringmanip.align;

import com.intellij.openapi.diagnostic.Logger;
import osmedile.intellij.stringmanip.sort.support.SortException;
import osmedile.intellij.stringmanip.sort.support.SortLine;
import osmedile.intellij.stringmanip.sort.support.SortSettings;
import osmedile.intellij.stringmanip.sort.support.Sortable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class ColumnAligner {
	private static final Logger LOG = Logger.getInstance(ColumnAligner.class);

	private final ColumnAlignerModel model;
	private List<String> debug = new ArrayList<>();

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
		if (model.isSequentialProcessing()) {
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
		} else {
			String[] separators = model.getSeparators().toArray(new String[0]);
			List<ColumnAlignerLine> columnAlignerLines = new ArrayList<ColumnAlignerLine>();
			for (String line : lines) {
				columnAlignerLines.add(new ColumnAlignerLine(model, line, false, separators));
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
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			lines.add(new ColumnAlignerLine(model, s, true, separator));
		}
		return lines;
	}

	public List<String> process(List<ColumnAlignerLine> lines) {
		initDebug(lines);

		try {
			lines = sort(lines);
		} catch (SortException e) {
			throw e;
		} catch (Exception e) {
			LOG.error(e);
		}

		if (model.isSortOnly()) {
			List<String> strings = new ArrayList<String>();
			for (ColumnAlignerLine line : lines) {
				strings.add(line.getOriginalString());
			}
			return strings;
		}
		if (model.isKeepLeadingIndent()) {
			String leadingIndent = minLeadingIndent(lines);
			for (ColumnAlignerLine line : lines) {
				line.append(leadingIndent);
			}
		}

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

			if (model.getAlignBy() == ColumnAlignerModel.Align.SEPARATORS) {
				int maxLength = getMaxLength(lines);
				for (ColumnAlignerLine line : lines) {
					line.appendSpace(maxLength);
				}
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

	private void initDebug(List<ColumnAlignerLine> lines) {
		for (ColumnAlignerLine line : lines) {
			if (line.getOriginalString().length() > 0) {
				debug = line.debugTokens();
				break;
			}
		}
	}

	private List<ColumnAlignerLine> sort(List<ColumnAlignerLine> lines) {
		String sortOrder = model.getColumnSortOrder();
		if (!isBlank(sortOrder)) {
			SortSettings sortSettings = model.getSortSettings();
			String[] split = sortOrder.split(" ");
			checkParse(split);

			for (int i = split.length - 1; i >= 0; i--) {
				if (isBlank(split[i])) {
					continue;
				}
				int s = Integer.parseInt(split[i]);
				sort(lines, s, sortSettings);
			}
		}
		return lines;
	}

	private List<ColumnAlignerLine> sort(List<ColumnAlignerLine> lines, int index, SortSettings sortSettings) {
		if (index <= 0) {
			throw new SortException("Invalid sort column index: " + index);
		}
		if (lines.size() > 1) {
			List<ColumnAlignerLine> linesToSort;
			ColumnAlignerLine firstLine = null;
			if (model.isSkipFirstRow()) {
				firstLine = lines.get(0);
				linesToSort = lines.subList(1, lines.size());
			} else {
				linesToSort = lines;
			}
			Comparator<Sortable> comparator = sortSettings.getSortType().getSortLineComparator(sortSettings.getBaseComparator(), sortSettings.getCollatorLanguageTag());

			linesToSort.sort(new Comparator<ColumnAlignerLine>() {
				@Override
				public int compare(ColumnAlignerLine o1, ColumnAlignerLine o2) {
					String s1 = o1.getToken(index - 1);
					String s2 = o2.getToken(index - 1);
//					System.out.println("s1=" + s1);
//					System.out.println("s2=" + s2);
					return comparator.compare(new SortLine(s1, sortSettings), new SortLine(s2, sortSettings));
				}
			});
			List<ColumnAlignerLine> result;
			if (firstLine != null) {
				result = new ArrayList<>();
				result.add(firstLine);
				result.addAll(linesToSort);
			} else {
				result = linesToSort;
			}
			return result;
		}
		return lines;
	}


	private void checkParse(String[] split) {
		try {
			for (int i = 0; i < split.length; i++) {
				if (isBlank(split[i])) {
					continue;
				}
				Integer s = Integer.valueOf(split[i]);
			}
		} catch (Exception e) {
			throw new SortException("Invalid sort settings: " + e.getMessage(), e);
		}
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

	private String minLeadingIndent(List<ColumnAlignerLine> lines) {
		String result = null;
		for (ColumnAlignerLine line : lines) {
			String leadingIndent = line.leadingIndent;
			if (result == null || (leadingIndent.length() < result.length() && !isBlank(line.getOriginalString()))) {
				result = leadingIndent;
			}
		}
		return result;
	}

	private int initialSeparatorPosition(List<ColumnAlignerLine> lines) {
		int i = 0;
		for (ColumnAlignerLine line : lines) {
			i = Math.max(i, line.currentTokenLength());
		}
		return i;
	}

	public List<String> getDebugValues() {
		return debug;
	}
}
