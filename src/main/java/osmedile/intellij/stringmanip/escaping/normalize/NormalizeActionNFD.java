package osmedile.intellij.stringmanip.escaping.normalize;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class NormalizeActionNFD extends AbstractStringManipAction<Object> {

	public NormalizeActionNFD() {
	}

	public NormalizeActionNFD(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
//		return com.ibm.icu.text.Normalizer2.getNFDInstance().normalize(s);
		return NormalizationType.NFD.normalize(s);
	}

}