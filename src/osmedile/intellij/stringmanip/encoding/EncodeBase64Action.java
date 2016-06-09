package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import javax.swing.*;
import java.nio.charset.Charset;

/**
 * @author Olivier Smedile
 * @version $Id: EncodeBase64Action.java 29 2008-03-21 18:01:20Z osmedile $
 */
public class EncodeBase64Action extends AbstractStringManipAction {

	public String transformSelection(Editor editor, DataContext dataContext, final String s) {
		final Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
			{
				init();
				setTitle("Base64 Encoding Options");
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
			return s;
		}


		Charset charset = null;
		try {
			charset = Charset.forName(base64EncodingDialog.getCharset());
		} catch (Exception e) {
			return s;
		}
		byte[] bytes = s.getBytes(charset);
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

	@Override
	public String transform(String s) {
		throw new NotImplementedException();
	}
}