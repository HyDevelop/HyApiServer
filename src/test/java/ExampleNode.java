import api.ApiAccess;
import api.ApiNode;

public class ExampleNode implements ApiNode {
    @Override
    public String path() {
        return "/example";
    }

    @Override
    public String process(ApiAccess apiAccess) {
        return "Hello World! ";
    }
}
