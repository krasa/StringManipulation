package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * @author Olivier Smedile
 * @version $Id: EncodeBase64Action.java 29 2008-03-21 18:01:20Z osmedile $
 */
public class EncodeBase64Action extends AbstractStringManipAction<Base64EncodingDialog> {

	@NotNull
	@Override
	public Pair<Boolean, Base64EncodingDialog> beforeWriteAction(Editor editor, DataContext dataContext) {
		final Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
			{
				init();
				setTitle("Encode Base64");
			}

			@Nullable
			@Override
			public JComponent getPreferredFocusedComponent() {
				return base64EncodingDialog.myComboBox;
			}

			@Nullable
			@Override
			protected String getDimensionServiceKey() {
				return "StringManipulation.Base64EncodingDialog";
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
			Charset.forName(base64EncodingDialog.getCharset());
		} catch (Exception e) {
			Messages.showErrorDialog(editor.getProject(), String.valueOf(e), "Invalid Charset");
			return stopExecution();
		}
		return continueExecution(base64EncodingDialog);
	}

	@Override
	public String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, final String s, Base64EncodingDialog base64EncodingDialog) {
		return transform(s, base64EncodingDialog);
	}

	protected String transform(String s, Base64EncodingDialog base64EncodingDialog) {
		Charset charset = null;
		try {
			charset = Charset.forName(base64EncodingDialog.getCharset());
		} catch (Exception e) {
			return s;
		}
		byte[] bytes = s.getBytes(charset);

		if (base64EncodingDialog.zipCheckBox.isSelected()) {
			bytes = compress(bytes);
		}

		if (base64EncodingDialog.defaultRadioButton.isSelected()) {
			return new String(Base64.encodeBase64(bytes), charset);
		} else if (base64EncodingDialog.urlSafe.isSelected()) {
			return new String(Base64.encodeBase64(bytes, false, true), charset);

		} else if (base64EncodingDialog.urlSafeChunked.isSelected()) {
			return new String(Base64.encodeBase64(bytes, true, true), charset);

		} else if (base64EncodingDialog.chunked.isSelected()) {
			return new String(Base64.encodeBase64(bytes, true, false), charset);
		}
		throw new IllegalStateException();
	}

	public static byte[] compress(byte[] str) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(str);
			gzip.close();
			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String transformByLine(Map<String, Object> actionContext, String s) {
		throw new UnsupportedOperationException();
	}
}