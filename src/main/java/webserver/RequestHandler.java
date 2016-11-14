package webserver;

import manage.UrlManager;
import model.HttpResponseEnum;
import model.ResponseData;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out)) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Map<String, String> parsingMap = HttpRequestUtils.parseHeader(br);
            if (parsingMap == null || parsingMap.isEmpty()) {
                log.error("[REQUEST HANDLER] Parameter is null.");
                return;
            }

            String url = MapUtils.getString(parsingMap, "Url");
            log.info("[REQUEST HANDLER] Request URL: {}", url);

            ResponseData data = UrlManager.getUrlConnector(url).process(parsingMap);
            HttpResponseEnum httpResponseEnum = HttpResponseEnum.getHttpResponse(data.getResponseStatus());
            httpResponseEnum.response(dos, data);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
