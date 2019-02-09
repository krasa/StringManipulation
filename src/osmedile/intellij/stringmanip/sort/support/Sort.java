package osmedile.intellij.stringmanip.sort.support;

import java.math.BigInteger;
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
	CASE_SENSITIVE_A_Z(new ComparatorFactory() {
		public Comparator<SortLine> prototype(Comparator comparator) {
			return new Comparator<SortLine>() {
				@Override
				public int compare(SortLine o1, SortLine o2) {
					return comparator.compare(o1.getTextForComparison(), o2.getTextForComparison());
				}
			};
		}
	}),
	CASE_SENSITIVE_Z_A(new ComparatorFactory() {
		public Comparator<SortLine> prototype(Comparator comparator) {
			return new Comparator<SortLine>() {
				@Override
				public int compare(SortLine o1, SortLine o2) {
					return comparator.compare(o2.getTextForComparison(), o1.getTextForComparison());
				}
			};
		}
	}),
	CASE_INSENSITIVE_A_Z(new ComparatorFactory() {
		public Comparator<SortLine> prototype(Comparator comparator) {
			return new Comparator<SortLine>() {
				@Override
				public int compare(SortLine o1, SortLine o2) {
					return comparator.compare(o1.getTextForComparison().toLowerCase(), o2.getTextForComparison().toLowerCase());
				}
			};
		}
	}),
	CASE_INSENSITIVE_Z_A(new ComparatorFactory() {
		public Comparator<SortLine> prototype(Comparator comparator) {
			return new Comparator<SortLine>() {
				@Override
				public int compare(SortLine o1, SortLine o2) {
					return comparator.compare(o2.getTextForComparison().toLowerCase(), o1.getTextForComparison().toLowerCase());
				}
			};
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
	}),
	HEXA(new Comparator<SortLine>() {   //TODO error handling?
		@Override
		public int compare(SortLine o1, SortLine o2) {
			return toHex(o1.getTextForComparison()).compareTo(toHex(o2.getTextForComparison()));
		}

		protected BigInteger toHex(String textForComparison) {
			if (textForComparison == null) {
				return BigInteger.ZERO;
			}
			textForComparison = textForComparison.trim().replaceAll("^0x", "");

			return new BigInteger(textForComparison, 16);
		}
	});

	private Comparator<SortLine> comparator;
	private ComparatorFactory factory;

	Sort(Comparator<SortLine> comparator) {
		this.comparator = comparator;
	}

	Sort(ComparatorFactory factory) {
		this.factory = factory;
	}

	public <T extends SortLine> List<T> sortLines(List<T> lines, SortSettings.ComparatorEnum comparatorEnum) {
		switch (this) {
			case SHUFFLE:
				Collections.shuffle(lines);
				break;
			case REVERSE:
				Collections.reverse(lines);
				break;
			default:
				Collections.sort(lines, getComparator(comparatorEnum));
		}
		return lines;
	}

	protected Comparator getComparator(SortSettings.ComparatorEnum comparatorEnum) {
		if (factory != null) {
			return factory.prototype(comparatorEnum.comparator);
		} else {
			return comparator;
		}
	}


	public Comparator<SortLine> getComparator() {
		return comparator;
	}

	static abstract class ComparatorFactory {
		abstract Comparator<SortLine> prototype(Comparator comparator);
	}
}
