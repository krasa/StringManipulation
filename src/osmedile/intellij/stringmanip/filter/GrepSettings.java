package osmedile.intellij.stringmanip.filter;

import java.util.Date;

public class GrepSettings {

	private boolean inverted;
	private boolean groupMatching;
	private boolean regex;
	private Date added;
	private String pattern;
	private boolean caseSensitive;
	private boolean fullWords;

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

	public void setAdded(Date added) {
		this.added = added;
	}

	public Date getAdded() {
		return added;
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
	public String toString() {
		return pattern + (regex ? " (regex)" : "");
	}
}
