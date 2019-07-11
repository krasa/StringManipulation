package osmedile.intellij.stringmanip.encoding;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.lang.NotImplementedException;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.Charset;

public class EncodeHexAction extends DecodeHexAction {

    @Override
    public String transformSelection(Editor editor, DataContext dataContext, final String s, Charset charset) {
        return new String(Hex.encode(s.getBytes(charset)), charset);
    }

    @Override
    public String transformByLine(String s) {
        throw new NotImplementedException();
    }

    @Override
    protected String getDimensionServiceKey() {
        return "StringManipulation.HexEncodingDialog";
    }
}
