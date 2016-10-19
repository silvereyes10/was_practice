package util;

/**
 * @author NAVER
 */
public class HttpResponseHeaderUtils {
    private static final String HTTP_RESPONSE_HEADER_200 = "HTTP/1.1 200 OK\r\n";
    private static final String HTTP_RESPONSE_HEADER_302 = "HTTP/1.1 302 Found\r\n";

    private static final String HTTP_RESPONSE_HEADER_CONTENT_TYPE_HTML = "Content-Type: text/html; charset=utf-8\r\n";
    private static final String HTTP_RESPONSE_HEADER_CONTENT_TYPE_CSS = "Content-Type: text/css; charset=utf-8\r\n";

    private static final String HTTP_RESPONSE_HEADER_SET_COOKIES_LOGIN = "Set-Cookies: logined=true\r\n";

    private static final String HTTP_RESPONSE_HEADER_CONTENT_LENGTH_FORMAT = "Content-Length: %d\r\n";
    private static final String HTTP_RESPONSE_HEADER_REDIRECTION_LOCATION_FORMAT = "Location: %s\r\n";

    private static final String HTTP_RESPONSE_HEADER_END = "\r\n";

    private static StringBuilder responseBuilder = new StringBuilder();

    public static String getHttpResponseHeader200(String url, int contentLength) {
        responseBuilder.append(HTTP_RESPONSE_HEADER_200);

        if (url.endsWith(".css")) {
            responseBuilder.append(HTTP_RESPONSE_HEADER_CONTENT_TYPE_CSS);
        } else {
            responseBuilder.append(HTTP_RESPONSE_HEADER_CONTENT_TYPE_HTML);
        }

        responseBuilder.append(String.format(HTTP_RESPONSE_HEADER_CONTENT_LENGTH_FORMAT, contentLength));
        responseBuilder.append(HTTP_RESPONSE_HEADER_END);

        return getResponseHeader();
    }

    public static String getHttpResponseHeader302(String redirectionLocation, boolean isLogin) {
        responseBuilder.append(HTTP_RESPONSE_HEADER_302);
        responseBuilder.append(String.format(HTTP_RESPONSE_HEADER_REDIRECTION_LOCATION_FORMAT, redirectionLocation));

        if (isLogin) {
            responseBuilder.append(HTTP_RESPONSE_HEADER_SET_COOKIES_LOGIN);
        }

        responseBuilder.append(HTTP_RESPONSE_HEADER_END);

        return getResponseHeader();
    }

    /**
     * StringBuilder를 초기화 하면서, 작성한 Response Header를 반환하기 위한 함수
     * @return 작성한 Response Header
     */
    private static String getResponseHeader() {
        String responseHeader = responseBuilder.toString();
        responseBuilder = new StringBuilder(); // StringBuilder 초기화
        return responseHeader;
    }
}
