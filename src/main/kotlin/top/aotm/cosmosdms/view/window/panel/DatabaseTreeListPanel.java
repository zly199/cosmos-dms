package top.aotm.cosmosdms.view.window.panel;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosDatabaseProperties;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.thunderz99.cosmos.Cosmos;
import top.aotm.cosmosdms.dao.DataClientUrlDao;
import com.intellij.ui.treeStructure.Tree;
import top.aotm.cosmosdms.service.CosmosDbOperator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: zenglingyog.zly
 * @version: 1.0
 * @createAt: 2023/12/23
 * @description:
 */
public class DatabaseTreeListPanel extends JPanel {
    private Tree tree;
    private DefaultTreeModel treeModel;

    private DataClientUrlDao dataBaseUrlDao = DataClientUrlDao.singleton;

    private CosmosDbOperator dbOperator = CosmosDbOperator.singleton;

    public DatabaseTreeListPanel() {
        super(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // 创建根节点
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Databases");

        // 添加数据库名称到数据库节点
        List<DefaultMutableTreeNode> lastDbNodes = getLastDbNodes();
        for (DefaultMutableTreeNode dbNode : lastDbNodes) {
            root.add(dbNode);
        }

        // 创建并设置树模型
        treeModel = new DefaultTreeModel(root);
        tree = new Tree(treeModel);

        // 添加树到滚动面板，并将滚动面板添加到当前面板
        JScrollPane scrollPane = new JScrollPane(tree);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshUI() {
        // 删除所有现有节点
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        root.removeAllChildren();

        // 添加新的数据库名称到数据库节点
        List<DefaultMutableTreeNode> dbNodes = getLastDbNodes();
        for (DefaultMutableTreeNode dbNode : dbNodes) {
            root.add(dbNode);
        }

        // 通知模型节点结构已经改变
        treeModel.nodeStructureChanged(root);

        // 强制树进行重新绘制
        tree.repaint();
    }

    private List<DefaultMutableTreeNode> getLastDbNodes() {
        List<DefaultMutableTreeNode> dbNodes = new LinkedList<>();
        for (String dbName : dataBaseUrlDao.getDbClientNames()) {
            DefaultMutableTreeNode clientNode = new DefaultMutableTreeNode(dbName);
            dbNodes.add(clientNode);
            Cosmos cosmos = dbOperator.getDatabaseInstance(dbName);
            CosmosClient dbClient = cosmos.getClientV4();
            List<CosmosDatabaseProperties> list = cosmos.getClientV4().readAllDatabases().stream().toList();
            for (CosmosDatabaseProperties cosmosDatabaseProperties : list) {
                DefaultMutableTreeNode dbNode = new DefaultMutableTreeNode(cosmosDatabaseProperties.getId());
                clientNode.add(dbNode);

                CosmosDatabase database = dbClient.getDatabase(cosmosDatabaseProperties.getId());
                CosmosPagedIterable<CosmosContainerProperties> collections = database.readAllContainers();
                for (CosmosContainerProperties collectionProperties : collections) {

                    DefaultMutableTreeNode collectNode = new DefaultMutableTreeNode(collectionProperties.getId());
                    dbNode.add(collectNode);

                    // 读取所有的partitionKey
                    CosmosContainer container = database.getContainer(collectionProperties.getId());


                    List<String> paths = collectionProperties.getPartitionKeyDefinition().getPaths();
                    String sql = "select DISTINCT c." + paths.get(0).replaceFirst("/", "") + " FROM c";
                    CosmosPagedIterable<JsonNode> queryResults = container.queryItems(sql, new CosmosQueryRequestOptions(), JsonNode.class);

                    Iterator<JsonNode> iterator = queryResults.iterator();
                    int count = 0;
                    while (iterator.hasNext()) {
                        System.out.println(count++);
                        JsonNode partitionKeyValue = iterator.next();
                        if (partitionKeyValue.get("_partition") != null){
                            collectNode.add(new DefaultMutableTreeNode(partitionKeyValue.get("_partition").asText()));
                        }
                    }
                }
            }
        }
        return dbNodes;
    }
}
