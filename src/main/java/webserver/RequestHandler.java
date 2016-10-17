package webserver;

import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String url = null;
            String line = br.readLine();
            while (!"".equals(line)) {
                if (line == null) { break; }
                log.debug("[Request Header] {}", line);

                if (line.startsWith("GET") || line.startsWith("POST")) {
                    Map<String, Object> parsingMap = urlParsing(line);
                    url = MapUtils.getString(parsingMap, "requestPath");
                    log.info("[REQUEST URL] : {}", url);

                    if (url.startsWith("/user/create")) {
                        log.info("create!!!");

                    } else {
                        DataOutputStream dos = new DataOutputStream(out);
                        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                        response200Header(dos, body.length);
                        responseBody(dos, body);
                    }
                }

                line = br.readLine();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, Object> urlParsing(String line) {
        String[] tokens = line.split(" ");

        Map<String, Object> resultMap = Maps.newHashMap();
        int index = tokens[1].indexOf("?");

        if (index == -1) {
            resultMap.put("requestPath", tokens[1]);
        } else {
            String requestPath = tokens[1].substring(0, index);
            resultMap.put("requestPath", requestPath);

            String params = tokens[1].substring(index + 1, tokens[1].length());
            resultMap.put("params", HttpRequestUtils.parseQueryString(params));
        }

        return resultMap;
    }
}
