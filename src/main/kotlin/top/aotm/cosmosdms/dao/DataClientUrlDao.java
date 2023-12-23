package top.aotm.cosmosdms.dao;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@State(
    name = "top.aotm.cosmosdms.dao.DataClientUrlDao",
    storages = {@Storage("DataClientUrlDao.xml")}
)
public class DataClientUrlDao implements PersistentStateComponent<DataClientUrlDao> {

    // Populate the list with database names
    public static DataClientUrlDao singleton = ServiceManager.getService(DataClientUrlDao.class);
    /**
     * name->url
     */
    private final Map<String,String> dbClientUrls = new HashMap<>();

    @Nullable
    @Override
    public DataClientUrlDao getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DataClientUrlDao state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // Helper methods to modify the list of URLs
    public void addClientUrl(String name, String url) {
        dbClientUrls.put(name,url);
    }

    public Set<String> getDbClientNames() {
        return getState().dbClientUrls.keySet();
    }


    public String geClientUrl(String name) {
        return getState().dbClientUrls.get(name);
    }
}
