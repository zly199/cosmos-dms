package top.aotm.cosmosdms.editor.editor;

import com.intellij.openapi.fileEditor.*;
import javax.swing.*;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.beans.PropertyChangeListener;

/**
 * @author: zenglingyog.zly
 * @version: 1.0
 * @createAt: 2023/12/25
 * @description:
 */
public class CosmosSqlFileEditor extends UserDataHolderBase implements FileEditor {
    private JTextArea textArea;
    private JPanel panel;

    private VirtualFile file;

    public CosmosSqlFileEditor(VirtualFile file) {
        this.file = file;  // 在构造函数中初始化 VirtualFile
        textArea = new JTextArea();
        panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return textArea;
    }

    @NotNull
    @Override
    public String getName() {
        return "CosmSql Editor";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        // 用于恢复编辑器的状态（例如，滚动位置）
        System.out.println("setState");
    }

    @Nullable
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        // 保存编辑器的当前状态
        return FileEditorState.INSTANCE;
    }

    @Override
    public boolean isModified() {
        // 返回编辑器内容是否已被修改
        return false;
    }

    @Override
    public boolean isValid() {
        // 返回编辑器是否有效（例如，文件是否仍然存在）
        return true;
    }

    @Override
    public void selectNotify() {
        // 当文件被选中时调用
        System.out.println("selectNotify");
    }

    @Override
    public void deselectNotify() {
        // 当文件不再被选中时调用
        System.out.println("deselectNotify");
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // 添加属性更改监听器（可用于监听各种事件）
        System.out.println("addPropertyChangeListener");
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // 移除属性更改监听器
        System.out.println("removePropertyChangeListener");
    }

    @Nullable
    @Override
    public VirtualFile getFile() {
        System.out.println("getFile");
        // 返回当前编辑器正在编辑的文件
        return file;
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
        // 清理资源，例如关闭文件流
    }
}