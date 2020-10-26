package osmedile.intellij.stringmanip.escaping;

public class EscapeCSharpInterpolatedAction extends EscapeCSharp {
    @Override
    protected LiteralType getEscapeType() {
        return LiteralType.Interpolated;
    }
}
