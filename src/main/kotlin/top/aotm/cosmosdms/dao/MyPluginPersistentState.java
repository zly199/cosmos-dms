package top.aotm.cosmosdms.dao;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(
    name = "com.example.MyPluginPersistentState",
    storages = {@Storage("MyPluginDatabaseUrls.xml")}
)
public class MyPluginPersistentState implements PersistentStateComponent<MyPluginPersistentState> {
    public List<String> databaseUrls = new ArrayList<>();

    @Nullable
    @Override
    public MyPluginPersistentState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MyPluginPersistentState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // Helper methods to modify the list of URLs
    public void addDatabaseUrl(String url) {
        if (!databaseUrls.contains(url)) {
            databaseUrls.add(url);
        }
    }

    public List<String> getDatabaseUrls() {
        return databaseUrls;
    }
}
