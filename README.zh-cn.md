## HyApiServer [![](https://jitpack.io/v/HyDevelop/JettyServer.svg)](https://jitpack.io/#HyDevelop/JettyServer)  
这是一个轻度封装, 仅仅满足了基本需求的 [Jetty](https://www.eclipse.org/jetty/) 微服务 API 服务器框架.<br>
框架和依赖一共 ≈600kb, 而且可以不用装 Tomcat 那样的容器直接打包成 Jar 运行w!<br>

### 使用方法  
1. 导入  

添加 [JitPack](https://jitpack.io/) 仓库  
`````xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
`````

添加本仓库  
`````xml
<dependency>
    <groupId>com.github.HyDevelop</groupId>
    <artifactId>HyApiServer</artifactId>
    <version>1.1.0.8</version>
</dependency>
`````

2. 新建节点类，实现两个方法（参见 [ApiNode](https://github.com/HyDevelop/JettyServer/blob/master/src/main/java/api/ApiNode.java)）:  

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

3. 开启服务器  

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
