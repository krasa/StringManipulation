package osmedile.intellij.stringmanip.escaping.normalize;

import osmedile.intellij.stringmanip.escaping.DiacriticsToAsciiAction;

import java.text.Normalizer;

public enum NormalizationType {
	NOTHING {
		@Override
		public String normalize(String s) {
			return s;
		}
	},
	NFD {
		@Override
		public String normalize(String s) {
			return Normalizer.normalize(s, Normalizer.Form.NFD);
		}
	},
	NFC {
		@Override
		public String normalize(String s) {
			return Normalizer.normalize(s, Normalizer.Form.NFC);

		}
	},
	NFKD {
		@Override
		public String normalize(String s) {
			return Normalizer.normalize(s, Normalizer.Form.NFKD);
		}
	},
	NFKC {
		@Override
		public String normalize(String s) {
			return Normalizer.normalize(s, Normalizer.Form.NFKC);
		}
	},
	STRIP_ACCENTS {
		@Override
		public String normalize(String s) {
			return org.apache.commons.lang3.StringUtils.stripAccents(s);
		}
	},
	CONVERT_DIACRITICS {
		@Override
		public String normalize(String s) {
			return DiacriticsToAsciiAction.toPlain(s);
		}
	},
	;

	public abstract String normalize(String line);
}
