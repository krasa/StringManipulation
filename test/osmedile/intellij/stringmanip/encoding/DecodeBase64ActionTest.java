package osmedile.intellij.stringmanip.encoding;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static osmedile.intellij.stringmanip.encoding.EncodeBase64ActionTest.SAML_CRLF;

public class DecodeBase64ActionTest {

	@Test
	public void zip() {
		DecodeBase64Action decodeBase64Action = new DecodeBase64Action();
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.zip.setSelected(true);

		String transform = decodeBase64Action.transform("H4sIAAAAAAAAAPNIzcnJVyjPL8pJUQQAlRmFGwwAAAA=", base64EncodingDialog);
		assertEquals("Hello world!", transform);
	}

	@Test
	public void inflate() {
		DecodeBase64Action decodeBase64Action = new DecodeBase64Action();
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.inflateDeflate.setSelected(true);

		String transform = decodeBase64Action.transform("80jNyclXKM8vyklRBAA=", base64EncodingDialog);
		assertEquals("Hello world!", transform);
	}

	@Test
	public void inflate2() {
		DecodeBase64Action decodeBase64Action = new DecodeBase64Action();
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.inflateDeflate.setSelected(true);

		String transform = decodeBase64Action.transform("xVZZb+M2EH4v0P8g6N0WdR9I3LpJWxjI1ThYFH0JKHK4ZiGJgkjB7r/vSJYcX+uk26ILGDA1nOv7ZobklaZlUWfPoGtVabA2ZVHprBde221TZYpqqbOKlqAzw7Ll/P4u86YkqxtlFFOFvWdy2YJqDY2RqrKtxe21/ZpAwlkoopSmCWMBc4XwAy+GkIEfRCQSPIo9kboQ2dYnaDRaXtvoCM21bmFRaUMrgyLiBhMST9z4hbgZ/oLkD9u6BW1kRU1vtTKmzhxH11PY0LIuYMpU6XAolevIisNmWq/qHyjT6LsauXhR1/bjw893j78uHl4DAeDnJIj8NGQBxKFLXBfSWCQpIXnoxT4PoygJ7dn331nWVcdG1qfZzIbgkh9GL8FQTg3tQl85+wY7D3W2NNS0upccyW4UB+sTLVq4zLrutbNlyxhobTtb786J+23K87FGQ1U3Wu7YW6/X07U/Vc1nxyPEdX6/v1uyFZR0IvtSMLB3Vu8bDU3AY5f6NIFUYAuELIWUe0ECIiZB6qe+YIkgAYQiDr+iCfZ5+1fVGH0s2/xPYGaQjdIHJH1xay2fusVvLS2kkNBc7rr9eLb1i2pKai4XspNIPhG9amYaWmkJlbFnrzgv3EuDJA+Y6HiIOAChJPc5j3C88oiJBBcejwdk24SPUAzYblQlZBeia4J7MCvFL+fFyiwH2kBj7xxecHmLmK0HZR6rx2YuTEeTRzwsnTtxkxcSZd4wv8/AZN0B/F+n13kjxfkShLEjnDMtscWN2lx2qrqD+hNgzeBMi7odzg9wcVSoecuRFwaI1DSS7aV0qjP7aA8OaHZ2RzR8Oeag8Ab5gIl5a1ZVd8pAiaW0+s93Du4lHlPo5xwvW9X0WHXR9QCeJTmkaRTTnKck4JxR8BmBPHCTlPIcfBG74HsxE6l/SinmhRAMbMwZLt82bwq8xZ5BzC5edCxjnR6Kn/BvrRq+o/GMqxOqT3LZ39mRecizwbLkrYHj7RMFqxt+nGjJ7X75kaOHonEnHQ+fHPXY6bjvYvTXkoV3R2b+qjHYBp8HuFV9tmcGG2iEc6B+QsO4+w6QksrimyH5cW+o/lNUwNsnvO1UNRdCFpJun03fAGWL7wF9Gdo/cTfw1agC3K8i7Fh82O/j7viKQeH41Bnvhdnf", base64EncodingDialog);
		assertEquals(SAML_CRLF, transform);
	}
}