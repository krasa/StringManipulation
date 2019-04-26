package osmedile.intellij.stringmanip.sort.support;

import com.intellij.openapi.diagnostic.Logger;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class SortSettings {
	private static final Logger LOG = Logger.getInstance(SortSettings.class);

	private String trailingChars = ",;";
	private String collatorLanguageTag = Locale.getDefault().toString();
	private BaseComparator baseComparator = BaseComparator.NATURAL;
	private BlankLines blankLines = BlankLines.REMOVE;
	private Sort sortType = Sort.CASE_INSENSITIVE_A_Z;
	private boolean ignoreLeadingSpaces = true;
	private boolean preserveLeadingSpaces = true;
	private boolean preserveTrailingSpecialCharacters = false;

	public static SortSettings allFeaturesDisabled(Sort sort) {
		return new SortSettings(sort).ignoreLeadingSpaces(false).preserveLeadingSpaces(false).preserveTrailingSpecialCharacters(false);
	}

	public SortSettings() {
	}

	public SortSettings(Sort sort) {
		this.sortType = sort;
	}

	public SortSettings ignoreLeadingSpaces(final boolean ignoreLeadingSpaces) {
		this.ignoreLeadingSpaces = ignoreLeadingSpaces;
		return this;
	}

	public SortSettings preserveLeadingSpaces(final boolean preserveLeadingSpaces) {
		this.preserveLeadingSpaces = preserveLeadingSpaces;
		return this;
	}

	public SortSettings preserveTrailingSpecialCharacters(final boolean preserveTrailingSpecialCharacters) {
		this.preserveTrailingSpecialCharacters = preserveTrailingSpecialCharacters;
		return this;
	}

	public SortSettings trailingChars(final String trailingChars) {
		this.trailingChars = trailingChars;
		return this;
	}

	public SortSettings sortType(final Sort sortType) {
		this.sortType = sortType;
		return this;
	}

	public SortSettings emptyLines(BlankLines blankLines) {
		this.blankLines = blankLines;
		return this;
	}

	public SortSettings comparator(BaseComparator baseComparator) {
		this.baseComparator = baseComparator;
		return this;
	}

	public BaseComparator getBaseComparator() {
		return baseComparator;
	}

	public String getTrailingChars() {
		return trailingChars;
	}

	public Sort getSortType() {
		return sortType;
	}

	public boolean isIgnoreLeadingSpaces() {
		return ignoreLeadingSpaces;
	}

	public boolean isPreserveLeadingSpaces() {
		return preserveLeadingSpaces;
	}

	public boolean isPreserveTrailingSpecialCharacters() {
		return preserveTrailingSpecialCharacters;
	}

	public BlankLines emptyLines() {
		return blankLines;
	}

	public void setBlankLines(String blankLines) {
		try {
			this.blankLines = BlankLines.valueOf(blankLines);
		} catch (IllegalArgumentException e) {
		}
	}

	public void setTrailingChars(String trailingChars) {
		this.trailingChars = trailingChars;
	}

	public String getCollatorLanguageTag() {
		return collatorLanguageTag;
	}

	public void setCollatorLanguageTag(String collatorLanguageTag) {
		this.collatorLanguageTag = collatorLanguageTag;
	}

	public void setBaseComparator(BaseComparator baseComparator) {
		this.baseComparator = baseComparator;
	}

	public BlankLines getBlankLines() {
		return blankLines;
	}

	public void setBlankLines(BlankLines blankLines) {
		this.blankLines = blankLines;
	}

	public void setSortType(Sort sortType) {
		this.sortType = sortType;
	}

	public void setIgnoreLeadingSpaces(boolean ignoreLeadingSpaces) {
		this.ignoreLeadingSpaces = ignoreLeadingSpaces;
	}

	public void setPreserveLeadingSpaces(boolean preserveLeadingSpaces) {
		this.preserveLeadingSpaces = preserveLeadingSpaces;
	}

	public void setPreserveTrailingSpecialCharacters(boolean preserveTrailingSpecialCharacters) {
		this.preserveTrailingSpecialCharacters = preserveTrailingSpecialCharacters;
	}

	public static enum BlankLines {
		PRESERVE, REMOVE
	}

	public static enum BaseComparator {
		NATURAL(NaturalOrderComparator.COMPARATOR),
		LOCALE_COLLATOR(Collator.getInstance());

		private Comparator comparator;

		BaseComparator(Comparator comparator) {
			this.comparator = comparator;
		}

		public static Comparator getComparator(BaseComparator baseComparator, String languageTag) {
			Comparator comparator;
			switch (baseComparator) {
				case NATURAL:
					comparator = baseComparator.comparator;
					break;
				case LOCALE_COLLATOR:
					comparator = Collator.getInstance(Locale.forLanguageTag(languageTag));
					break;
				default:
					throw new RuntimeException("invalid enum");
			}

			return comparator;
		}
	}
}
