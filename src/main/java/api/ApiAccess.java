package api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 调用 Api 时的一些信息，可以选择使用
 */
@Getter
@AllArgsConstructor
public class ApiAccess {
    /**
     * Jetty 提供的请求信息
     */
    private final HttpServletRequest request;
    /**
     * 请求头
     */
    private final Map<String, String> headers;
    /**
     * 请求内容
     */
    private final String content;
}
