package com.github.krasa.stringmanipulation.intellij.encoding;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.github.krasa.stringmanipulation.intellij.utils.EncodingUtils;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;

public class URLDecodeRFC3986Action extends AbstractStringManipAction {

    @Override
    protected String transformSelection(Editor editor, DataContext dataContext, String selectedText, Object additionalParam) {
        return EncodingUtils.decodeUrlRFC3986(selectedText);
    }

    @Override
    public String transformByLine(String s) {
        throw new UnsupportedOperationException();
    }
}