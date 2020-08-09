package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang3.NotImplementedException;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.Charset;
import java.util.Map;

public class EncodeHexAction extends DecodeHexAction {

    @Override
    public String transformSelection(Editor editor, Map<String, Object> actionContext, DataContext dataContext, final String s, Charset charset) {
        return new String(Hex.encode(s.getBytes(charset)), charset);
    }

    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        throw new NotImplementedException();
    }

    @Override
    protected String getDimensionServiceKey() {
        return "StringManipulation.HexEncodingDialog";
    }
}
