package osmedile.intellij.stringmanip.styles.action;

import com.intellij.util.xmlb.annotations.Transient;
import osmedile.intellij.stringmanip.styles.Style;

import static osmedile.intellij.stringmanip.styles.Style.valueOf;

public class StyleStep {

	private String style;
	private boolean enabled = true;

	public StyleStep() {
	}

	public StyleStep(boolean enabled, Style style) {
		this.style = style.name();
		this.enabled = enabled;
	}

	@Transient
	public Style getStyleAsEnum() {
		try {
			return valueOf(style);
		} catch (Exception e) {
			return null;
		}
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StyleStep step = (StyleStep) o;

		if (enabled != step.enabled) return false;
		return style != null ? style.equals(step.style) : step.style == null;

	}

	@Override
	public int hashCode() {
		int result = style != null ? style.hashCode() : 0;
		result = 31 * result + (enabled ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Step{" +
			"style=" + style +
			", enabled=" + enabled +
			'}';
	}
}
