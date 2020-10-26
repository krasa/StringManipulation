package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public abstract class EscapeCSharp extends AbstractStringManipAction<Object> {
    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        return escape(s, getEscapeType());
    }

    protected enum LiteralType {
        Regular,
        Verbatim,
        Interpolated,
        InterpolatedVerbatim,
    }

    protected abstract LiteralType getEscapeType();

    //
    // Implementation
    //

    private static String escape(String s, LiteralType type) {
        StringBuilder sb = new StringBuilder(s.length() * 2);
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            boolean escaped = false;

            switch (type) {
            case Regular:
                escaped = escapeRegular(c, sb);
                break;
            case Verbatim:
                escaped = escapeVerbatim(c, sb);
                break;
            case Interpolated:
                escaped = escapeInterpolated(c, sb) || escapeRegular(c, sb);
                break;
            case InterpolatedVerbatim:
                escaped = escapeInterpolated(c, sb) || escapeVerbatim(c, sb);
                break;
            }

            if (!escaped)
                sb.append(c);
        }

        return sb.toString();
    }

    // We don't use temporary strings (only chars) in the escape* methods to reduce GC pressure.

    // See: https://docs.microsoft.com/en-us/dotnet/csharp/language-reference/language-specification/lexical-structure#string-literals
    private static boolean escapeRegular(char c, StringBuilder sb) {
        switch (c) {
        case 0x0000:
            sb.append('\\');
            sb.append('0');
            break;
        case 0x0007:
            sb.append('\\');
            sb.append('a');
            break;
        case 0x0008:
            sb.append('\\');
            sb.append('b');
            break;
        case 0x000C:
            sb.append('\\');
            sb.append('f');
            break;
        case 0x000A:
            sb.append('\\');
            sb.append('n');
            break;
        case 0x000D:
            sb.append('\\');
            sb.append('r');
            break;
        case 0x0009:
            sb.append('\\');
            sb.append('t');
            break;
        case 0x000B:
            sb.append('\\');
            sb.append('v');
            break;
        case '"':
            sb.append('\\');
            sb.append('"');
            break;
        default:
            if (c < 128)
                return false;

            sb.append('\\');
            sb.append('u');
            sb.append(hexDigit(c >> 12));
            sb.append(hexDigit(c >> 8));
            sb.append(hexDigit(c >> 4));
            sb.append(hexDigit(c));

            break;
        }

        return true;
    }

    private static boolean escapeVerbatim(char c, StringBuilder sb) {
        switch (c) {
        case '"':
            sb.append(c);
            sb.append(c);
            break;
        default:
            return false;
        }

        return true;
    }

    private static boolean escapeInterpolated(char c, StringBuilder sb) {
        switch (c) {
        case '{':
        case '}':
            sb.append(c);
            sb.append(c);
            break;
        default:
            return false;
        }

        return true;
    }

    private static char hexDigit(int d) {
        d &= 0x0F;
        return (char) (d <= 9 ? '0' + d : 'A' + d - 10);
    }
}
