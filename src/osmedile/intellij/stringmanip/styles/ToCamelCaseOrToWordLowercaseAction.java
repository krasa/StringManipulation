package osmedile.intellij.stringmanip.styles;

public class ToCamelCaseOrToWordLowercaseAction extends AbstractCaseConvertingAction {
    public ToCamelCaseOrToWordLowercaseAction() {
    }

    public ToCamelCaseOrToWordLowercaseAction(boolean setupHandler) {
        super(setupHandler);
    }

    @Override
    public String transformByLine(String s) {
        Style from = Style.from(s);
        if (from == Style.CAMEL_CASE) {
            return Style.WORD_LOWERCASE.transform(from, s);
        } else {
            return Style.CAMEL_CASE.transform(from, s);
        }
    }
}
