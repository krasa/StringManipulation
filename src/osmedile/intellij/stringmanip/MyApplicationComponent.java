package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.styles.action.CustomStyleAction;
import osmedile.intellij.stringmanip.styles.action.StyleActionModel;
import osmedile.intellij.stringmanip.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplicationComponent implements ApplicationComponent {

	private Class lastAction;
	private Map<Class, AnAction> classToActionMap;

	public static MyApplicationComponent getInstance() {
		return ApplicationManager.getApplication().getComponent(MyApplicationComponent.class);
	}

	public AnAction getAnAction() {
		return getActionMap().get(lastAction);
	}

	public static void setAction(Class itsAction) {
		if (itsAction != null) {
			getInstance().lastAction = itsAction;
			PluginPersistentStateComponent.getInstance().actionExecuted();
		}
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
		registerActions();
	}

	public void registerActions() {
		ActionManager instance = ActionManager.getInstance();
		DefaultActionGroup group = (DefaultActionGroup) instance.getAction("StringManipulation.Group.SwitchCase");
		List<StyleActionModel> styleActionModels = PluginPersistentStateComponent.getInstance().getStyleActionModels();

		unRegisterActions(styleActionModels);

		for (int i = styleActionModels.size() - 1; i >= 0; i--) {
			StyleActionModel styleActionModel = styleActionModels.get(i);
			String actionId = styleActionModel.getId();
			if (StringUtils.isNotBlank(actionId) && StringUtils.isNotBlank(styleActionModel.getName())) {
				CustomStyleAction action = new CustomStyleAction(styleActionModel);
				instance.registerAction(actionId, action, PluginId.getId("String Manipulation"));
				group.add(action, Constraints.FIRST);
			}
		}
	}

	public void unRegisterActions(List<StyleActionModel> styleActionModels) {
		ActionManager instance = ActionManager.getInstance();
		DefaultActionGroup group = (DefaultActionGroup) instance.getAction("StringManipulation.Group.SwitchCase");
		for (StyleActionModel actionModel : styleActionModels) {
			String id = actionModel.getId();
			if (StringUtils.isNotBlank(id)) {
				unRegisterAction(instance, id, group);
			}
		}
	}

	private void unRegisterAction(ActionManager instance, String actionId, DefaultActionGroup group) {
		AnAction action = instance.getActionOrStub(actionId);
		if (action != null) {
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
