package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;

import java.util.HashMap;
import java.util.Map;

public class MyApplicationService {
	private static final Logger LOG = Logger.getInstance(MyApplicationService.class);

	private Class lastAction;
	private CustomActionModel lastCustomActionModel;
	private Map<Class, AnAction> classToActionMap;

	public static MyApplicationService getInstance() {
		return ServiceManager.getService(MyApplicationService.class);
	}

	public AnAction getAnAction() {
		if (lastCustomActionModel != null) {
			return ActionManagerEx.getInstanceEx().getAction(lastCustomActionModel.getId());
		}
		return getActionMap().get(lastAction);
	}

	public static void setAction(Class aClass, CustomActionModel customActionModel) {
		if (aClass != null) {
			MyApplicationService instance = getInstance();
			instance.lastAction = aClass;
			instance.lastCustomActionModel = customActionModel;
		}
	}

	public static void setAction(Class aClass) {
		setAction(aClass, null);
	}

	@NotNull
	protected Map<Class, AnAction> getActionMap() {
		if (this.classToActionMap == null) {
			ActionManagerEx instanceEx = ActionManagerEx.getInstanceEx();
			PluginId pluginId = PluginId.getId("String Manipulation");

			HashMap<Class, AnAction> classToActionMap = new HashMap<Class, AnAction>();
			for (String string_manipulation : instanceEx.getPluginActions(pluginId)) {
				AnAction action = instanceEx.getAction(string_manipulation);
				classToActionMap.put(action.getClass(), action);
			}
			this.classToActionMap = classToActionMap;
		}
		return this.classToActionMap;
	}


}
