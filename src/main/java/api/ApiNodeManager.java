package api;

import java.util.ArrayList;
import java.util.Arrays;

class ApiNodeManager {
    private final ArrayList<ApiNode> registeredNodes = new ArrayList<>();

    /**
     * 注册 Api 节点
     *
     * @param nodes Api 节点
     */
    public void register(ApiNode... nodes) {
        registeredNodes.addAll(Arrays.asList(nodes));
    }

    /**
     * 获取 Api 节点
     *
     * @param nodePath Api 节点名
     * @return 匹配到的 Api 节点 或 null
     * @see ApiNode#path()
     */
    public ApiNode getNode(String nodePath) {
        for (ApiNode node : registeredNodes) {
            if (node.path().equalsIgnoreCase(nodePath)) {
                return node;
            }
        }

        return null;
    }
}
