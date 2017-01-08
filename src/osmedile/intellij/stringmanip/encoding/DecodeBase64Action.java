package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import javax.swing.*;
import java.nio.charset.Charset;


/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class DecodeBase64Action extends AbstractStringManipAction {
	public static final Key<Charset> KEY = Key.create("StringManipulation.DecodeBase64Action.UserData");

	@Override
	public boolean doBeforeWriteAction(Editor editor, DataContext dataContext) {
		final Base64EncodingDialog base64EncodingDialog = new Base64EncodingDialog();
		DialogWrapper dialogWrapper = new DialogWrapper(editor.getProject()) {
			{
				init();
				setTitle("Choose Charset");
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
				return base64EncodingDialog.myComboBox;
			}

			@Override
			protected void doOKAction() {


				super.doOKAction();
			}
		};

		boolean b = dialogWrapper.showAndGet();
		if (!b) {
			return false;
		}

		try {
			Charset charset = Charset.forName(base64EncodingDialog.getCharset());
			editor.putUserData(KEY, charset);
			return true;
		} catch (Exception e) {
			Messages.showErrorDialog(editor.getProject(), String.valueOf(e), "Invalid Charset");
			return false;
		}
	}

	@Override
	protected void cleanupAfterWriteAction(Editor editor, DataContext dataContext) {
		super.cleanupAfterWriteAction(editor, dataContext);
		editor.putUserData(KEY, null);
	}


	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String s) {
		Charset charset = editor.getUserData(KEY);
		return new String(Base64.decodeBase64(s.getBytes(charset)), charset);
	}

	@Override
	public String transformByLine(String s) {
		throw new NotImplementedException();
	}
}