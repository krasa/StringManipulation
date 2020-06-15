package osmedile.intellij.stringmanip.sort.support;

import com.intellij.openapi.diagnostic.Logger;

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
	private boolean hierarchicalSort = false;
	private boolean sortByGroups = false;

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

	public SortSettings collatorLanguageTag(String collatorLanguageTag) {
		this.collatorLanguageTag = collatorLanguageTag;
		return this;
	}

	public SortSettings baseComparator(BaseComparator baseComparator) {
		this.baseComparator = baseComparator;
		return this;
	}

	public SortSettings blankLines(BlankLines blankLines) {
		this.blankLines = blankLines;
		return this;
	}

	public boolean isHierarchicalSort() {
		return hierarchicalSort;
	}

	public boolean isSortByGroups() {
		return sortByGroups;
	}

	public void setSortByGroups(boolean sortByGroups) {
		this.sortByGroups = sortByGroups;
	}

	public void setHierarchicalSort(boolean hierarchicalSort) {
		this.hierarchicalSort = hierarchicalSort;
	}

	public SortSettings sortByGroups(boolean sortByGroups) {
		this.sortByGroups = sortByGroups;
		return this;
	}

	public SortSettings hierarchicalSort(boolean hiearchicalSort) {
		this.hierarchicalSort = hiearchicalSort;
		return this;
	}

	public static enum BlankLines {
		PRESERVE, REMOVE
	}

	public static enum BaseComparator {
		NORMAL,
		NATURAL,
		LOCALE_COLLATOR;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SortSettings that = (SortSettings) o;

		if (ignoreLeadingSpaces != that.ignoreLeadingSpaces) return false;
		if (preserveLeadingSpaces != that.preserveLeadingSpaces) return false;
		if (preserveTrailingSpecialCharacters != that.preserveTrailingSpecialCharacters) return false;
		if (hierarchicalSort != that.hierarchicalSort) return false;
		if (sortByGroups != that.sortByGroups) return false;
		if (trailingChars != null ? !trailingChars.equals(that.trailingChars) : that.trailingChars != null)
			return false;
		if (collatorLanguageTag != null ? !collatorLanguageTag.equals(that.collatorLanguageTag) : that.collatorLanguageTag != null)
			return false;
		if (baseComparator != that.baseComparator) return false;
		if (blankLines != that.blankLines) return false;
		return sortType == that.sortType;
	}

	@Override
	public int hashCode() {
		int result = trailingChars != null ? trailingChars.hashCode() : 0;
		result = 31 * result + (collatorLanguageTag != null ? collatorLanguageTag.hashCode() : 0);
		result = 31 * result + (baseComparator != null ? baseComparator.hashCode() : 0);
		result = 31 * result + (blankLines != null ? blankLines.hashCode() : 0);
		result = 31 * result + (sortType != null ? sortType.hashCode() : 0);
		result = 31 * result + (ignoreLeadingSpaces ? 1 : 0);
		result = 31 * result + (preserveLeadingSpaces ? 1 : 0);
		result = 31 * result + (preserveTrailingSpecialCharacters ? 1 : 0);
		result = 31 * result + (hierarchicalSort ? 1 : 0);
		result = 31 * result + (sortByGroups ? 1 : 0);
		return result;
	}
}
