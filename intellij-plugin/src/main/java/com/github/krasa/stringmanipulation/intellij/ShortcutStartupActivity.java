package com.github.krasa.stringmanipulation.intellij;

import com.github.krasa.stringmanipulation.intellij.config.PluginPersistentStateComponent;
import com.github.krasa.stringmanipulation.intellij.styles.custom.CustomAction;
import com.github.krasa.stringmanipulation.intellij.styles.custom.CustomActionModel;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ShortcutStartupActivity implements StartupActivity {

	private static final Logger LOG = Logger.getInstance(ShortcutStartupActivity.class);

	private volatile boolean registered = false;

	@Override
	public void runActivity(@NotNull Project project) {
		if (!registered) {
			registerActions();
			registered = true;
		}
	}


	public static void registerActions() {
		ActionManager actionManager = ActionManager.getInstance();
		DefaultActionGroup group = (DefaultActionGroup) actionManager.getAction("StringManipulation.Group.SwitchCase");
		List<CustomActionModel> customActionModels = PluginPersistentStateComponent.getInstance().getCustomActionModels();

		unRegisterActions(customActionModels);

		for (int i = customActionModels.size() - 1; i >= 0; i--) {
			CustomActionModel customActionModel = customActionModels.get(i);
			registerAction(actionManager, group, customActionModel);
		}
	}

	protected static void registerAction(ActionManager actionManager, DefaultActionGroup group, CustomActionModel customActionModel) {
		if (StringUtils.isNotBlank(customActionModel.getId()) && StringUtils.isNotBlank(customActionModel.getName())) {
			CustomAction action = new CustomAction(customActionModel);
			LOG.info("Registering " + action + " id:" + customActionModel.getId());
			actionManager.registerAction(customActionModel.getId(), action, PluginId.getId("String Manipulation"));
			group.add(action, Constraints.FIRST);


			CustomActionModel reverse = customActionModel.reverse();
			CustomAction reverseAction = new CustomAction(reverse);
			LOG.info("Registering " + reverseAction + " id:" + reverse.getId());
			actionManager.registerAction(reverse.getId(), reverseAction, PluginId.getId("String Manipulation"));
//			group.add(reverseAction, Constraints.FIRST);
		}
	}

	public static void unRegisterActions(List<CustomActionModel> customActionModels) {
		ActionManager instance = ActionManager.getInstance();
		DefaultActionGroup group = (DefaultActionGroup) instance.getAction("StringManipulation.Group.SwitchCase");
		for (CustomActionModel actionModel : customActionModels) {
			String id = actionModel.getId();
			if (StringUtils.isNotBlank(id)) {
				unRegisterAction(instance, id, group);
				unRegisterAction(instance, id + CustomActionModel.REVERSE, group);
			}
		}
	}

	private static void unRegisterAction(ActionManager instance, String actionId, DefaultActionGroup group) {
		AnAction action = instance.getActionOrStub(actionId);
		if (action != null) {
			LOG.info("Unregistering " + action + " id:" + actionId);
			group.remove(action);
			instance.unregisterAction(actionId);
		}
	}

}
