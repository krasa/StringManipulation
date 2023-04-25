package osmedile.intellij.stringmanip.escaping;

public class UnescapeCSharpInterpolatedAction extends UnescapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.Interpolated;
    }
}
