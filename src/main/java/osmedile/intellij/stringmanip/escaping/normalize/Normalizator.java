package osmedile.intellij.stringmanip.escaping.normalize;

import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Normalizator {

	public static String normalizeText(String text, NormalizationSettings settings) {
		if (settings.isUnescapeBefore()) {
			text = StringUtil.escapedUnicodeToString(text);
		}

		String normalize = settings.getType().normalize(text);

		if (settings.isEscapeAfter()) {
			normalize = StringUtil.nonAsciiToUnicode(normalize);
		}

		return normalize;
	}

	public static List<String> normalizeLines(List<String> lines, NormalizationSettings settings) {
		NormalizationType normalizationType = settings.getType();
		ArrayList<String> strings = new ArrayList<>();
		for (String line : lines) {
			if (settings.isUnescapeBefore()) {
				line = StringUtil.escapedUnicodeToString(line);
			}

			String normalize = normalizationType.normalize(line);

			if (settings.isEscapeAfter()) {
				normalize = StringUtil.nonAsciiToUnicode(normalize);
			}
			strings.add(normalize);
		}
		return strings;
	}
}
