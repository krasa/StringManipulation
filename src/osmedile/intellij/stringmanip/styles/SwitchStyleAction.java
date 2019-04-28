package osmedile.intellij.stringmanip.styles;

@Deprecated
public class SwitchStyleAction extends AbstractCaseConvertingAction {

	public static Style[][] transformation = new Style[][]{
		{Style.KEBAB_LOWERCASE, Style.KEBAB_UPPERCASE},
		{Style.KEBAB_UPPERCASE, Style.SNAKE_CASE},
		{Style.SNAKE_CASE, Style.SCREAMING_SNAKE_CASE},
		{Style.SCREAMING_SNAKE_CASE, Style.DOT},
		{Style.DOT, Style.WORD_LOWERCASE},
		{Style.WORD_LOWERCASE, Style.SENTENCE_CASE},
		{Style.SENTENCE_CASE, Style.WORD_CAPITALIZED},
		{Style.WORD_CAPITALIZED, Style.PASCAL_CASE},
		{Style.PASCAL_CASE, Style.CAMEL_CASE},
		{Style.CAMEL_CASE, Style.KEBAB_LOWERCASE},
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
