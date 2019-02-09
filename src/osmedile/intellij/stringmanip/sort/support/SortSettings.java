package osmedile.intellij.stringmanip.sort.support;

import com.google.common.base.Splitter;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

public class SortSettings {
	private static final Logger LOG = Logger.getInstance(SortSettings.class);

	private String trailingChars = ",;";
	private ComparatorEnum comparatorEnum = ComparatorEnum.NATURAL;
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

	public SortSettings comparator(ComparatorEnum comparatorEnum) {
		this.comparatorEnum = comparatorEnum;
		return this;
	}

	public ComparatorEnum getComparatorEnum() {
		return comparatorEnum;
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

	protected String asString() {
		return new StringBuilder()
			.append(comparatorEnum).append("|")
			.append(sortType).append("|")
			.append(blankLines).append("|")
			.append(ignoreLeadingSpaces).append("|")
			.append(preserveLeadingSpaces).append("|")
			.append(preserveTrailingSpecialCharacters).append("|")
			.append(trailingChars)
			.toString();
	}

	protected static SortSettings fromString(String s) {
		List<String> strings = Splitter.on("|").limit(7).splitToList(s);
		SortSettings sortSettings = new SortSettings();
		int i = 0;
		if (strings.size() >= 6) {
			String sort = strings.get(i++);
			sortSettings.comparator(valueOfComparator(sort));
			if (!sortSettings.getComparatorEnum().name().equals(sort)) {
				//old format
				i = 0;
				strings = Splitter.on("|").limit(6).splitToList(s);
			}

			sortSettings.sortType(valueOfSort(strings.get(i++)));
			sortSettings.setBlankLines(strings.get(i++));
			sortSettings.ignoreLeadingSpaces(Boolean.parseBoolean(strings.get(i++)));
			sortSettings.preserveLeadingSpaces(Boolean.parseBoolean(strings.get(i++)));
			sortSettings.preserveTrailingSpecialCharacters(Boolean.parseBoolean(strings.get(i++)));
			sortSettings.trailingChars(strings.get(i++));
		}
		return sortSettings;
	}

	private static Sort valueOfSort(String s) {
		try {
			return Sort.valueOf(s);
		} catch (IllegalArgumentException e) {
			return Sort.CASE_INSENSITIVE_A_Z;
		}
	}

	private static ComparatorEnum valueOfComparator(String s) {
		try {
			return ComparatorEnum.valueOf(s);
		} catch (IllegalArgumentException e) {
			return ComparatorEnum.NATURAL;
		}
	}

	public void store(String key) {
		final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
		propertiesComponent.setValue(key, this.asString());
	}

	public static SortSettings readFromStore(String key) {
		final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
		final String history = propertiesComponent.getValue(key);
		if (history != null) {
			try {
				return fromString(history);
			} catch (Exception e) {
				LOG.error(e);
			}
		}
		return new SortSettings(Sort.CASE_INSENSITIVE_A_Z);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		SortSettings that = (SortSettings) o;

		return new EqualsBuilder()
			.append(comparatorEnum, that.comparatorEnum)
			.append(ignoreLeadingSpaces, that.ignoreLeadingSpaces)
			.append(preserveLeadingSpaces, that.preserveLeadingSpaces)
			.append(preserveTrailingSpecialCharacters, that.preserveTrailingSpecialCharacters)
			.append(trailingChars, that.trailingChars)
			.append(sortType, that.sortType)
			.append(blankLines, that.blankLines)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(comparatorEnum)
			.append(trailingChars)
			.append(sortType)
			.append(ignoreLeadingSpaces)
			.append(preserveLeadingSpaces)
			.append(preserveTrailingSpecialCharacters)
			.append(blankLines)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("comparator", comparatorEnum)
			.append("trailingChars", trailingChars)
			.append("sortType", sortType)
			.append("ignoreLeadingSpaces", ignoreLeadingSpaces)
			.append("preserveLeadingSpaces", preserveLeadingSpaces)
			.append("preserveTrailingSpecialCharacters", preserveTrailingSpecialCharacters)
			.append("emptyLines", blankLines)
			.toString();
	}

	public static enum BlankLines {
		PRESERVE, REMOVE
	}

	public static enum ComparatorEnum {
		NATURAL(NaturalOrderComparator.COMPARATOR),
		LOCALE_COLLATOR(Collator.getInstance());

		public Comparator comparator;

		ComparatorEnum(Comparator comparator) {
			this.comparator = comparator;
		}

	}
}
