package osmedile.intellij.stringmanip.border;

public class BorderSettings {

	private String padding = "0";
	private String borderWidth = "1";
	private String customBorder = "#";
	private boolean borderSingle;
	private boolean borderDouble = true;
	private boolean borderCustom;

	public boolean isBorderSingle() {
		return borderSingle;
	}

	public void setBorderSingle(boolean borderSingle) {
		this.borderSingle = borderSingle;
	}

	public boolean isBorderDouble() {
		return borderDouble;
	}

	public void setBorderDouble(boolean borderDouble) {
		this.borderDouble = borderDouble;
	}

	public boolean isBorderCustom() {
		return borderCustom;
	}

	public void setBorderCustom(boolean borderCustom) {
		this.borderCustom = borderCustom;
	}


	public String getPadding() {
		return padding;
	}

	public int getPaddingAsInt() {
		try {
			return Integer.parseInt(padding);
		} catch (Throwable e) {
			return 0;
		}
	}

	public int getBorderWidthAsInt() {
		try {
			return Integer.parseInt(borderWidth);
		} catch (Throwable e) {
			return 0;
		}
	}

	public void setPadding(final String padding) {
		this.padding = padding;
	}

	public String getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(final String borderWidth) {
		this.borderWidth = borderWidth;
	}

	public String getCustomBorder() {
		return customBorder;
	}

	public void setCustomBorder(final String customBorder) {
		this.customBorder = customBorder;
	}

	public char getBorderChar(int i) {
		if (customBorder.isEmpty()) {
			return '#';
		}
		String customBorder = this.customBorder;
		if (customBorder.length() > i) {
			return customBorder.charAt(i);
		}
		return customBorder.charAt(customBorder.length() - 1);
	}

}
