package osmedile.intellij.stringmanip;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Olivier Smedile
 * @version $Id: EncodeMd5Hex16Action.java 32 2008-03-24 10:15:55Z osmedile $
 */
public class EncodeMd5Hex16Action extends AbstractStringManipAction {

    public String transform(String s) {
        try {

            byte[] hash = MessageDigest.getInstance("md5").digest(s.getBytes("UTF-8"));


            StringBuffer hashString = new StringBuffer();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(aHash);

                if (hex.length() == 1) {
                    hashString.append("0");
                    hashString.append(hex.charAt(hex.length() - 1));
                } else {
                    hashString.append(hex.substring(hex.length() - 2));

                }
            }

            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}