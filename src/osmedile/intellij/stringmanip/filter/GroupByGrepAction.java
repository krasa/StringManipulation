package osmedile.intellij.stringmanip.filter;

import com.intellij.openapi.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class GroupByGrepAction extends GrepAction {
	@Override
	protected String transform(Pair<String, Boolean> grepos, String selectedText, GrepFilter grepFilter) {
		String[] textParts = selectedText.split("\n");
		Collection<String> match = new ArrayList<String>();
		Collection<String> notMatch = new ArrayList<String>();

		for (String textPart : textParts) {
			if (grepFilter.execute(textPart, grepos)) {
				match.add(textPart);
			} else {
				notMatch.add(textPart);
			}
		}

		String matchS = StringUtils.join(match.toArray(new String[match.size()]), '\n');

		StringBuilder sb = new StringBuilder(matchS);
		if (notMatch.size() > 0) {
			sb.append("\n");
			sb.append("\n");
			sb.append("\n");
			String notMatchS = StringUtils.join(notMatch.toArray(new String[match.size()]), '\n');
			sb.append(notMatchS);
		}
		return sb.toString();
	}
}