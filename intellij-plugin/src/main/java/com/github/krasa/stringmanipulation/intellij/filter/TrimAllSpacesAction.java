package com.github.krasa.stringmanipulation.intellij.filter;

import com.github.krasa.stringmanipulation.intellij.AbstractStringManipAction;
import com.github.krasa.stringmanipulation.utils.common.StringUtil;

/**
 * @author Olivier Smedile
 * @version $Id: TrimAllSpacesAction.java 16 2008-03-20 19:21:43Z osmedile $
 */
public class TrimAllSpacesAction extends AbstractStringManipAction {

	@Override
    public String transformByLine(String s) {
        return StringUtil.trimAllSpace(s);
    }
}