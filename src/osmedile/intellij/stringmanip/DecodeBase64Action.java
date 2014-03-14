package osmedile.intellij.stringmanip;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class DecodeBase64Action extends AbstractStringManipAction {

    public String transform(String s) {
        return new String(Base64.decodeBase64(s.getBytes()));
    }
}