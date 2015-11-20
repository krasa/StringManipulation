package osmedile.intellij.stringmanip.styles;

public class ToPascalCaseAction extends AbstractCaseConvertingAction {
    public ToPascalCaseAction() {
    }

    public ToPascalCaseAction(boolean setupHandler) {
        super(setupHandler);
    }

    public String transform(String s) {
        Style from = Style.from(s);
        if (from == Style.PASCAL_CASE) {
            return Style.CAMEL_CASE.transform(from, s);
        } else {
            return Style.PASCAL_CASE.transform(from, s);
        }
    }

}
