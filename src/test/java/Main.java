import api.ApiServer;

public class Main {
    public static void main(String[] args) {
        ApiServer apiServer = new ApiServer(2333);
        apiServer.register(new ExampleNode()).start();
    }
}
