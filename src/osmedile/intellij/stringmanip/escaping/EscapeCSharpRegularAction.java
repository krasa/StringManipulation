package osmedile.intellij.stringmanip.escaping;

public class EscapeCSharpRegularAction extends EscapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.Regular;
    }
}
