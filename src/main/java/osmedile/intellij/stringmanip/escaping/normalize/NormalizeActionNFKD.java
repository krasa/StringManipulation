package osmedile.intellij.stringmanip.escaping.normalize;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class NormalizeActionNFKD extends AbstractStringManipAction<Object> {

	public NormalizeActionNFKD() {
	}

	public NormalizeActionNFKD(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
//		return com.ibm.icu.text.Normalizer2.getNFKDInstance().normalize(s);
		return NormalizationType.NFKD.normalize(s);
	}

}