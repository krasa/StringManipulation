package osmedile.intellij.stringmanip.escaping;

public class UnescapeCSharpRegularAction extends UnescapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.Regular;
    }
}
