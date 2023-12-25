package top.aotm.cosmosdms.view.window.panel;

import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;
import top.aotm.cosmosdms.dao.DataClientUrlDao;
import top.aotm.cosmosdms.service.CosmosDbOperator;
import top.aotm.cosmosdms.view.dialog.DatabaseUrlInputDialog;

import javax.swing.*;
import java.awt.*;

import com.intellij.openapi.project.Project;

public class DatabaseToolPanel extends JBPanel{

    DataClientUrlDao dataBaseUrlDao = DataClientUrlDao.singleton;
    CosmosDbOperator cosmosDbOperator = CosmosDbOperator.singleton;

    private DatabaseTreeListPanel myToolWindowContent;
    private JButton myRefreshButton;
    private JButton myNewDatabaseButton;
    private JButton myDeleteDatabaseButton;
    private JButton myModifyDatabaseButton;

    public DatabaseToolPanel(Project project) {
        super(new BorderLayout());

        myToolWindowContent = new DatabaseTreeListPanel(project);

        this.add(getButtonPanel(), BorderLayout.NORTH);
        this.add(myToolWindowContent, BorderLayout.CENTER);

        initActionListeners(project);
    }

    @NotNull
    private JBPanel getButtonPanel() {
        JBPanel buttonPanel = new JBPanel(new FlowLayout(FlowLayout.LEFT));
        //myRefreshButton = new JButton(new ImageIcon(getClass().getResource("/icons/refresh.png")));
        myNewDatabaseButton = new JButton("新建");
        myRefreshButton = new JButton("刷新");
        myDeleteDatabaseButton = new JButton("删除");
        myModifyDatabaseButton = new JButton("修改");

        buttonPanel.add(myNewDatabaseButton);
        buttonPanel.add(myRefreshButton);
        buttonPanel.add(myDeleteDatabaseButton);
        buttonPanel.add(myModifyDatabaseButton);
        return buttonPanel;
    }

    private void initActionListeners(Project project) {
        // 新增
        myNewDatabaseButton.addActionListener(e -> {
            // 弹出输入对话框
            DatabaseUrlInputDialog dialog = new DatabaseUrlInputDialog();
            if (dialog.showAndGet()) {
                String databaseName = dialog.getDatabaseName();
                String databaseUrl = dialog.getDatabaseUrl();
                try {
                    cosmosDbOperator.initInstance(databaseName, databaseUrl);
                    dataBaseUrlDao.addClientUrl(databaseName, databaseUrl);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage().toString(), "数据库连接失败", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            myToolWindowContent.refreshUI();
        });
        //刷新
        myRefreshButton.addActionListener(e -> {
            myToolWindowContent.refreshUI();
        });

    }
}