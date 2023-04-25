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

	@Test
	public void sha512() {
		verifyHash(new EncodeSha512HexAction(), "48630f422d9ec4e758b257a97b28d1ce1cd199f32ffc06f21a607950705e10b308cfdad52c0fd2f57ae04d720aacaba7f3c2a9fbc0232f6226abbcf5f9fbc7f7");
	}

	@Test
	public void sha3_256() {
		verifyHash(new EncodeSha3_256HexAction(), "0e707bef7e8c631a0958febaba121448397ec18c7e0f63e7753e5d31ddc23fa8");
	}

	@Test
	public void sha3_512() {
		verifyHash(new EncodeSha3_512HexAction(), "1241f762ce8df0a492d6c953d9a17de65953bdad44597cbf8b853b98dad181566bee4cda3a196df0ecb7fc302ce14fae5320b97af63f949030f81b4238ec6629");
	}

	//
	// Helper
	//

	private void verifyHash(EncodeHashHex action, String expected) {
		String hash = action.transformSelection(null, null, null, "blah-blah", null);
		assertEquals(expected, hash);
	}
}
