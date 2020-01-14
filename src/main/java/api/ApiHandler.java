package api;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

@Setter
@Getter
class ApiHandler extends AbstractHandler {
    private final ApiNodeManager manager = new ApiNodeManager();
    /**
     * 默认的帮助信息和错误信息，你也可以自己设置
     *
     * @see ApiServer#setHelpUsage(String)
     * @see ApiServer#setWrongMethod(String)
     */
    private String helpUsage = "Help Usage";
    private String wrongMethod = "{\"error\" : \"Error method\"}";

    private static void writeResponse(HttpServletResponse response, String content) {
        writeResponse(response, content, "application/json; charset=utf-8");
    }

    private static void writeResponse(HttpServletResponse response, String content, String contentType) {

        response.setContentType(contentType);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);

        try {
            response.getWriter().println(content);
        } catch (IOException e) {
            throw new RuntimeException("Response failed to write", e);
        }
    }

    /**
     * HttpServletRequest 转 Map<String, String>，方便用户使用
     */
    private Map<String, String> mapHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> keysEnumeration = request.getHeaderNames();

        while (keysEnumeration.hasMoreElements()) {
            String key = keysEnumeration.nextElement();
            headers.put(key, request.getHeader(key));
        }

        return headers;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        request.setHandled(true);
        ApiNode node = manager.getNode(s);

        // 不是 get 或 post 方法时，输出错误消息
        if (!request.getMethod().equalsIgnoreCase("get") && !request.getMethod().equalsIgnoreCase("post")) {
            writeResponse(httpServletResponse, wrongMethod);
        }

        if (s == null || s.isEmpty() || node == null) {
            writeResponse(httpServletResponse, helpUsage, "text/html");
            return;
        }

        String content = request.getReader().lines().collect(joining(lineSeparator()));
        Map<String, String> headers = mapHeaders(request);
        writeResponse(httpServletResponse, node.process(new ApiAccess(request, headers, content)));
    }
}
