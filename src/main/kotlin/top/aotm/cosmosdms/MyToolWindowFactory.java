package top.aotm.cosmosdms;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import top.aotm.cosmosdms.dao.MyPluginPersistentState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MyToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MyDatabaseToolWindow myDatabaseToolWindow = new MyDatabaseToolWindow(project);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(myDatabaseToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public static class MyDatabaseToolWindow {
        private JBPanel myToolWindowContent;
        private JBList<String> myDatabaseList;
        private JButton myRefreshButton;
        private JButton myNewDatabaseButton;
        private JButton myDeleteDatabaseButton;
        private JButton myModifyDatabaseButton;

        public MyDatabaseToolWindow(Project project) {
            myToolWindowContent = new JBPanel(new BorderLayout());
            JBPanel buttonPanel = new JBPanel(new FlowLayout(FlowLayout.LEFT));

            //myRefreshButton = new JButton(new ImageIcon(getClass().getResource("/icons/refresh.png")));
            myRefreshButton = new JButton("刷新");
            myNewDatabaseButton = new JButton("新建");
            myDeleteDatabaseButton = new JButton("删除");
            myModifyDatabaseButton = new JButton("修改");

            buttonPanel.add(myRefreshButton);
            buttonPanel.add(myNewDatabaseButton);
            buttonPanel.add(myDeleteDatabaseButton);
            buttonPanel.add(myModifyDatabaseButton);

            myDatabaseList = new JBList<>();
            // Populate the list with database names
            MyPluginPersistentState state = ServiceManager.getService(MyPluginPersistentState.class);

            myDatabaseList.setListData(state.databaseUrls.toArray(new String[0]));

            myToolWindowContent.add(buttonPanel, BorderLayout.NORTH);
            myToolWindowContent.add(new JScrollPane(myDatabaseList), BorderLayout.CENTER);

            // 按钮点击事件监听器
            myNewDatabaseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 弹出输入对话框
                    String databaseUrl = Messages.showInputDialog(
                            new JPanel(new BorderLayout()),
                            "Enter the database URL:",
                            "Add Database",
                            Messages.getQuestionIcon()
                    );

                    // 处理用户输入
                    if (databaseUrl != null && !databaseUrl.trim().isEmpty()) {
                        // Add the database to the list
                        state.addDatabaseUrl(databaseUrl);
                        myDatabaseList.setListData(state.databaseUrls.toArray(new String[0]));
                    }
                }
            });
        }

        public JComponent getContent() {
            return myToolWindowContent;
        }

        // Methods for button actions and other functionalities
        // ...
    }
}
