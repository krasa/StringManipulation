package osmedile.intellij.stringmanip.sort.support;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum Sort {

	SHUFFLE(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			throw new RuntimeException();
		}
	}),
	REVERSE(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			throw new RuntimeException();
		}
	}),
	CASE_SENSITIVE_A_Z(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			return COMPARATOR.compare(o1.getTextForComparison(), o2.getTextForComparison());
		}
	}),
	CASE_SENSITIVE_Z_A(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			return COMPARATOR.compare(o2.getTextForComparison(), o1.getTextForComparison());
		}
	}),
	CASE_INSENSITIVE_A_Z(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			return COMPARATOR.compare(o1.getTextForComparison().toLowerCase(), o2.getTextForComparison().toLowerCase());
		}
	}),
	CASE_INSENSITIVE_Z_A(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			return COMPARATOR.compare(o2.getTextForComparison().toLowerCase(), o1.getTextForComparison().toLowerCase());
		}
	}),
	LINE_LENGTH_SHORT_LONG(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			return o1.getTextForComparison().length() - o2.getTextForComparison().length();
		}
	}),
	LINE_LENGTH_LONG_SHORT(new Comparator<SortLine>() {
		@Override
		public int compare(SortLine o1, SortLine o2) {
			return o2.getTextForComparison().length() - o1.getTextForComparison().length();

		}
	});

	private Comparator<SortLine> comparator;

	Sort(Comparator<SortLine> comparator) {
		this.comparator = comparator;
	}

	public <T extends SortLine> List<T> sortLines(List<T> lines) {
		switch (this) {
			case SHUFFLE:
				Collections.shuffle(lines);
				break;
			case REVERSE:
				Collections.reverse(lines);
				break;
			default:
				Collections.sort(lines, comparator);
		}
		return lines;
	}


	public Comparator<SortLine> getComparator() {
		return comparator;
	}

	final static Comparator<String> COMPARATOR = new NaturalOrderComparator();

	// final static Comparator alphanumComparator = Ordering.natural();

}
