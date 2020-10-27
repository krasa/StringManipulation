package osmedile.intellij.stringmanip.escaping;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public abstract class UnescapeCSharp extends AbstractStringManipAction<Object> {
    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        return unescape(s, getEscapeType());
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

    // Very simple input string stream with put back functionality.
    private static class Stream {
        public Stream(String s) {
            _s = s;
            _position = 0;
        }

        public boolean hasNext() {
            return _position < _s.length();
        }

        public char next() {
            assert hasNext();
            return _s.charAt(_position++);
        }

        public void putBack() {
            assert _position > 0;
            _position--;
        }

        private final String _s;
        private int _position;
    }

    private static String unescape(String s, LiteralType type) {
        StringBuilder sb = new StringBuilder(s.length());
        Stream input = new Stream(s);

        while (input.hasNext()) {
            char c = input.next();
            boolean unescaped = false;

            switch (type) {
            case Regular:
                unescaped = unescapeRegular(c, input, sb);
                break;
            case Verbatim:
                unescaped = unescapeVerbatim(c, input, sb);
                break;
            case Interpolated:
                unescaped = unescapeInterpolated(c, input, sb) || unescapeRegular(c, input, sb);
                break;
            case InterpolatedVerbatim:
                unescaped = unescapeInterpolated(c, input, sb) || unescapeVerbatim(c, input, sb);
                break;
            }

            if (!unescaped)
                sb.append(c);
        }

        return sb.toString();
    }

    private static boolean unescapeRegular(char c, Stream input, StringBuilder sb) {
        if (c == '\\' && input.hasNext()) {
            char cc = input.next();
            switch (cc) {
            case '0':
                sb.append('\0');
                return true;
            case 'a':
                sb.append('\u0007');
                return true;
            case 'b':
                sb.append('\u0008');
                return true;
            case 'f':
                sb.append('\u000C');
                return true;
            case 'n':
                sb.append('\n');
                return true;
            case 'r':
                sb.append('\r');
                return true;
            case 't':
                sb.append('\t');
                return true;
            case 'v':
                sb.append('\u000B');
                return true;
            case '\'':
                sb.append('\'');
                return true;
            case '"':
                sb.append('\"');
                return true;
            case 'u':
                if (input.hasNext()) {
                    char u1 = input.next();
                    if (isHexDigit(u1) && input.hasNext()) {
                        char u2 = input.next();
                        if (isHexDigit(u2) && input.hasNext()) {
                            char u3 = input.next();
                            if (isHexDigit(u3) && input.hasNext()) {
                                char u4 = input.next();
                                if (isHexDigit(u4)) {
                                    char ccc = (char) (
                                            (hexToInt(u1) << 12) |
                                            (hexToInt(u2) << 8) |
                                            (hexToInt(u3) << 4) |
                                            hexToInt(u4));
                                    sb.append(ccc);
                                    return true;
                                }
                                input.putBack();
                            }
                            input.putBack();
                        }
                        input.putBack();
                    }
                    input.putBack();
                }
                return false;
            default:
                input.putBack();
                return false;
            }
        }

        return false;
    }

    private static boolean unescapeVerbatim(char c, Stream input, StringBuilder sb) {
        if (c == '"' && input.hasNext()) {
            char cc = input.next();
            if (cc == '"') {
                sb.append('"');
                return true;
            }

            input.putBack();
        }

        return false;
    }

    private static boolean unescapeInterpolated(char c, Stream input, StringBuilder sb) {
        if ((c == '{' || c == '}') && input.hasNext()) {
            char cc = input.next();
            if (cc == c) {
                sb.append(c);
                return true;
            }

            input.putBack();
        }

        return false;
    }

    private static boolean isHexDigit(char c) {
        c = Character.toLowerCase(c);
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
    }

    private static int hexToInt(char c) {
        assert isHexDigit(c);
        c = Character.toLowerCase(c);
        return c >= '0' && c <= '9' ? c - '0' : c - 'a' + 10;
    }
}
