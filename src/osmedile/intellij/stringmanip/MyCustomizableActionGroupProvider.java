package osmedile.intellij.stringmanip;

import com.intellij.ide.ui.customization.CustomizableActionGroupProvider;
import com.intellij.openapi.diagnostic.Logger;

public class MyCustomizableActionGroupProvider extends CustomizableActionGroupProvider {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(MyCustomizableActionGroupProvider.class);

	@Override
	public void registerGroups(CustomizableActionGroupRegistrar registrar) {
		registrar.addCustomizableActionGroup("StringManipulation.Group.Main", "String Manipulation - 弹出主窗口...");
		registrar.addCustomizableActionGroup("StringManipulation.Group.SwitchCase", "String Manipulation - 弹出转换...");
	}
}
