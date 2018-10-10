package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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
