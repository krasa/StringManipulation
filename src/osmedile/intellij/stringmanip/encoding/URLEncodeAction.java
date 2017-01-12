package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang.NotImplementedException;
import osmedile.intellij.stringmanip.AbstractStringManipAction;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Olivier Smedile
 * @version $Id: EscapeHtmlAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class URLEncodeAction extends AbstractStringManipAction {

	@Override
	protected String transformSelection(Editor editor, DataContext dataContext, String selectedText, Object additionalParam) {

        try {
					return URLEncoder.encode(selectedText, "UTF-8");
				} catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}

	@Override
	public String transformByLine(String s) {
		throw new NotImplementedException();
	}
}