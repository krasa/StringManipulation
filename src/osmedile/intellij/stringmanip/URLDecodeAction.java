package osmedile.intellij.stringmanip;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class URLDecodeAction extends AbstractStringManipAction {

    public String transform(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}