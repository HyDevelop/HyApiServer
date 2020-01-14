package api;

public interface ApiNode {
    /**
     * 应返回 Api 节点路径，
     * 例如 http://example.com/hydev/jetty
     * 应返回 /hydev/jetty
     *
     * @return Api 节点路径
     */
    String path();

    /**
     * 处理 Api 调用
     *
     * @param access 客户端请求信息
     * @return 返回给客户端的内容
     */
    String process(ApiAccess access);
}
