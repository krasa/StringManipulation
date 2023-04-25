package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.ui.Messages;
import org.bouncycastle.util.encoders.Hex;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author Olivier Smedile
 * @version $Id: EncodeMd5Hex16Action.java 32 2008-03-24 10:15:55Z osmedile $
 */
public abstract class EncodeHashHex extends AbstractStringManipAction<Object> {
	private static final Logger LOG = Logger.getInstance(EncodeHashHex.class);

	private String algorithm;

	EncodeHashHex(String algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
		try {
			byte[] hash = MessageDigest.getInstance(algorithm).digest(selectedText.getBytes("UTF-8"));
			return Hex.toHexString(hash);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			SwingUtilities.invokeLater(() -> Messages.showErrorDialog(editor.getProject(), e.getMessage(), "Encoding Error"));
			LOG.info(e);
			throw new ProcessCanceledException(e);
		}
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new UnsupportedOperationException();
	}
}
