package osmedile.intellij.stringmanip.encoding;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncodeBase64ActionTest {
	@Test
	public void zip() {
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.zipCheckBox.setSelected(true);

		String transform = new EncodeBase64Action().transform("Hello world!", base64EncodingDialog);
		assertEquals("H4sIAAAAAAAAAPNIzcnJVyjPL8pJUQQAlRmFGwwAAAA=", transform);
	}
}