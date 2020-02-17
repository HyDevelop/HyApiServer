package api;

import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.server.Server;

@RequiredArgsConstructor
public class ApiServer {
    private final int port;
    private ApiHandler handler = new ApiHandler();

    private Server jetty;

    /**
     * 开启服务器，端口即构造方法的参数
     */
    public void start() {
        startAsync();

        // 同步线程
        try {
            jetty.join();
        }
        catch (InterruptedException e) {
            System.err.println("Thread interrupted.");
            e.printStackTrace();
        }
    }

    public void startAsync()
    {
        jetty = new Server(port);
        jetty.setHandler(handler);

        try {
            jetty.start();
        } catch (Exception e) {
            throw new RuntimeException("Server start error", e);
        }
    }

    /**
     * 注册 Api 节点
     *
     * @param apiNode 需要注册的 Api 节点
     */
    public ApiServer register(ApiNode... apiNode) {
        handler.getManager().register(apiNode);
        return this;
    }

    public ApiServer setHelpUsage(String helpUsage) {
        handler.setHelpUsage(helpUsage + "\n");
        return this;
    }

    public ApiServer setWrongMethod(String wrongMethod) {
        handler.setWrongMethod(wrongMethod + "\n");
        return this;
    }
}
