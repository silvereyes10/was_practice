package util;

import constant.HttpConstants;
import model.ResponseData;

import java.util.Map;
import java.util.Set;

/**
 * @author NAVER
 */
public class HttpResponseUtils {
    private static final String HTTP_RESPONSE_HEADER_200 = "HTTP/1.1 200 OK \r\n";
    private static final String HTTP_RESPONSE_HEADER_302 = "HTTP/1.1 302 Found \r\n";

    private static final String HTTP_RESPONSE_HEADER_CONTENT_TYPE_HTML = "Content-Type: text/html; charset=utf-8 \r\n";
    private static final String HTTP_RESPONSE_HEADER_CONTENT_TYPE_CSS = "Content-Type: text/css; charset=utf-8 \r\n";
    private static final String HTTP_RESPONSE_HEADER_CONTENT_TYPE_JS = "Content-Type: application/javascript \r\n";

    private static final String HTTP_RESPONSE_HEADER_CONTENT_LENGTH_KEY = "Content-Length: ";
    private static final String HTTP_RESPONSE_HEADER_REDIRECTION_LOCATION_KEY = "Location: ";
    private static final String HTTP_RESPONSE_HEADER_SET_COOKIES_KEY = "Set-Cookie: ";

    private static final String HTTP_RESPONSE_HEADER_END = "\r\n";

    private static StringBuilder responseBuilder = new StringBuilder();

    public static String getHttpResponseHeader(ResponseData data) {
        switch (data.getResponseStatus()) {
            case HttpConstants.HTTP_STATUS_200:
                return getHttpResponseHeader200(data);
            case HttpConstants.HTTP_STATUS_302:
                return getHttpResponseHeader302(data);
        }

        return getResponseHeader();
    }

    private static String getHttpResponseHeader200(ResponseData data) {
        responseBuilder.append(HTTP_RESPONSE_HEADER_200);

        String url = data.getRequestUrl();

        if (url.endsWith(".css")) {
            responseBuilder.append(HTTP_RESPONSE_HEADER_CONTENT_TYPE_CSS);
        } else if (url.endsWith(".js")) {
            responseBuilder.append(HTTP_RESPONSE_HEADER_CONTENT_TYPE_JS);
        } else {
            responseBuilder.append(HTTP_RESPONSE_HEADER_CONTENT_TYPE_HTML);
        }

        responseBuilder.append(String.format(HTTP_RESPONSE_HEADER_CONTENT_LENGTH_KEY + "%d" + HTTP_RESPONSE_HEADER_END,
                data.getContentLength()));
        responseBuilder.append(HTTP_RESPONSE_HEADER_END);

        return getResponseHeader();
    }

    private static String getHttpResponseHeader302(ResponseData data) {
        responseBuilder.append(HTTP_RESPONSE_HEADER_302);
        responseBuilder.append(HTTP_RESPONSE_HEADER_REDIRECTION_LOCATION_KEY + data.getRedirectionUrl()
                + HTTP_RESPONSE_HEADER_END);

        if (!data.getHeaderMap().isEmpty()) {
            addResponseHeaderCookie(data.getHeaderMap());
        }

        responseBuilder.append(HTTP_RESPONSE_HEADER_END);

        return getResponseHeader();
    }

    private static void addResponseHeaderCookie(Map<String, String> headerMap) {
        responseBuilder.append(HTTP_RESPONSE_HEADER_SET_COOKIES_KEY);

        Set<String> keySet = headerMap.keySet();
        for (String key : keySet) {
            responseBuilder.append(key + "=" + headerMap.get(key));
        }

        responseBuilder.append(HTTP_RESPONSE_HEADER_END);
    }

    /**
     * StringBuilder를 초기화 하면서, 작성한 Response Header를 반환하기 위한 함수
     *
     * @return 작성한 Response Header
     */
    private static String getResponseHeader() {
        String responseHeader = responseBuilder.toString();
        responseBuilder = new StringBuilder(); // StringBuilder 초기화
        return responseHeader;
    }
}
