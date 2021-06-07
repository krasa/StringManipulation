package osmedile.intellij.stringmanip.swap;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.CharacterSwitchingSettings;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwapCharacterToFromIntAction extends AbstractStringManipAction<Object> {
    private static final Pattern isChar = Pattern.compile("(?s)'(.*)'");
    private static final Pattern isNumber = Pattern.compile("\\d+");

    private CharacterSwitchingSettings.Encoding forceEncoding = null;

    @Override
    protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String s, Object additionalParam) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        Matcher m;
        if ((m = isChar.matcher(s)).matches()) {
            return toDigits(m.group(1));
        } else if (isNumber.matcher(s).matches()) {
            return toChar(s);
        }
//        char i = '\f';
        return s;
    }

    private String toDigits(String s) {
        String result = StringEscapeUtils.unescapeJava(s);
        return "" + result.codePointAt(0);
    }

    private String toChar(String s) {
        if (s == null || s.length() > 3) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return s;
            }
        }
        int is = Integer.parseInt(s);
        if (is < 0x00 || is > 0xFF) {
            return s;
        }
        String result = is == 39 ? "\\'" : isUnicode() ? StringEscapeUtils.escapeJava("" + (char) is) : escapeJava("" + (char) is);
        return "'" + result + "'";
    }

    private boolean isUnicode() {
        return this.forceEncoding == null ? PluginPersistentStateComponent.getInstance().getCharacterSwitchingSettings().isUnicode() : forceEncoding == CharacterSwitchingSettings.Encoding.UNICODE;
    }

    public void setForceEncoding(CharacterSwitchingSettings.Encoding forceEncoding) {
        this.forceEncoding = forceEncoding;
    }

    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        return null;
    }

    private static String escapeJava(String str) {
        return escapeJavaStyleString(str);
    }

    private static String escapeJavaStyleString(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter writer = new StringWriter(str.length() * 2);
                escapeJavaStyleString(writer, str);
                return writer.toString();
            } catch (IOException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    private static void escapeJavaStyleString(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);
                if (ch > 127) {
                    out.write("\\" + fmt(ch));
                } else if (ch < 32) {
                    switch (ch) {
                        case '\b':
                            out.write(92);
                            out.write(98);
                            break;
                        case '\t':
                            out.write(92);
                            out.write(116);
                            break;
                        case '\n':
                            out.write(92);
                            out.write(110);
                            break;
                        case '\u000b':
                        default:
                            out.write("\\" + fmt(ch));
                            break;
                        case '\f':
                            out.write(92);
                            out.write(102);
                            break;
                        case '\r':
                            out.write(92);
                            out.write(114);
                    }
                } else {
                    switch (ch) {
                        case '"':
                            out.write(92);
                            out.write(34);
                            break;
                        case '\'':
                            out.write(92);
                            out.write(39);
                            break;
                        case '/':
                            out.write(92);
                            out.write(47);
                            break;
                        case '\\':
                            out.write(92);
                            out.write(92);
                            break;
                        default:
                            out.write(ch);
                    }
                }
            }
        }
    }
    private static String fmt(char ch) {
        return Integer.toString(ch, 8);
    }
}
