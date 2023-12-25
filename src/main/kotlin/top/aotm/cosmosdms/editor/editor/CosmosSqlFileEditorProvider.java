package top.aotm.cosmosdms.editor.editor;

import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import top.aotm.cosmosdms.editor.type.CosmosSqlType;

/**
 * @author: zenglingyog.zly
 * @version: 1.0
 * @createAt: 2023/12/25
 * @description:
 */
public class CosmosSqlFileEditorProvider implements FileEditorProvider {
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file.getFileType() instanceof CosmosSqlType;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return new CosmosSqlFileEditor(file);
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "cosmsql-editor";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }
}