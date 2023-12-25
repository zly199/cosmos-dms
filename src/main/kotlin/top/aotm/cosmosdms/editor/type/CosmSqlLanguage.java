package top.aotm.cosmosdms.editor.type;

import com.intellij.lang.Language;

public class CosmSqlLanguage extends Language {
    public static final CosmSqlLanguage INSTANCE = new CosmSqlLanguage();

    private CosmSqlLanguage() {
        super("CosmSql");
    }
}
