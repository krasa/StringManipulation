package osmedile.intellij.stringmanip.escaping.normalize;

public class NormalizationSettings {

	private NormalizationType normalizationType = NormalizationType.NFD;
	private boolean unescapeBefore = true;
	private boolean escapeAfter;

	public NormalizationType getType() {
		return normalizationType;
	}

	public void setType(NormalizationType normalizationType) {
		this.normalizationType = normalizationType;
	}

	public boolean isUnescapeBefore() {
		return unescapeBefore;
	}

	public void setUnescapeBefore(final boolean unescapeBefore) {
		this.unescapeBefore = unescapeBefore;
	}

	public boolean isEscapeAfter() {
		return escapeAfter;
	}

	public void setEscapeAfter(final boolean escapeAfter) {
		this.escapeAfter = escapeAfter;
	}
}
      