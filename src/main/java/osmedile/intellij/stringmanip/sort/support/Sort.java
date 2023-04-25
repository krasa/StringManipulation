package osmedile.intellij.stringmanip.sort.support;


import java.math.BigInteger;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.*;

public enum Sort {

	SHUFFLE(new ComparatorFactory() {
		@Override
		Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "SHUFFLE");

		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					throw new RuntimeException("SHUFFLE not supported");
				}
			};
		}
	}),
	REVERSE(new ComparatorFactory() {
		@Override
		Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "REVERSE");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					throw new RuntimeException("REVERSE not supported");
				}
			};
		}
	}),
	CASE_SENSITIVE_A_Z(new ComparatorFactory() {
		public Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "CASE_SENSITIVE_A_Z");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public String toString() {
					return "CASE_SENSITIVE_A_Z";
				}

				@Override
				public int compare(String o1, String o2) {
					if (comparator == null) {
						return o1.compareTo(o2);
					}
					return comparator.compare(o1, o2);
				}
			};
		}
	}),
	CASE_SENSITIVE_Z_A(new ComparatorFactory() {
		public Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "CASE_SENSITIVE_Z_A");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public String toString() {
					return "CASE_SENSITIVE_Z_A";
				}

				@Override
				public int compare(String o1, String o2) {
					if (comparator == null) {
						return o2.compareTo(o1);
					}

					return comparator.compare(o2, o1);
				}
			};
		}
	}),
	CASE_INSENSITIVE_A_Z(new ComparatorFactory() {
		public Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "CASE_INSENSITIVE_A_Z");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public String toString() {
					return "CASE_INSENSITIVE_A_Z";
				}

				@Override
				public int compare(String o1, String o2) {
					if (comparator == null) {
						return o1.compareToIgnoreCase(o2);
					}
					return comparator.compare(o1.toLowerCase(), o2.toLowerCase());
				}
			};
		}
	}),
	CASE_INSENSITIVE_Z_A(new ComparatorFactory() {
		public Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "CASE_INSENSITIVE_Z_A");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public String toString() {
					return "CASE_INSENSITIVE_Z_A";
				}

				@Override
				public int compare(String o1, String o2) {
					if (comparator == null) {
						return o2.compareToIgnoreCase(o1);
					}
					return comparator.compare(o2.toLowerCase(), o1.toLowerCase());
				}
			};
		}
	}),
	LINE_LENGTH_SHORT_LONG(new ComparatorFactory() {
		public Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "LINE_LENGTH_SHORT_LONG");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public String toString() {
					return "LINE_LENGTH_SHORT_LONG";
				}

				@Override
				public int compare(String o1, String o2) {
					return o1.length() - o2.length();
				}
			};
		}
	}),
	LINE_LENGTH_LONG_SHORT(new ComparatorFactory() {
		public Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "LINE_LENGTH_LONG_SHORT");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public String toString() {
					return "LINE_LENGTH_LONG_SHORT";
				}

				@Override
				public int compare(String o1, String o2) {
					return o2.length() - o1.length();

				}
			};
		}
	}),

	HEXA(new ComparatorFactory() {
		public Comparator<Sortable> sortable(Comparator<String> comparator) {
			return new SortableComparator(string(comparator), "HEXA");
		}

		@Override
		Comparator<String> string(Comparator<String> comparator) {
			return new Comparator<String>() {
				@Override
				public String toString() {
					return "HEXA";
				}

				@Override
				public int compare(String o1, String o2) {
					try {
						return toHex(o1).compareTo(toHex(o2));
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
			};
		}
	});

	private ComparatorFactory factory;

	Sort(ComparatorFactory factory) {
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
		Comparator<String> baseComparator = getBaseStringComparator(baseComparatorEnum, languageTag);
		return factory.sortable(baseComparator);
	}

	public Comparator<String> getStringComparator(SortSettings.BaseComparator baseComparatorEnum, String languageTag) {
		Comparator<String> baseComparator = getBaseStringComparator(baseComparatorEnum, languageTag);
		return factory.string(baseComparator);
	}

	private Comparator<String> getBaseStringComparator(SortSettings.BaseComparator baseComparator, String languageTag) {
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

	static abstract class ComparatorFactory {
		abstract Comparator<Sortable> sortable(Comparator<String> baseComparator);

		abstract Comparator<String> string(Comparator<String> baseComparator);
	}

	private static class SortableComparator implements Comparator<Sortable> {
		private final Comparator<String> stringComparator;
		private String name;

		public SortableComparator(Comparator<String> stringComparator, String name) {
			this.name = name;
			this.stringComparator = stringComparator;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public int compare(Sortable o1, Sortable o2) {
			return stringComparator.compare(o1.getTextForComparison(), o2.getTextForComparison());
		}
	}
}
