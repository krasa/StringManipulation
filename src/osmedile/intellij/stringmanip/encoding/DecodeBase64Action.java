package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;


/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class DecodeBase64Action extends AbstractStringManipAction<Base64EncodingDialog> {
	private static final Logger LOG = Logger.getInstance(DecodeBase64Action.class);

	@NotNull
	@Override
	public Pair<Boolean, Base64EncodingDialog> beforeWriteAction(Editor editor, DataContext dataContext) {
		final Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		base64EncodingDialog.encodingOptions.setVisible(false);
		base64EncodingDialog.lineEnd.setVisible(false);
		base64EncodingDialog.zip.setText("Unzip after decoding");
		base64EncodingDialog.inflateDeflate.setText("Inflate after decoding");
		base64EncodingDialog.inflateDeflate.setToolTipText("");

		DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
			{
				init();
				setTitle("Decode Base64");
			}

			@Nullable
			@Override
			public JComponent getPreferredFocusedComponent() {
				return base64EncodingDialog.myComboBox;
			}

			@Nullable
			@Override
			protected String getDimensionServiceKey() {
				return "StringManipulation.Base64DecodingDialog";
			}

			@Nullable
			@Override
			protected JComponent createCenterPanel() {
				return base64EncodingDialog.contentPane;
			}

			@Override
			protected void doOKAction() {
				super.doOKAction();
			}
		};

		boolean b = dialogWrapper.showAndGet();
		if (!b) {
			return stopExecution();
		}

		try {
			Charset charset = Charset.forName(base64EncodingDialog.getCharset());
			return continueExecution(base64EncodingDialog);
		} catch (Exception e) {
			Messages.showErrorDialog(editor.getProject(), String.valueOf(e), "Invalid Charset");
			return stopExecution();
		}
	}

	@Override
	protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String s, Base64EncodingDialog dialog) {
		try {
			return transform(s, dialog);
		} catch (Exception e) {
			LOG.warn(e);
			SwingUtilities.invokeLater(() -> Messages.showErrorDialog(editor.getProject(), String.valueOf(e), "Error"));
			throw new ProcessCanceledException(e);
		}
	}

	@NotNull
	protected String transform(String s, Base64EncodingDialog dialog) {
		Charset charset = Charset.forName(dialog.getCharset());
		byte[] decodeBase64 = Base64.decodeBase64(s.getBytes(charset));

		if (dialog.zip.isSelected()) {
			decodeBase64 = decompress(decodeBase64);
		} else if (dialog.inflateDeflate.isSelected()) {
			decodeBase64 = inflate(decodeBase64);
		}

		return new String(decodeBase64, charset);
	}

	protected byte[] inflate(byte[] input) {
		try {
			Inflater inflater = new Inflater(true);
			inflater.setInput(input);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input.length);

			byte[] buffer = new byte[1024];
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}

			outputStream.close();
			return outputStream.toByteArray();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] decompress(byte[] decoded) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(decoded);
			GZIPInputStream gzip = new GZIPInputStream(in);
			byte[] bytes = IOUtils.toByteArray(gzip);
			gzip.close();
			return bytes;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new UnsupportedOperationException();
	}
}