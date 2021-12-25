package osmedile.intellij.stringmanip.replace.gui;

import com.intellij.find.FindModel;
import org.apache.commons.lang3.StringUtils;

/**
 * UPDATE #equals
 */
public class ReplaceItemModel {
	private boolean caseSensitive;
	private boolean wholeWords;
	private String to = "";
	private String from = "";
	private String exclusiveGroup = "";
	private boolean regex;
	private boolean preserveCase;
	private boolean enabled;

	public ReplaceItemModel() {
	}

	public String getExclusiveGroup() {
		return exclusiveGroup;
	}

	public void setExclusiveGroup(String exclusiveGroup) {
		this.exclusiveGroup = exclusiveGroup;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isPreserveCase() {
		return preserveCase;
	}

	public void setPreserveCase(boolean preserveCase) {
		this.preserveCase = preserveCase;
	}

	public boolean isWholeWords() {
		return wholeWords;
	}

	public void setWholeWords(boolean wholeWords) {
		this.wholeWords = wholeWords;
	}


	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}


	public void setFrom(String from) {
		this.from = from;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	public String getFrom() {
		return from;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}


	public boolean isRegex() {
		return regex;
	}


	boolean isValid() {
		return StringUtils.isNotEmpty(from);
	}

	public FindModel toFindModel() {
		if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {
			return null;
		}
		FindModel model = new FindModel();
		model.setPreserveCase(isPreserveCase());
		model.setWholeWordsOnly(isWholeWords());
		model.setCaseSensitive(isCaseSensitive());
		model.setRegularExpressions(isRegex());
		model.setStringToFind(getFrom());
		model.setStringToReplace(getTo());
		return model;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(from) && StringUtils.isEmpty(to);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ReplaceItemModel that = (ReplaceItemModel) o;

		if (caseSensitive != that.caseSensitive) return false;
		if (wholeWords != that.wholeWords) return false;
		if (regex != that.regex) return false;
		if (preserveCase != that.preserveCase) return false;
		if (enabled != that.enabled) return false;
		if (to != null ? !to.equals(that.to) : that.to != null) return false;
		if (from != null ? !from.equals(that.from) : that.from != null) return false;
		return exclusiveGroup != null ? exclusiveGroup.equals(that.exclusiveGroup) : that.exclusiveGroup == null;
	}

	@Override
	public int hashCode() {
		int result = (caseSensitive ? 1 : 0);
		result = 31 * result + (wholeWords ? 1 : 0);
		result = 31 * result + (to != null ? to.hashCode() : 0);
		result = 31 * result + (from != null ? from.hashCode() : 0);
		result = 31 * result + (exclusiveGroup != null ? exclusiveGroup.hashCode() : 0);
		result = 31 * result + (regex ? 1 : 0);
		result = 31 * result + (preserveCase ? 1 : 0);
		result = 31 * result + (enabled ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ReplaceItemModel{" +
				"caseSensitive=" + caseSensitive +
				", wholeWords=" + wholeWords +
				", to='" + to + '\'' +
				", from='" + from + '\'' +
				", exclusiveGroup='" + exclusiveGroup + '\'' +
				", regex=" + regex +
				", preserveCase=" + preserveCase +
				", enabled=" + enabled +
				'}';
	}
}
