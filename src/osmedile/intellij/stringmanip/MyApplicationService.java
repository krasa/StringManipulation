package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;

import java.util.HashMap;
import java.util.Map;

public class MyApplicationService {
	private static final Logger LOG = Logger.getInstance(MyApplicationService.class);

	private Class lastAction;
	private Object lastCustomActionModel;
	private Map<Class, AnAction> classToActionMap;

	public static MyApplicationService getInstance() {
		return ApplicationManager.getApplication().getService(MyApplicationService.class);
	}

	public MyApplicationService() {
		UniversalActionModel value = PluginPersistentStateComponent.getInstance().getLastAction();
		if (value != null) {
			try {
				lastAction = Class.forName(value.getActionClassName());
				lastCustomActionModel = value.getModelAsObject();
			} catch (Throwable e) {
				LOG.debug(e);
			}
		}
	}

	@NotNull
	public Pair<AnAction, Object> setAction() {
		if (lastCustomActionModel instanceof CustomActionModel) {
			AnAction action = ActionManagerEx.getInstanceEx().getAction(((CustomActionModel) lastCustomActionModel).getId());
			return Pair.pair(action, lastCustomActionModel);
		}

		return Pair.pair(getActionMap().get(lastAction), lastCustomActionModel);
	}

	public static void setAction(Class aClass, Object customActionModel) {
		if (aClass != null) {
			MyApplicationService instance = getInstance();
			instance.lastAction = aClass;
			instance.lastCustomActionModel = customActionModel;
			PluginPersistentStateComponent.getInstance().setLastAction(new UniversalActionModel(aClass.getCanonicalName(), customActionModel));
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
