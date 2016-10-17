package webserver;

import com.google.common.collect.Maps;
import db.DataBase;
import model.User;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.MemberUtils;

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
            Map<String, String> parsingMap = headerParsing(br);

            String method = MapUtils.getString(parsingMap, "method");
            String url = MapUtils.getString(parsingMap, "requestPath");

            if ("/user/create".startsWith(url)) {
                log.info("[REQUEST HANDLER] Method : {}", method);
                if (parsingMap == null || parsingMap.isEmpty()) {
                    log.error("[REQUEST HANDLER] Parameter is null. Member create process is fail.");
                }

                User user = MemberUtils.createUserObject(parsingMap);
                DataBase.addUser(user);
            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
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

    private Map<String, String> headerParsing(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] urlTokens = line.split(" ");
        String method = urlTokens[0];

        if (HttpRequestUtils.HTTP_METHOD_GET.equals(method)) {
            return makeParamMapForGet(urlTokens[1]);
        } else if (HttpRequestUtils.HTTP_METHOD_POST.equals(method)) {
            return makeParamMapForPost(urlTokens[1], br);
        } else {
            return Maps.newHashMap();
        }
    }

    private Map<String, String> makeParamMapForGet(String data) {
        Map<String, String> parsingMap = Maps.newHashMap();

        int index = data.indexOf("?");
        if (index == -1) {
            parsingMap.put("requestPath", data);
            return parsingMap;
        }

        String requestPath = data.substring(0, index);

        parsingMap.put("method", HttpRequestUtils.HTTP_METHOD_GET);
        parsingMap.put("requestPath", requestPath);
        parsingMap.putAll(HttpRequestUtils.parseQueryString(data.substring(index + 1, data.length())));

        return parsingMap;
    }

    private Map<String, String> makeParamMapForPost(String data, BufferedReader br) throws IOException {
        Map<String, String> parsingMap = Maps.newHashMap();
        int contentLength = 0;

        parsingMap.put("method", HttpRequestUtils.HTTP_METHOD_POST);
        parsingMap.put("requestPath", data);

        String line = br.readLine();
        while (!line.equals("")) {
            if (line.contains("Content-Length")) {
                String[] contentLengthTokens = line.split(":");
                contentLength = Integer.parseInt(contentLengthTokens[1].trim());
            }
            line = br.readLine();
        }

        parsingMap.putAll(HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength)));
        return parsingMap;
    }
}
