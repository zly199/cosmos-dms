package top.aotm.cosmosdms.view.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import top.aotm.cosmosdms.view.window.panel.DatabaseToolPanel;



public class SideToolWindow implements com.intellij.openapi.wm.ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.getInstance();

        Content content = contentFactory.createContent(new DatabaseToolPanel(project), "", false);

        toolWindow.getContentManager().addContent(content);
    }


}
