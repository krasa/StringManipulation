package osmedile.intellij.stringmanip;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class URLEncodeAction extends AbstractStringManipAction {

    public String transform(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}