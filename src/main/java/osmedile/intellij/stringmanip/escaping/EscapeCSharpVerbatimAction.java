package osmedile.intellij.stringmanip.escaping;

public class EscapeCSharpVerbatimAction extends EscapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.Verbatim;
    }
}
