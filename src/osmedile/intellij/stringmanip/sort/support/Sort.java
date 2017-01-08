package osmedile.intellij.stringmanip.sort.support;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum Sort {

	CASE_SENSITIVE_A_Z(new Comparator<Line>() {
		@Override
		public int compare(Line o1, Line o2) {
			return COMPARATOR.compare(o1.getTextForComparison(), o2.getTextForComparison());
		}
	}),
	CASE_SENSITIVE_Z_A(new Comparator<Line>() {
		@Override
		public int compare(Line o1, Line o2) {
			return COMPARATOR.compare(o2.getTextForComparison(), o1.getTextForComparison());
		}
	}),
	CASE_INSENSITIVE_A_Z(new Comparator<Line>() {
		@Override
		public int compare(Line o1, Line o2) {
			return COMPARATOR.compare(o1.getTextForComparison().toLowerCase(), o2.getTextForComparison().toLowerCase());
		}
	}),
	CASE_INSENSITIVE_Z_A(new Comparator<Line>() {
		@Override
		public int compare(Line o1, Line o2) {
			return COMPARATOR.compare(o2.getTextForComparison().toLowerCase(), o1.getTextForComparison().toLowerCase());
		}
	}),
	LINE_LENGTH_SHORT_LONG(new Comparator<Line>() {
		@Override
		public int compare(Line o1, Line o2) {
			return o1.getTextForComparison().length() - o2.getTextForComparison().length();
		}
	}),
	LINE_LENGTH_LONG_SHORT(new Comparator<Line>() {
		@Override
		public int compare(Line o1, Line o2) {
			return o2.getTextForComparison().length() - o1.getTextForComparison().length();

		}
	});

	private Comparator<Line> comparator;

	Sort(Comparator<Line> comparator) {
		this.comparator = comparator;
	}

	public List<Line> sortLines(List<Line> lines) {
		Collections.sort(lines, comparator);
		return lines;
	}


	public Comparator<Line> getComparator() {
		return comparator;
	}

	public static List<String> sortLines(SortSettings settings, List<String> lines) {
		return new Lines(lines, settings).sortLines();
	}

	final static Comparator<String> COMPARATOR = new NaturalOrderComparator();

	// final static Comparator alphanumComparator = Ordering.natural();

}
