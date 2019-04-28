package osmedile.intellij.stringmanip.styles.action;

import com.intellij.openapi.diagnostic.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StyleActionModel {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(StyleActionModel.class);

	private List<StyleStep> steps = new ArrayList<>(DefaultActions.DEFAULT);
	private String id;
	private String name;

	protected StyleActionModel() {
	}

	public static StyleActionModel create() {
		return create("StringManipulation." + UUID.randomUUID());
	}

	public static StyleActionModel create(String id) {
		StyleActionModel actionModel = new StyleActionModel();
		actionModel.setId(id);
		return actionModel;
	}

	public List<StyleStep> getSteps() {
		return steps;
	}

	public void setSteps(List<StyleStep> steps) {
		this.steps = steps;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StyleActionModel that = (StyleActionModel) o;

		if (steps != null ? !steps.equals(that.steps) : that.steps != null) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		return name != null ? name.equals(that.name) : that.name == null;

	}

	@Override
	public int hashCode() {
		int result = steps != null ? steps.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "StyleActionModel{" +
			"steps=" + steps +
			", id='" + id + '\'' +
			", name='" + name + '\'' +
			'}';
	}
}
