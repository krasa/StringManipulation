package osmedile.intellij.stringmanip.styles.custom;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.annotations.Transient;
import osmedile.intellij.stringmanip.styles.Style;

import java.util.List;
import java.util.UUID;

import static osmedile.intellij.stringmanip.styles.Style.valueOf;

public class CustomActionModel {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(CustomActionModel.class);

	private List<Step> steps = DefaultActions.getDefaultSteps();
	private String id;
	private String name;

	protected CustomActionModel() {
	}

	public static CustomActionModel create() {
		return create("StringManipulation." + UUID.randomUUID());
	}

	public static CustomActionModel create(String id) {
		CustomActionModel actionModel = new CustomActionModel();
		actionModel.setId(id);
		return actionModel;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
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

		CustomActionModel that = (CustomActionModel) o;

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
		return "CustomActionModel{" +
			"steps=" + steps +
			", id='" + id + '\'' +
			", name='" + name + '\'' +
			'}';
	}

	public static class Step {

		private String style;
		private boolean enabled = true;

		public Step() {
		}

		public Step(boolean enabled, Style style) {
			this.style = style.name();
			this.enabled = enabled;
		}

		@Transient
		public Style getStyleAsEnum() {
			try {
				return valueOf(style);
			} catch (Exception e) {
				return null;
			}
		}

		public String getStyle() {
			return style;
		}

		public void setStyle(String style) {
			this.style = style;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Step step = (Step) o;

			if (enabled != step.enabled) return false;
			return style != null ? style.equals(step.style) : step.style == null;

		}

		@Override
		public int hashCode() {
			int result = style != null ? style.hashCode() : 0;
			result = 31 * result + (enabled ? 1 : 0);
			return result;
		}

		@Override
		public String toString() {
			return "Step{" +
				"style=" + style +
				", enabled=" + enabled +
				'}';
		}
	}
}
