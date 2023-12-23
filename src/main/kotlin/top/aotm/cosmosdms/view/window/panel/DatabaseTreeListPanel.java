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
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    public static final String loading = "     [loading...]";


    public DatabaseTreeListPanel() {
        super(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // 创建根节点
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Databases");

        // 添加数据库名称到数据库节点
        List<DefaultMutableTreeNode> lastDbNodes = getLastCollectionNodes();
        for (DefaultMutableTreeNode dbNode : lastDbNodes) {
            root.add(dbNode);
        }

        // 创建并设置树模型
        treeModel = new DefaultTreeModel(root);
        tree = new Tree(treeModel);

        // 添加树到滚动面板，并将滚动面板添加到当前面板
        JScrollPane scrollPane = new JScrollPane(tree);
        this.add(scrollPane, BorderLayout.CENTER);
        asyncLoadPartition(lastDbNodes);
    }



    public void refreshUI() {
        // 删除所有现有节点
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        // 添加新的数据库名称到数据库节点
        List<DefaultMutableTreeNode> dbNodes = getLastCollectionNodes();
        root.removeAllChildren();
        for (DefaultMutableTreeNode dbNode : dbNodes) {
            root.add(dbNode);
        }

        // 通知模型节点结构已经改变
        treeModel.nodeStructureChanged(root);

        // 强制树进行重新绘制
        tree.repaint();

        asyncLoadPartition(dbNodes);
    }

    private void asyncLoadPartition(List<DefaultMutableTreeNode> lastDbNodes) {
        for (DefaultMutableTreeNode clientNode : lastDbNodes) {
            if (clientNode.getChildCount() == 0) {
                continue;
            }

            for (int i = 0; i < clientNode.getChildCount(); i++) {
                if (clientNode.getChildAt(i).getChildCount() == 0) {
                    continue;
                }

                DefaultMutableTreeNode dbNode = (DefaultMutableTreeNode) clientNode.getChildAt(i);
                // 对于每个容器节点，创建并执行一个 SwingWorker
                for (int j = 0; j < dbNode.getChildCount(); j++) {
                    DefaultMutableTreeNode containerNode = (DefaultMutableTreeNode) dbNode.getChildAt(j);
                    SwingWorker<List<DefaultMutableTreeNode>, Void> worker = new SwingWorker<>() {
                        @Override
                        protected List<DefaultMutableTreeNode> doInBackground() throws Exception {
                            String clientName = (String) clientNode.getUserObject();
                            String dbName = (String) dbNode.getUserObject();
                            String collectionName =containerNode.getUserObject().toString().replace(loading,"");

                            Cosmos cosmos = dbOperator.getDatabaseInstance(clientName);
                            CosmosClient dbClient = cosmos.getClientV4();
                            CosmosDatabase database = dbClient.getDatabase(dbName);

                            CosmosContainer container = database.getContainer(collectionName);
                            CosmosContainerProperties collectionProperties = container.read().getProperties();
                            return loadPartion(collectionProperties, container);
                        }

                        @Override
                        protected void done() {
                            try {
                                List<DefaultMutableTreeNode> partitionNodes = get();
                                for (DefaultMutableTreeNode partitionNode : partitionNodes) {
                                    containerNode.add(partitionNode);
                                }
                                String collectionName =containerNode.getUserObject().toString().replace(loading,"");
                                containerNode.setUserObject(collectionName);
                                treeModel.nodeStructureChanged(containerNode);
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    worker.execute();
                }

            }
        }
    }

    private List<DefaultMutableTreeNode> getLastCollectionNodes() {
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
                //排一下序
                List<CosmosContainerProperties> sortedList = collections.stream().sorted(Comparator.comparing(CosmosContainerProperties::getId)).collect(Collectors.toList());
                for (CosmosContainerProperties collectionProperties : sortedList) {
                    String collectionName = collectionProperties.getId()+loading;
                    DefaultMutableTreeNode collectNode = new DefaultMutableTreeNode(collectionName);
                    dbNode.add(collectNode);
                }
            }
        }
        return dbNodes;
    }

    private  List<DefaultMutableTreeNode> loadPartion(CosmosContainerProperties collectionProperties, CosmosContainer container) {
        List<DefaultMutableTreeNode> partitionNodes = new LinkedList<>();
        List<String> paths = collectionProperties.getPartitionKeyDefinition().getPaths();
        String sql = "select DISTINCT c." + paths.get(0).replaceFirst("/", "") + " FROM c";
        CosmosPagedIterable<JsonNode> queryResults = container.queryItems(sql, new CosmosQueryRequestOptions(), JsonNode.class);
        System.out.println("load:"+container.getId());
        Iterator<JsonNode> iterator = queryResults.iterator();
        while (iterator.hasNext()) {
            JsonNode partitionKeyValue = iterator.next();
            if (partitionKeyValue.get("_partition") != null){
                partitionNodes.add(new DefaultMutableTreeNode(partitionKeyValue.get("_partition").asText()));
            }
        }
        return partitionNodes;
    }
}
