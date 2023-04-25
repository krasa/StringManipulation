package osmedile.intellij.stringmanip;

import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;

public class CharacterSwitchingSettings {
    private Encoding selectedEncoding = Encoding.UNICODE;

    public CharacterSwitchingSettings() {
    }
    public static CharacterSwitchingSettings getInstance() {
   		return PluginPersistentStateComponent.getInstance().getCharacterSwitchingSettings();
   	}

   	public Encoding getSelectedEncoding() {
   		return selectedEncoding;
   	}

   	public void setSelectedEncoding(final Encoding selectedEncoding) {
   		this.selectedEncoding = selectedEncoding;
   	}

   	public boolean isUnicode() {
        return selectedEncoding == Encoding.UNICODE;
    }

   	public boolean isOctal() {
   		return selectedEncoding == Encoding.OCTAL;
   	}

   	public void resetToDefault() {
   		selectedEncoding = Encoding.UNICODE;
   	}

    public enum Encoding {
        UNICODE,
        OCTAL
    }
}
