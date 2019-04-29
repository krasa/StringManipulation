package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.styles.custom.CustomAction;
import osmedile.intellij.stringmanip.styles.custom.CustomActionModel;
import shaded.org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplicationComponent implements ApplicationComponent {
	private static final Logger LOG = Logger.getInstance(MyApplicationComponent.class);

	private Class lastAction;
	private CustomActionModel lastCustomActionModel;
	private Map<Class, AnAction> classToActionMap;

	public static MyApplicationComponent getInstance() {
		return ApplicationManager.getApplication().getComponent(MyApplicationComponent.class);
	}

	public AnAction getAnAction() {
		if (lastCustomActionModel != null) {
			return ActionManagerEx.getInstanceEx().getAction(lastCustomActionModel.getId());
		}
		return getActionMap().get(lastAction);
	}

	public static void setAction(Class aClass, CustomActionModel customActionModel) {
		if (aClass != null) {
			MyApplicationComponent instance = getInstance();
			instance.lastAction = aClass;
			instance.lastCustomActionModel = customActionModel;
			PluginPersistentStateComponent.getInstance().actionExecuted();
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

	@Override
	public void initComponent() {
		try {
			registerActions();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public void registerActions() {
		ActionManager instance = ActionManager.getInstance();
		DefaultActionGroup group = (DefaultActionGroup) instance.getAction("StringManipulation.Group.SwitchCase");
		List<CustomActionModel> customActionModels = PluginPersistentStateComponent.getInstance().getCustomActionModels();

		unRegisterActions(customActionModels);

		for (int i = customActionModels.size() - 1; i >= 0; i--) {
			CustomActionModel customActionModel = customActionModels.get(i);
			registerAction(instance, group, customActionModel);
		}
	}

	protected void registerAction(ActionManager instance, DefaultActionGroup group, CustomActionModel customActionModel) {
		String actionId = customActionModel.getId();
		if (StringUtils.isNotBlank(actionId) && StringUtils.isNotBlank(customActionModel.getName())) {
			CustomAction action = new CustomAction(customActionModel);
			LOG.info("Registering " + action + " id:" + actionId);
			instance.registerAction(actionId, action, PluginId.getId("String Manipulation"));
			group.add(action, Constraints.FIRST);
		}
	}

	public void unRegisterActions(List<CustomActionModel> customActionModels) {
		ActionManager instance = ActionManager.getInstance();
		DefaultActionGroup group = (DefaultActionGroup) instance.getAction("StringManipulation.Group.SwitchCase");
		for (CustomActionModel actionModel : customActionModels) {
			String id = actionModel.getId();
			if (StringUtils.isNotBlank(id)) {
				unRegisterAction(instance, id, group);
			}
		}
	}

	private void unRegisterAction(ActionManager instance, String actionId, DefaultActionGroup group) {
		AnAction action = instance.getActionOrStub(actionId);
		if (action != null) {
			LOG.info("Unregistering " + action + " id:" + actionId);
			group.remove(action);
			instance.unregisterAction(actionId);
		}
	}

	@Override
	public void disposeComponent() {

	}

	@NotNull
	@Override
	public String getComponentName() {
		return getClass().getName();
	}
}
