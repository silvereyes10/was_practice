package util;

/**
 * @author NAVER
 */
public interface HttpResponseConstants {
    String HTTP_200_RESPONSE_FORMAT =
            "HTTP/1.1 200 OK \r\n" +
                    "Content-Type: text/html; charset=utf-8 \r\n" +
                    "Content-Length: %d \r\n" +
                    "\r\n";

    String HTTP_302_RESPONSE_FORMAT =
            "HTTP/1.1 302 Found \r\n" +
                    "Location: %s \r\n" +
                    "\r\n";
}
