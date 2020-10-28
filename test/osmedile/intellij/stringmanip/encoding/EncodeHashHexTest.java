package osmedile.intellij.stringmanip.encoding;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncodeHashHexTest {
    @Test
    public void md5() {
        verifyHash(new EncodeMd5HexAction(), "64e28d72338b19d28341e0eb0342123d");
    }

    @Test
    public void sha1() {
        verifyHash(new EncodeSha1HexAction(), "b038990f898bc0b4a72fa5def49ce96f861bfc50");
    }

    @Test
    public void sha256() {
        verifyHash(new EncodeSha256HexAction(), "0810b032040c0b17d2d3f8775494bc3c7a39e83a3e39757646e5e1d1cc97763d");
    }

    //
    // Helper
    //

    private void verifyHash(EncodeHashHex action, String expected) {
        String hash = action.transformSelection(null, null, null, "blah-blah", null);
        assertEquals(expected, hash);
    }
}

