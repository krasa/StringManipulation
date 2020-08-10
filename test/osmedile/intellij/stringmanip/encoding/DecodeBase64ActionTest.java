package osmedile.intellij.stringmanip.encoding;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecodeBase64ActionTest {
	@Test
	public void zip() {
		DecodeBase64Action decodeBase64Action = new DecodeBase64Action();
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.zipCheckBox.setSelected(true);

		String transform = decodeBase64Action.transform("H4sIAAAAAAAAAPNIzcnJVyjPL8pJUQQAlRmFGwwAAAA=", base64EncodingDialog);
		assertEquals("Hello world!", transform);
	}
}