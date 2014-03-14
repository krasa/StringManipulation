package osmedile.intellij.stringmanip;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Olivier Smedile
 * @version $Id: EncodeBase64Action.java 29 2008-03-21 18:01:20Z osmedile $
 */
public class EncodeBase64Action extends AbstractStringManipAction {

    public String transform(String s) {
        return new String(Base64.encodeBase64(s.getBytes()));
    }
}