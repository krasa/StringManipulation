package osmedile.intellij.stringmanip.config;

import com.intellij.openapi.diagnostic.Logger;
import osmedile.intellij.stringmanip.CharacterSwitchingSettings;

import javax.swing.*;

public class CharacterSwitchingSettingsForm {
    private static final Logger LOG = Logger.getInstance(CharacterSwitchingSettingsForm.class);

    private JRadioButton unicodeRadio;
    private JRadioButton octalRadio;
    private JPanel root;

    public CharacterSwitchingSettingsForm() {
    }

    public void setData(CharacterSwitchingSettings data) {
        unicodeRadio.setSelected(data.isUnicode());
        octalRadio.setSelected(data.isOctal());
    }

    public void getData(CharacterSwitchingSettings data) {
        data.setSelectedEncoding(unicodeRadio.isSelected()
                ? CharacterSwitchingSettings.Encoding.UNICODE
                : CharacterSwitchingSettings.Encoding.OCTAL);
    }

    public boolean isModified(CharacterSwitchingSettings data) {
        if (unicodeRadio.isSelected() != data.isUnicode()) return true;
        if (octalRadio.isSelected() != data.isOctal()) return true;
        return false;
    }

    public JPanel getRoot() {
        return root;
    }

}
