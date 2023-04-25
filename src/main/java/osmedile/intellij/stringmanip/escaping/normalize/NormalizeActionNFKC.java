package osmedile.intellij.stringmanip.escaping.normalize;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.util.Map;

public class NormalizeActionNFKC extends AbstractStringManipAction<Object> {

	public NormalizeActionNFKC() {
	}

	public NormalizeActionNFKC(boolean setupHandler) {
		super(setupHandler);
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
//		return com.ibm.icu.text.Normalizer2.getNFKCInstance().normalize(s);
		return NormalizationType.NFKC.normalize(s);
	}

}