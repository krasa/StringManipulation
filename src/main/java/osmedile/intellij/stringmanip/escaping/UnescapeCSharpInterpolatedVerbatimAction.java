package osmedile.intellij.stringmanip.escaping;

public class UnescapeCSharpInterpolatedVerbatimAction extends UnescapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.InterpolatedVerbatim;
    }
}
