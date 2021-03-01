package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.utils.StringUtil;

import java.util.Map;

public class ToSpringBootEnvironmentVariableAction extends AbstractCaseConvertingAction {
    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        return StringUtil.toSpringEnvVariable(s);
    }
}
