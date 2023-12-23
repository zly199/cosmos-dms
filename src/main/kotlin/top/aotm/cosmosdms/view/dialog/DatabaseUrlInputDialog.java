package top.aotm.cosmosdms.view.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import javax.swing.*;
import java.awt.*;

public class DatabaseUrlInputDialog extends DialogWrapper {
    private JBTextField databaseNameField;
    private JBTextField databaseUrlField;

    public DatabaseUrlInputDialog() {
        super(true); // use current window as parent
        setTitle("Add Database");
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        inputPanel.add(new JLabel("Enter the database name:"));
        databaseNameField = new JBTextField();
        inputPanel.add(databaseNameField);

        inputPanel.add(new JLabel("Enter the database URL:"));
        databaseUrlField = new JBTextField();
        inputPanel.add(databaseUrlField);

        dialogPanel.add(inputPanel, BorderLayout.CENTER);
        return dialogPanel;
    }

    public String getDatabaseName() {
        return databaseNameField.getText();
    }

    public String getDatabaseUrl() {
        return databaseUrlField.getText();
    }
}
