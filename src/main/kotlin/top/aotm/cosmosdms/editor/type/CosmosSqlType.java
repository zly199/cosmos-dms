package top.aotm.cosmosdms.editor.type;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author: zenglingyog.zly
 * @version: 1.0
 * @createAt: 2023/12/25
 * @description:
 */
public class CosmosSqlType extends LanguageFileType {
    public static final CosmosSqlType INSTANCE = new CosmosSqlType();

    private CosmosSqlType() {
        super(CosmSqlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "CosmSql";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "CosmSql file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "cosmsql";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Json;
    }
}
