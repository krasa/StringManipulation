package osmedile.intellij.stringmanip.escaping;

public class EscapeCSharpInterpolatedVerbatimAction extends EscapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.InterpolatedVerbatim;
    }
}
