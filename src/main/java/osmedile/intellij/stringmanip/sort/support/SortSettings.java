package osmedile.intellij.stringmanip.sort.support;

import com.intellij.openapi.diagnostic.Logger;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class SortSettings {

	private static final Logger LOG = Logger.getInstance(SortSettings.class);

	public static final String LEVEL_REGEX = "^[\\s]+";
	public static final String GROUP_SEPARATOR_REGEX = "(^[\\s]*$|^---.*$)";
	public static final String GROUP_CLOSING_LINE_REGEX = "^[\\s]*[)\\]}],?[\\s]*$";

	private String trailingChars = ",;";
	private String levelRegex = LEVEL_REGEX;
	private String groupSeparatorRegex = GROUP_SEPARATOR_REGEX;
	private String collatorLanguageTag = Locale.getDefault().toString();
	private BaseComparator baseComparator = BaseComparator.NATURAL;
	private BlankLines blankLines = BlankLines.REMOVE;
	private Sort sortType = Sort.CASE_INSENSITIVE_A_Z;
	private boolean ignoreLeadingSpaces = true;
	private String ignoreLeadingCharacters;
	private boolean ignoreLeadingCharactersEnabled;
	private boolean preserveLeadingSpaces = true;
	private boolean preserveTrailingSpecialCharacters = false;
	private boolean hierarchicalSort = false;
	private boolean sortByGroups = false;
	private boolean jsonSort;

	private String groupClosingLineRegex = GROUP_CLOSING_LINE_REGEX;
	private boolean groupClosingLineRegexEnabled = true;

	public static SortSettings allFeaturesDisabled(Sort sort) {
		return new SortSettings(sort).ignoreLeadingSpaces(false).preserveLeadingSpaces(false).preserveTrailingSpecialCharacters(false);
	}

	public SortSettings() {
	}

	public SortSettings(Sort sort) {
		this.sortType = sort;
	}

	public boolean isGroupClosingLineRegexEnabled() {
		return groupClosingLineRegexEnabled;
	}

	public void setGroupClosingLineRegexEnabled(boolean groupClosingLineRegexEnabled) {
		this.groupClosingLineRegexEnabled = groupClosingLineRegexEnabled;
	}

	public String getIgnoreLeadingCharacters() {
		return ignoreLeadingCharacters;
	}

	public void setIgnoreLeadingCharacters(String ignoreLeadingCharacters) {
		this.ignoreLeadingCharacters = ignoreLeadingCharacters;
	}

	public boolean isIgnoreLeadingCharactersEnabled() {
		return ignoreLeadingCharactersEnabled;
	}

	public void setIgnoreLeadingCharactersEnabled(boolean ignoreLeadingCharactersEnabled) {
		this.ignoreLeadingCharactersEnabled = ignoreLeadingCharactersEnabled;
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

	public String getLevelRegex() {
		return levelRegex;
	}

	public void setLevelRegex(String levelRegex) {
		this.levelRegex = levelRegex;
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

	public SortSettings levelRegexp(String levelRegexp) {
		this.levelRegex = levelRegexp;
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

	public boolean isJsonSort() {
		return jsonSort;
	}

	public void setJsonSort(boolean jsonSort) {
		this.jsonSort = jsonSort;
	}

	public Comparator<Sortable> getSortLineComparator() {
		return getSortType().getSortLineComparator(getBaseComparator(), getCollatorLanguageTag());
	}

	public Comparator<String> getStringComparator() {
		return getSortType().getStringComparator(getBaseComparator(), getCollatorLanguageTag());
	}

	public static enum BlankLines {
		PRESERVE, REMOVE
	}

	public static enum BaseComparator {
		NORMAL,
		NATURAL,
		LOCALE_COLLATOR;
	}

	public String getGroupSeparatorRegex() {
		return groupSeparatorRegex;
	}

	public String getGroupClosingLineRegex() {
		return groupClosingLineRegex;
	}

	public void setGroupClosingLineRegex(String groupClosingLineRegex) {
		this.groupClosingLineRegex = groupClosingLineRegex;
	}

	public void setGroupSeparatorRegex(String groupSeparatorRegex) {
		this.groupSeparatorRegex = groupSeparatorRegex;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SortSettings that = (SortSettings) o;

		if (ignoreLeadingSpaces != that.ignoreLeadingSpaces) return false;
		if (ignoreLeadingCharactersEnabled != that.ignoreLeadingCharactersEnabled) return false;
		if (preserveLeadingSpaces != that.preserveLeadingSpaces) return false;
		if (preserveTrailingSpecialCharacters != that.preserveTrailingSpecialCharacters) return false;
		if (hierarchicalSort != that.hierarchicalSort) return false;
		if (sortByGroups != that.sortByGroups) return false;
		if (jsonSort != that.jsonSort) return false;
		if (groupClosingLineRegexEnabled != that.groupClosingLineRegexEnabled) return false;
		if (!Objects.equals(trailingChars, that.trailingChars))
			return false;
		if (!Objects.equals(levelRegex, that.levelRegex)) return false;
		if (!Objects.equals(groupSeparatorRegex, that.groupSeparatorRegex))
			return false;
		if (!Objects.equals(collatorLanguageTag, that.collatorLanguageTag))
			return false;
		if (baseComparator != that.baseComparator) return false;
		if (blankLines != that.blankLines) return false;
		if (sortType != that.sortType) return false;
		if (!Objects.equals(ignoreLeadingCharacters, that.ignoreLeadingCharacters))
			return false;
		return Objects.equals(groupClosingLineRegex, that.groupClosingLineRegex);
	}

	@Override
	public int hashCode() {
		int result = trailingChars != null ? trailingChars.hashCode() : 0;
		result = 31 * result + (levelRegex != null ? levelRegex.hashCode() : 0);
		result = 31 * result + (groupSeparatorRegex != null ? groupSeparatorRegex.hashCode() : 0);
		result = 31 * result + (collatorLanguageTag != null ? collatorLanguageTag.hashCode() : 0);
		result = 31 * result + (baseComparator != null ? baseComparator.hashCode() : 0);
		result = 31 * result + (blankLines != null ? blankLines.hashCode() : 0);
		result = 31 * result + (sortType != null ? sortType.hashCode() : 0);
		result = 31 * result + (ignoreLeadingSpaces ? 1 : 0);
		result = 31 * result + (ignoreLeadingCharacters != null ? ignoreLeadingCharacters.hashCode() : 0);
		result = 31 * result + (ignoreLeadingCharactersEnabled ? 1 : 0);
		result = 31 * result + (preserveLeadingSpaces ? 1 : 0);
		result = 31 * result + (preserveTrailingSpecialCharacters ? 1 : 0);
		result = 31 * result + (hierarchicalSort ? 1 : 0);
		result = 31 * result + (sortByGroups ? 1 : 0);
		result = 31 * result + (jsonSort ? 1 : 0);
		result = 31 * result + (groupClosingLineRegex != null ? groupClosingLineRegex.hashCode() : 0);
		result = 31 * result + (groupClosingLineRegexEnabled ? 1 : 0);
		return result;
	}
}
