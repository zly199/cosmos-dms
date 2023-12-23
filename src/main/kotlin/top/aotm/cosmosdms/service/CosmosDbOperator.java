package top.aotm.cosmosdms.service;

import io.github.thunderz99.cosmos.Cosmos;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zenglingyog.zly
 * @version: 1.0
 * @createAt: 2023/12/23
 * @description:
 */
public class CosmosDbOperator {
    public static CosmosDbOperator singleton = new CosmosDbOperator();

    private Map<String,Cosmos> databaseInstances = new HashMap<>();

    public void init(Map<String,String> databaseUrls) {
        for (Map.Entry<String,String> entry : databaseUrls.entrySet()) {
            databaseInstances.put(entry.getKey(),new Cosmos(entry.getValue()));
        }
    }

    public void initInstance(String name,String url) throws Exception{
        databaseInstances.put(name,new Cosmos(url));
    }

    public Cosmos getDatabaseInstance(String name) {
        return databaseInstances.get(name);
    }

}
