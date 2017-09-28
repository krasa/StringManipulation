package osmedile.intellij.stringmanip;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;
import osmedile.intellij.stringmanip.align.ColumnAlignerModel;

@State(name = "StringManipulationState", storages = {@Storage(id = "StringManipulationState", file = "$APP_CONFIG$/stringManipulation.xml")})
public class PluginPersistentStateComponent implements PersistentStateComponent<PluginPersistentStateComponent> {

    private ColumnAlignerModel columnAlignerModel = new ColumnAlignerModel();


    public ColumnAlignerModel getColumnAlignerModel() {
        return columnAlignerModel;
    }

    public void setColumnAlignerModel(ColumnAlignerModel columnAlignerModel) {
        this.columnAlignerModel = columnAlignerModel;
    }

    public static PluginPersistentStateComponent getInstance() {
        return ServiceManager.getService(PluginPersistentStateComponent.class);
    }

    @Nullable
    @Override
    public PluginPersistentStateComponent getState() {
        return this;
    }

    @Override
    public void loadState(PluginPersistentStateComponent o) {
        XmlSerializerUtil.copyBean(o, this);

    }

}
