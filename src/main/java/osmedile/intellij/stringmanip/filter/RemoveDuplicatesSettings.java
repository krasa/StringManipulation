package osmedile.intellij.stringmanip.filter;

public class RemoveDuplicatesSettings {
	Type type = Type.REMOVE_LINE;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	enum Type {
		REMOVE_LINE, REMOVE_LINE_KEEP_EMPTY, REMOVE_SELECTION
	}
}
