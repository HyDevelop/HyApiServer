## JettyServer
这是一个 轻度封装，仅仅满足了基本需求的 Jetty 微服务器

### 使用方法
1. 新建节点类，实现两个方法（参见 [ApiNode](https://github.com/HyDevelop/JettyServer/blob/master/src/main/java/api/ApiNode.java)）:  

`````Java
import api.ApiAccess;
import api.ApiNode;

public class ExampleNode implements ApiNode {
    @Override
    public String path() {
        // 服务器节点
        return "/example";
    }

    @Override
    public String process(ApiAccess apiAccess) {
        // 处理后返回的内容，ApiAccess 中有请求头、请求体等信息
        return "Hello World! ";
    }
}
`````

2. 开启服务器

`````Java
import api.ApiServer;

public class Main {
    public static void main(String[] args) {
        // 设置服务器端口
        ApiServer apiServer = new ApiServer(2333);
        
        // 添加节点并开启服务器
        apiServer.register(new ExampleNode()).start();
    }
}
`````

#### 开发组群号：498386389
