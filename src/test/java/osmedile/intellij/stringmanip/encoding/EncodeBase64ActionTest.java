package osmedile.intellij.stringmanip.encoding;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncodeBase64ActionTest {

	public static final String SAML_LF = "<samlp:Response xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\" ID=\"_8e8dc5f69a98cc4c1ff3427e5ce34606fd672f91e6\" Version=\"2.0\" IssueInstant=\"2014-07-17T01:01:48Z\" Destination=\"http://sp.example.com/demo1/index.php?acs\" InResponseTo=\"ONELOGIN_4fee3b046395c4e751011e97f8900b5273d56685\">\n" +
			"  <saml:Issuer>http://idp.example.com/metadata.php</saml:Issuer>\n" +
			"  <samlp:Status>\n" +
			"    <samlp:StatusCode Value=\"urn:oasis:names:tc:SAML:2.0:status:Success\"/>\n" +
			"  </samlp:Status>\n" +
			"  <saml:Assertion xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" ID=\"_d71a3a8e9fcc45c9e9d248ef7049393fc8f04e5f75\" Version=\"2.0\" IssueInstant=\"2014-07-17T01:01:48Z\">\n" +
			"    <saml:Issuer>http://idp.example.com/metadata.php</saml:Issuer>\n" +
			"    <saml:Subject>\n" +
			"      <saml:NameID SPNameQualifier=\"http://sp.example.com/demo1/metadata.php\" Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:transient\">_ce3d2948b4cf20146dee0a0b3dd6f69b6cf86f62d7</saml:NameID>\n" +
			"      <saml:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\">\n" +
			"        <saml:SubjectConfirmationData NotOnOrAfter=\"2024-01-18T06:21:48Z\" Recipient=\"http://sp.example.com/demo1/index.php?acs\" InResponseTo=\"ONELOGIN_4fee3b046395c4e751011e97f8900b5273d56685\"/>\n" +
			"      </saml:SubjectConfirmation>\n" +
			"    </saml:Subject>\n" +
			"    <saml:Conditions NotBefore=\"2014-07-17T01:01:18Z\" NotOnOrAfter=\"2024-01-18T06:21:48Z\">\n" +
			"      <saml:AudienceRestriction>\n" +
			"        <saml:Audience>http://sp.example.com/demo1/metadata.php</saml:Audience>\n" +
			"      </saml:AudienceRestriction>\n" +
			"    </saml:Conditions>\n" +
			"    <saml:AuthnStatement AuthnInstant=\"2014-07-17T01:01:48Z\" SessionNotOnOrAfter=\"2024-07-17T09:01:48Z\" SessionIndex=\"_be9967abd904ddcae3c0eb4189adbe3f71e327cf93\">\n" +
			"      <saml:AuthnContext>\n" +
			"        <saml:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Password</saml:AuthnContextClassRef>\n" +
			"      </saml:AuthnContext>\n" +
			"    </saml:AuthnStatement>\n" +
			"    <saml:AttributeStatement>\n" +
			"      <saml:Attribute Name=\"uid\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">test</saml:AttributeValue>\n" +
			"      </saml:Attribute>\n" +
			"      <saml:Attribute Name=\"mail\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">test@example.com</saml:AttributeValue>\n" +
			"      </saml:Attribute>\n" +
			"      <saml:Attribute Name=\"eduPersonAffiliation\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">users</saml:AttributeValue>\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">examplerole1</saml:AttributeValue>\n" +
			"      </saml:Attribute>\n" +
			"    </saml:AttributeStatement>\n" +
			"  </saml:Assertion>\n" +
			"</samlp:Response>";
	public static final String SAML_CRLF = "<samlp:Response xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\" ID=\"_8e8dc5f69a98cc4c1ff3427e5ce34606fd672f91e6\" Version=\"2.0\" IssueInstant=\"2014-07-17T01:01:48Z\" Destination=\"http://sp.example.com/demo1/index.php?acs\" InResponseTo=\"ONELOGIN_4fee3b046395c4e751011e97f8900b5273d56685\">\r\n" +
			"  <saml:Issuer>http://idp.example.com/metadata.php</saml:Issuer>\r\n" +
			"  <samlp:Status>\r\n" +
			"    <samlp:StatusCode Value=\"urn:oasis:names:tc:SAML:2.0:status:Success\"/>\r\n" +
			"  </samlp:Status>\r\n" +
			"  <saml:Assertion xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" ID=\"_d71a3a8e9fcc45c9e9d248ef7049393fc8f04e5f75\" Version=\"2.0\" IssueInstant=\"2014-07-17T01:01:48Z\">\r\n" +
			"    <saml:Issuer>http://idp.example.com/metadata.php</saml:Issuer>\r\n" +
			"    <saml:Subject>\r\n" +
			"      <saml:NameID SPNameQualifier=\"http://sp.example.com/demo1/metadata.php\" Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:transient\">_ce3d2948b4cf20146dee0a0b3dd6f69b6cf86f62d7</saml:NameID>\r\n" +
			"      <saml:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\">\r\n" +
			"        <saml:SubjectConfirmationData NotOnOrAfter=\"2024-01-18T06:21:48Z\" Recipient=\"http://sp.example.com/demo1/index.php?acs\" InResponseTo=\"ONELOGIN_4fee3b046395c4e751011e97f8900b5273d56685\"/>\r\n" +
			"      </saml:SubjectConfirmation>\r\n" +
			"    </saml:Subject>\r\n" +
			"    <saml:Conditions NotBefore=\"2014-07-17T01:01:18Z\" NotOnOrAfter=\"2024-01-18T06:21:48Z\">\r\n" +
			"      <saml:AudienceRestriction>\r\n" +
			"        <saml:Audience>http://sp.example.com/demo1/metadata.php</saml:Audience>\r\n" +
			"      </saml:AudienceRestriction>\r\n" +
			"    </saml:Conditions>\r\n" +
			"    <saml:AuthnStatement AuthnInstant=\"2014-07-17T01:01:48Z\" SessionNotOnOrAfter=\"2024-07-17T09:01:48Z\" SessionIndex=\"_be9967abd904ddcae3c0eb4189adbe3f71e327cf93\">\r\n" +
			"      <saml:AuthnContext>\r\n" +
			"        <saml:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Password</saml:AuthnContextClassRef>\r\n" +
			"      </saml:AuthnContext>\r\n" +
			"    </saml:AuthnStatement>\r\n" +
			"    <saml:AttributeStatement>\r\n" +
			"      <saml:Attribute Name=\"uid\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">\r\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">test</saml:AttributeValue>\r\n" +
			"      </saml:Attribute>\r\n" +
			"      <saml:Attribute Name=\"mail\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">\r\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">test@example.com</saml:AttributeValue>\r\n" +
			"      </saml:Attribute>\r\n" +
			"      <saml:Attribute Name=\"eduPersonAffiliation\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">\r\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">users</saml:AttributeValue>\r\n" +
			"        <saml:AttributeValue xsi:type=\"xs:string\">examplerole1</saml:AttributeValue>\r\n" +
			"      </saml:Attribute>\r\n" +
			"    </saml:AttributeStatement>\r\n" +
			"  </saml:Assertion>\r\n" +
			"</samlp:Response>";
	public static final String SAML_DEFLATED_CRLF = "xVZZb+M2EH4v0P8g6N0WdR9I3LpJWxjI1ThYFH0JKHK4ZiGJgkjB7r/vSJYcX+uk26ILGDA1nOv7ZobklaZlUWfPoGtVabA2ZVHprBde221TZYpqqbOKlqAzw7Ll/P4u86YkqxtlFFOFvWdy2YJqDY2RqrKtxe21/ZpAwlkoopSmCWMBc4XwAy+GkIEfRCQSPIo9kboQ2dYnaDRaXtvoCM21bmFRaUMrgyLiBhMST9z4hbgZ/oLkD9u6BW1kRU1vtTKmzhxH11PY0LIuYMpU6XAolevIisNmWq/qHyjT6LsauXhR1/bjw893j78uHl4DAeDnJIj8NGQBxKFLXBfSWCQpIXnoxT4PoygJ7dn331nWVcdG1qfZzIbgkh9GL8FQTg3tQl85+wY7D3W2NNS0upccyW4UB+sTLVq4zLrutbNlyxhobTtb786J+23K87FGQ1U3Wu7YW6/X07U/Vc1nxyPEdX6/v1uyFZR0IvtSMLB3Vu8bDU3AY5f6NIFUYAuELIWUe0ECIiZB6qe+YIkgAYQiDr+iCfZ5+1fVGH0s2/xPYGaQjdIHJH1xay2fusVvLS2kkNBc7rr9eLb1i2pKai4XspNIPhG9amYaWmkJlbFnrzgv3EuDJA+Y6HiIOAChJPc5j3C88oiJBBcejwdk24SPUAzYblQlZBeia4J7MCvFL+fFyiwH2kBj7xxecHmLmK0HZR6rx2YuTEeTRzwsnTtxkxcSZd4wv8/AZN0B/F+n13kjxfkShLEjnDMtscWN2lx2qrqD+hNgzeBMi7odzg9wcVSoecuRFwaI1DSS7aV0qjP7aA8OaHZ2RzR8Oeag8Ab5gIl5a1ZVd8pAiaW0+s93Du4lHlPo5xwvW9X0WHXR9QCeJTmkaRTTnKck4JxR8BmBPHCTlPIcfBG74HsxE6l/SinmhRAMbMwZLt82bwq8xZ5BzC5edCxjnR6Kn/BvrRq+o/GMqxOqT3LZ39mRecizwbLkrYHj7RMFqxt+nGjJ7X75kaOHonEnHQ+fHPXY6bjvYvTXkoV3R2b+qjHYBp8HuFV9tmcGG2iEc6B+QsO4+w6QksrimyH5cW+o/lNUwNsnvO1UNRdCFpJun03fAGWL7wF9Gdo/cTfw1agC3K8i7Fh82O/j7viKQeH41Bnvhdnf";
	public static final String SAML_DEFLATED___LF = "xVbZbts4FH3vVwh6t0XtEpF46iYzAwPZJg6KQV8CirysOdAGkYI9fz9XsuR4Sd10QQcIEPmuPOcu5IVmRV7TR9B1VWqwNkVeatoLL+22KWnFtNK0ZAVoajhdzm9vqDcltG4qU/Eqt/dcznswraExqipta3F9aT8nkAgeyihlacJ5wF0p/cCLIeTgBxGJpIhiT6YuRLb1ERqNnpc2BkJ3rVtYlNqw0qCIuMGExBM3fiIuxb8g+WRb16CNKpnpvVbG1NRxdD2FDSvqHKa8KhwBReU6qhSwmdar+jfGNcYuRy6eqkv7/u73m/s/F3fPgQTwMxJEfhryAOLQJa4LaSyTlJAs9GJfhFGUhPbsnWVddGTQ/pTNbMitxGHyAgwTzLAu84Wz7zAGqOnSMNPqTnAkuqoEWB9Z3sJ5ynVvTZct56C17fSxnePg2+POx/IMBd1otSNuvV5P1/60aj47HiGu8/ftzZKvoGAT1VeBg73z+rrTUH8Ru8xnCaQSqx/yFFLhBQnImASpn/qSJ5IEEMo4/I7675H2I4UYQyzb7B/gZisahXfI9+LaWj50H3+1LFdSQXO+2/az2dYfVVMwc76GnUSJiexNqWlYqRWUxp4945wILw2SLOCyIyESAISRzBciwrHKIi4T/PBEPODaHvgQxIDsqiql6jJ0DXALZlWJ88fiBc2ANdDYY7wzEa8RsXVXmfvyvplL05HkEQ+r5k7c5IlE1Bum9hG4qjt4v3RmnR0lzpcQDM3gnHbDFjTaCtUZ6g7nB8BywSut6XYg30DEYY3mrUBOOCBK0yj+cp5Tk9lbm2+AsvM7ZOCLGQf9C9x9EuatWZXdXoECS2j1P7+yppe4lzDMa5RsTdNj00VXe1wfGaRpFLNMpCQQgjPwOYEscJOUiQx8GbvgezGXqX/CJh4LARjYmFMaX3RXOd5YjyBnZy81Tnlnh+IH/LeuGrGj8JVQxywfn2RfsSPygGKD9chaA0faE73VDTuOsBJ2//mWVcPQuZOOyyZDO34y37sU/QVk4T1Bzb815trgKwBV5Wd7ZrBvRiwH5scMjMrzKAqm8v8Lxvu9OfqJkEC0D3ipVeVcSpUrtn0Y/XqILd76+iyub4k2cNVUObjfQdax9KDJR+X4Tpm9G58y4/af/Qc=";

	@Test
	public void zip() {
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.zip.setSelected(true);

		String transform = new EncodeBase64Action().transform("Hello world!", base64EncodingDialog);
		assertEquals("H4sIAAAAAAAAAPNIzcnJVyjPL8pJUQQAlRmFGwwAAAA=", transform);
	}

	@Test
	public void deflate() {
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.inflateDeflate.setSelected(true);

		String transform = new EncodeBase64Action().transform("Hello world!", base64EncodingDialog);
		assertEquals("80jNyclXKM8vyklRBAA=", transform);
	}

	@Test
	public void deflate2() {
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.inflateDeflate.setSelected(true);
		base64EncodingDialog.crlf.setSelected(true);

		String transform = new EncodeBase64Action().transform(SAML_CRLF, base64EncodingDialog);
		assertEquals(SAML_DEFLATED_CRLF, transform);
	}

	@Test
	public void deflate3() {
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.inflateDeflate.setSelected(true);
		base64EncodingDialog.crlf.setSelected(true);

		String transform = new EncodeBase64Action().transform(SAML_LF, base64EncodingDialog);
		assertEquals(SAML_DEFLATED_CRLF, transform);
	}

	@Test
	public void lf() {
		Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.inflateDeflate.setSelected(true);
		base64EncodingDialog.lf.setSelected(true);

		String transform = new EncodeBase64Action().transform(SAML_LF, base64EncodingDialog);
		assertEquals(SAML_DEFLATED___LF, transform);

		transform = new EncodeBase64Action().transform(SAML_CRLF, base64EncodingDialog);
		assertEquals(SAML_DEFLATED___LF, transform);

		DecodeBase64Action decodeBase64Action = new DecodeBase64Action();
		base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.inflateDeflate.setSelected(true);

		transform = decodeBase64Action.transform(transform, base64EncodingDialog);
		assertEquals(EncodeBase64ActionTest.SAML_LF, transform);
	}
}