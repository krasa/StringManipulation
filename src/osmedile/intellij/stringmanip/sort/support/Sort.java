package osmedile.intellij.stringmanip.sort.support;


import java.math.BigInteger;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.*;

public enum Sort {

	SHUFFLE(new Comparator<Sortable>() {

		@Override
		public int compare(Sortable o1, Sortable o2) {
			throw new RuntimeException();
		}
	}),
	REVERSE(new Comparator<Sortable>() {
		@Override
		public int compare(Sortable o1, Sortable o2) {
			throw new RuntimeException();
		}
	}),
	CASE_SENSITIVE_A_Z(new ComparatorAdapterFactory() {
		public Comparator<Sortable> adapter(Comparator<String> comparator) {
			return new Comparator<Sortable>() {
				@Override
				public int compare(Sortable o1, Sortable o2) {
					if (comparator == null) {
						return o1.getTextForComparison().compareTo(o2.getTextForComparison());
					}
					return comparator.compare(o1.getTextForComparison(), o2.getTextForComparison());
				}
			};
		}
	}),
	CASE_SENSITIVE_Z_A(new ComparatorAdapterFactory() {
		public Comparator<Sortable> adapter(Comparator<String> comparator) {
			return new Comparator<Sortable>() {
				@Override
				public int compare(Sortable o1, Sortable o2) {
					if (comparator == null) {
						return o2.getTextForComparison().compareTo(o1.getTextForComparison());
					}

					return comparator.compare(o2.getTextForComparison(), o1.getTextForComparison());
				}
			};
		}
	}),
	CASE_INSENSITIVE_A_Z(new ComparatorAdapterFactory() {
		public Comparator<Sortable> adapter(Comparator<String> comparator) {
			return new Comparator<Sortable>() {
				@Override
				public int compare(Sortable o1, Sortable o2) {
					if (comparator == null) {
						return o1.getTextForComparison().compareToIgnoreCase(o2.getTextForComparison());
					}
					return comparator.compare(o1.getTextForComparison().toLowerCase(), o2.getTextForComparison().toLowerCase());
				}
			};
		}
	}),
	CASE_INSENSITIVE_Z_A(new ComparatorAdapterFactory() {
		public Comparator<Sortable> adapter(Comparator<String> comparator) {
			return new Comparator<Sortable>() {
				@Override
				public int compare(Sortable o1, Sortable o2) {
					if (comparator == null) {
						return o2.getTextForComparison().compareToIgnoreCase(o1.getTextForComparison());
					}
					return comparator.compare(o2.getTextForComparison().toLowerCase(), o1.getTextForComparison().toLowerCase());
				}
			};
		}
	}),
	LINE_LENGTH_SHORT_LONG(new Comparator<Sortable>() {
		@Override
		public int compare(Sortable o1, Sortable o2) {
			return o1.getTextForComparison().length() - o2.getTextForComparison().length();
		}
	}),
	LINE_LENGTH_LONG_SHORT(new Comparator<Sortable>() {
		@Override
		public int compare(Sortable o1, Sortable o2) {
			return o2.getTextForComparison().length() - o1.getTextForComparison().length();

		}
	}),
	HEXA(new Comparator<Sortable>() {
		@Override
		public int compare(Sortable o1, Sortable o2) {
			try {
				return toHex(o1.getTextForComparison()).compareTo(toHex(o2.getTextForComparison()));
			} catch (Throwable e) {
				throw new SortException("Hexadecimal sort failed, select Hex text only! \n(" + e.toString() + ")", e);
			}
		}

		protected BigInteger toHex(String textForComparison) {
			if (textForComparison == null) {
				return BigInteger.ZERO;
			}
			textForComparison = textForComparison.trim().replaceAll("^0x", "");

			return new BigInteger(textForComparison, 16);
		}
	});

	private Comparator<Sortable> comparator;
	private ComparatorAdapterFactory factory;

	Sort(Comparator<Sortable> comparator) {
		this.comparator = comparator;
	}

	Sort(ComparatorAdapterFactory factory) {
		this.factory = factory;
	}

	public <T extends Sortable> List<T> sortLines(List<T> lines, SortSettings.BaseComparator baseComparator, String languageTag) {
		List<T> sortedLines = new ArrayList<T>(lines);
		switch (this) {
			case SHUFFLE:
				Collections.shuffle(sortedLines);
				break;
			case REVERSE:
				Collections.reverse(sortedLines);
				break;
			default:
				sortedLines.sort(getSortLineComparator(baseComparator, languageTag));
		}
		return sortedLines;
	}

	public Comparator<Sortable> getSortLineComparator(SortSettings.BaseComparator baseComparatorEnum, String languageTag) {
		Comparator<String> baseComparator = getStringComparator(baseComparatorEnum, languageTag);
		if (factory != null) {
			return factory.adapter(baseComparator);
		} else {
			return comparator;
		}
	}

	public static Comparator<String> getStringComparator(SortSettings.BaseComparator baseComparator, String languageTag) {
		Comparator comparator;
		switch (baseComparator) {
			case NORMAL:
				comparator = null;
				break;
			case NATURAL:
				comparator = new osmedile.intellij.stringmanip.sort.support.Paour.NaturalOrderComparator();
				break;
			case LOCALE_COLLATOR:
				try {
					Collator instance = Collator.getInstance(Locale.forLanguageTag(languageTag));
					String rules = ((RuleBasedCollator) instance).getRules();
					Character[] chars = new Character[]{
						(char) 0x0009,//  9	Horizontal tab	HT
						(char) 0x000B,//  11	Vertical tab	VT
						(char) 0x000C,//  12	Form feed	FF
						(char) 0x000D,//  13	Carriage return	CR
						(char) 0x002D,//  45	Hyphen-minus	0014
						(char) ' ',//  
					};
					StringBuilder sb = new StringBuilder();
					for (Character aChar : chars) {
						sb.append("<'").append(aChar).append("'");
					}

					String correctedRules = rules.replaceAll("<'\u005f'", sb.toString() + "<'\u005f'");
					RuleBasedCollator correctedCollator = new RuleBasedCollator(correctedRules);
					comparator = correctedCollator;
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
				break;
			default:
				throw new RuntimeException("invalid enum");
		}

		return comparator;
	}

	static abstract class ComparatorAdapterFactory {
		abstract Comparator<Sortable> adapter(Comparator<String> comparator);
	}

}
