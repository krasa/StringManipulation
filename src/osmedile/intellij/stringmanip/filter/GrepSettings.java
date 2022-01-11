package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.editor.VisualPosition;
import com.intellij.util.xmlb.annotations.Transient;
import osmedile.intellij.stringmanip.StringManipulationBundle;

import java.util.Objects;

public class GrepSettings {

	private boolean inverted;
	private boolean groupMatching;
	private boolean regex;
	private String pattern;
	private boolean caseSensitive;
	private boolean fullWords;
	@Transient
	public transient boolean quick;
	@Transient
	public transient VisualPosition visualPosition;

	/**
	 * UPDATE EQUALS
	 */
	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public boolean isGroupMatching() {
		return groupMatching;
	}

	public void setGroupMatching(boolean groupMatching) {
		this.groupMatching = groupMatching;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(final boolean regex) {
		this.regex = regex;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(final boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isFullWords() {
		return fullWords;
	}

	public void setFullWords(final boolean fullWords) {
		this.fullWords = fullWords;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GrepSettings that = (GrepSettings) o;
		return inverted == that.inverted && groupMatching == that.groupMatching && regex == that.regex && caseSensitive == that.caseSensitive && fullWords == that.fullWords && Objects.equals(pattern, that.pattern);
	}

	@Override
	public int hashCode() {
		return Objects.hash(inverted, groupMatching, regex, pattern, caseSensitive, fullWords);
	}

	@Override
	public String toString() {
		return pattern + (this.regex ? " (" + StringManipulationBundle.message("regex") + ")" : "");
	}
}
