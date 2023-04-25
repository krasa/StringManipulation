package osmedile.intellij.stringmanip.escaping;

public class UnescapeCSharpVerbatimAction extends UnescapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.Verbatim;
    }
}
