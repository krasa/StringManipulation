package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import osmedile.intellij.stringmanip.AbstractStringManipAction;
import osmedile.intellij.stringmanip.utils.TimestampUtils;

import java.util.Map;

public class TimestampDecodeAction extends AbstractStringManipAction<Object> {

    @Override
    protected String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, String selectedText, Object additionalParam) {
        return TimestampUtils.decodeTimestamp(selectedText);
    }

    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        throw new UnsupportedOperationException();
    }
}
