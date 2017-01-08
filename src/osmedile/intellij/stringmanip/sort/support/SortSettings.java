package osmedile.intellij.stringmanip.sort.support;

import com.google.common.base.Splitter;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class SortSettings {
	private static final Logger LOG = Logger.getInstance(SortSettings.class);
	public static final Key<SortSettings> KEY = Key.create("StringManipulation.SortSettings.UserData");

	private String trailingChars = ",;";
	private final Sort sortType;
	private boolean ignoreLeadingSpaces = true;
	private boolean preserveLeadingSpaces = true;
	private boolean preserveTrailingSpecialCharacters = true;

	public static SortSettings allFeaturesDisabled(Sort sort) {
		return new SortSettings(sort).ignoreLeadingSpaces(false).preserveLeadingSpaces(false).preserveTrailingSpecialCharacters(false);
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


	protected String asString() {
		return new StringBuilder()
				.append(sortType).append("|")
				.append(ignoreLeadingSpaces).append("|")
				.append(preserveLeadingSpaces).append("|")
				.append(preserveTrailingSpecialCharacters).append("|")
				.append(trailingChars)
				.toString();
	}

	protected static SortSettings fromString(String s) {
		List<String> strings = Splitter.on("|").limit(5).splitToList(s);
		SortSettings sortSettings = new SortSettings(Sort.valueOf(strings.get(0)));
		sortSettings.ignoreLeadingSpaces(Boolean.parseBoolean(strings.get(1)));
		sortSettings.preserveLeadingSpaces(Boolean.parseBoolean(strings.get(2)));
		sortSettings.preserveTrailingSpecialCharacters(Boolean.parseBoolean(strings.get(3)));
		sortSettings.trailingChars(strings.get(4));
		return sortSettings;
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
				.append(ignoreLeadingSpaces, that.ignoreLeadingSpaces)
				.append(preserveLeadingSpaces, that.preserveLeadingSpaces)
				.append(preserveTrailingSpecialCharacters, that.preserveTrailingSpecialCharacters)
				.append(trailingChars, that.trailingChars)
				.append(sortType, that.sortType)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(trailingChars)
				.append(sortType)
				.append(ignoreLeadingSpaces)
				.append(preserveLeadingSpaces)
				.append(preserveTrailingSpecialCharacters)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("trailingChars", trailingChars)
				.append("sortType", sortType)
				.append("ignoreLeadingSpaces", ignoreLeadingSpaces)
				.append("preserveLeadingSpaces", preserveLeadingSpaces)
				.append("preserveTrailingSpecialCharacters", preserveTrailingSpecialCharacters)
				.toString();
	}

}
