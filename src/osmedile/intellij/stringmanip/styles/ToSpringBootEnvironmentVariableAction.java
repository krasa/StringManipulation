package osmedile.intellij.stringmanip.styles;

import java.util.Map;

public class ToSpringBootEnvironmentVariableAction extends AbstractCaseConvertingAction {
    @Override
    public String transformByLine(Map<String, Object> actionContext, String s) {
        return Style.SPRING_BOOT_ENVIRONMENT_VARIABLE.transform(s);
    }
}
