package osmedile.intellij.stringmanip.sort.tokens;

import org.apache.commons.lang3.StringUtils;
import osmedile.intellij.stringmanip.sort.support.SortSettings;

import java.text.SimpleDateFormat;
import java.util.*;

public class SortTokensModel {
	private Date added;
	private List<String> separators = new ArrayList<String>();
	private SortSettings sortSettings = new SortSettings();
	private boolean processEachLineSeparately =true;
	private boolean sortAllLinesTogether;
	private boolean ignoreEmptyTokens = true;

	public SortTokensModel() {
	}

	public SortTokensModel(String... separator) {
		this.separators = Arrays.asList(separator);
	}

	public List<String> getSeparators() {
		return separators;
	}

	public boolean isIgnoreEmptyTokens() {
		return ignoreEmptyTokens;
	}

	public void setIgnoreEmptyTokens(boolean ignoreEmptyTokens) {
		this.ignoreEmptyTokens = ignoreEmptyTokens;
	}

	public void setSeparators(List<String> separators) {
		this.separators = separators;
	}
	public Date getAdded() {
		return added;
	}

	public void setAdded(Date added) {
		this.added = added;
	}
	public SortSettings getSortSettings() {
		return sortSettings;
	}

	public void setSortSettings(SortSettings sortSettings) {
		this.sortSettings = sortSettings;
	}

	public boolean isProcessEachLineSeparately() {
		return processEachLineSeparately;
	}

	public void setProcessEachLineSeparately(boolean processEachLineSeparately) {
		this.processEachLineSeparately = processEachLineSeparately;
	}

	public boolean isSortAllLinesTogether() {
		return sortAllLinesTogether;
	}

	public void setSortAllLinesTogether(final boolean sortAllLinesTogether) {
		this.sortAllLinesTogether = sortAllLinesTogether;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SortTokensModel that = (SortTokensModel) o;
		return processEachLineSeparately == that.processEachLineSeparately &&
				sortAllLinesTogether == that.sortAllLinesTogether &&
				ignoreEmptyTokens == that.ignoreEmptyTokens &&
				Objects.equals(added, that.added) &&
				Objects.equals(separators, that.separators) &&
				Objects.equals(sortSettings, that.sortSettings);
	}

	@Override
	public int hashCode() {
		return Objects.hash(added, separators, sortSettings, processEachLineSeparately, sortAllLinesTogether, ignoreEmptyTokens);
	}

	@Override
	public String toString() {
		String format = "";
		if (added != null) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(added);
		}


		StringBuilder s = new StringBuilder();
		for (String separator : separators) {
			if (StringUtils.isNotEmpty(separator)) {
				s.append(separator).append(" ");
			}
		}
		return format + " - " + s.toString();
	}
}