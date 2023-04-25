package osmedile.intellij.stringmanip.utils;

import shaded.org.springframework.web.util.UriUtils;

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


    public static String decodeUrl(String selectedText) {
        try {
            return URLDecoder.decode(selectedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeUrlRFC3986(String selectedText) {
        try {
            return UriUtils.encode(selectedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String decodeUrlRFC3986(String selectedText) {
        try {
            return UriUtils.decode(selectedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
}
