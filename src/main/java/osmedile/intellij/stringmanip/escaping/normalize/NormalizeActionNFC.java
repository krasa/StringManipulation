package osmedile.intellij.stringmanip.escaping.normalize;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class NormalizeActionNFC extends AbstractStringManipAction<Object> {

	public NormalizeActionNFC() {
	}

	public NormalizeActionNFC(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
//		return com.ibm.icu.text.Normalizer2.getNFCInstance().normalize(s);
		return NormalizationType.NFC.normalize(s);
	}

}               