package osmedile.intellij.stringmanip.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncodingUtils {
    public static String encodeUrl(String selectedText) {
        try {
            return URLEncoder.encode(selectedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeUrlWithoutPlus(String selectedText) {
        return encodeUrl(selectedText).replace("+", "%20");
    }

    public static String decodeUrl(String selectedText) {
        try {
            return URLDecoder.decode(selectedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
