package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.serialization.SerializationException;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.JDOMException;

import java.io.IOException;
/**
 * TODO add ID
 */
public class UniversalActionModel {
	private String actionClassName;
	private String modelClass;
	private String modelData;
	private String name;
	private String description;
	private String icon;
	private String textWithMnemonic;

	public UniversalActionModel() {
	}

	public UniversalActionModel(AnAction anAction, String actionClassName, Object model) throws SerializationException {
		this.actionClassName = actionClassName;
		if (model != null) {
			this.modelClass = model.getClass().getCanonicalName();
			this.modelData = JDOMUtil.write(XmlSerializer.serialize(model));
		}
		Presentation templatePresentation = anAction.getTemplatePresentation();
		setDescription(templatePresentation.getDescription());
		setTextWithMnemonic(templatePresentation.getTextWithMnemonic());
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getActionClassName() {
		return actionClassName;
	}

	public void setActionClassName(String actionClassName) {
		this.actionClassName = actionClassName;
	}

	public String getModelData() {
		return modelData;
	}

	public void setModelData(String modelData) {
		this.modelData = modelData;
	}

	public Object getModelAsObject() throws ClassNotFoundException, IOException, JDOMException {
		if (modelData == null) {
			return null;
		}
		return XmlSerializer.deserialize(JDOMUtil.load(modelData), Class.forName(modelClass));
	}

	public String getTextWithMnemonic() {
		return textWithMnemonic;
	}

	public void setTextWithMnemonic(String textWithMnemonic) {
		this.textWithMnemonic = textWithMnemonic;
	}
}
