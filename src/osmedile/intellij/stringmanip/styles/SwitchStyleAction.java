package osmedile.intellij.stringmanip.styles;

public class SwitchStyleAction extends AbstractCaseConvertingAction {

	public static Style[][] transformation = new Style[][]{
		{Style.HYPHEN_LOWERCASE, Style.HYPHEN_UPPERCASE},
		{Style.HYPHEN_UPPERCASE, Style.UNDERSCORE_LOWERCASE},
		{Style.UNDERSCORE_LOWERCASE, Style.SCREAMING_SNAKE_CASE},
		{Style.SCREAMING_SNAKE_CASE, Style.DOT},
		{Style.DOT, Style.WORD_LOWERCASE},
		{Style.WORD_LOWERCASE, Style.WORD_CAPITALIZED},
		{Style.WORD_CAPITALIZED, Style.PASCAL_CASE},
		{Style.PASCAL_CASE, Style.CAMEL_CASE},
		{Style.CAMEL_CASE, Style.HYPHEN_LOWERCASE},
		{Style._UNKNOWN, Style.CAMEL_CASE},
		{Style._ALL_UPPER_CASE, Style.WORD_LOWERCASE},
		{Style._SINGLE_WORD_CAPITALIZED, Style.SCREAMING_SNAKE_CASE},
	};

	private boolean setupHandler;

	public SwitchStyleAction() {
		this(true);
	}

	public SwitchStyleAction(boolean setupHandler) {
		super(setupHandler);
		this.setupHandler = setupHandler;
	}

	@Override
	public String transformByLine(String s) {
		Style style = Style.from(s);
		for (Style[] styles : transformation) {
			if (styles[0] == style) {
				if (!setupHandler) {
					System.out.println("from " + styles[0] + " to " + styles[1]);
				}
				return styles[1].transform(styles[0], s);
			}
		}
		return s;
	}

}
