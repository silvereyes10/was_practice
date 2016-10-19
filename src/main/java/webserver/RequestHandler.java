package webserver;

import com.google.common.collect.Maps;
import db.DataBase;
import model.HttpResponse;
import model.ResponseData;
import model.User;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.MemberUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); DataOutputStream dos = new DataOutputStream(out)) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Map<String, String> parsingMap = headerParsing(br);
            if (parsingMap == null || parsingMap.isEmpty()) {
                log.error("[REQUEST HANDLER] Parameter is null.");
                return;
            }

            ResponseData data = new ResponseData();

            String url = MapUtils.getString(parsingMap, "Url");
            data.setRequestUrl(url);
            log.info("[REQUEST HANDLER] Request URL: {}", url);

            if ("/user/create".startsWith(url)) {
                User user = MemberUtils.createUserObject(parsingMap);
                DataBase.addUser(user);

                data.setRedirectionUrl("/index.html");
                HttpResponse.STATUS_302.response(dos, data);
            } else if ("/user/login".startsWith(url)) {
                User user = DataBase.findUserById(MapUtils.getString(parsingMap, "userId"));
                if (user != null && user.getPassword().equals(MapUtils.getString(parsingMap, "password"))) {
                    data.setLogin(true);
                    data.setRedirectionUrl("/index.html");
                } else {
                    data.setRedirectionUrl("/user/login_failed.html");
                }

                HttpResponse.STATUS_302.response(dos, data);
            } else if ("/user/list".startsWith(url)) {
                String cookie = parsingMap.get("Cookie");
                log.info("Cookie : {}", parsingMap.get("Cookie"));
                if (BooleanUtils.isFalse(isLogined(cookie))) {
                    data.setRedirectionUrl("/user/login.html");
                    HttpResponse.STATUS_302.response(dos, data);
                    return;
                }

                // TODO 회원 목록 출력
                data.setResponseBody(this.getUserList());
                HttpResponse.STATUS_200.response(dos, data);
            }
            else {
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                data.setResponseBody(new String(body));
                HttpResponse.STATUS_200.response(dos, data);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isLogined(String cookie) {
        if (StringUtils.isBlank(cookie)) {
            return false;
        }

        boolean isLogined = MapUtils.getBooleanValue(HttpRequestUtils.parseCookies(cookie), "logined");
        if (BooleanUtils.isFalse(isLogined)) {
            return false;
        }

        return true;
    }

    private String getUserList() {
        String tdStringFormat = "<td>%s</td>";
        StringBuilder userListBuilder = new StringBuilder();

        Collection<User> users = DataBase.findAll();
        userListBuilder.append("<table border='1'>");
        for (User user : users) {
            userListBuilder.append("<tr>");
            userListBuilder.append(String.format(tdStringFormat, user.getUserId()));
            userListBuilder.append(String.format(tdStringFormat, user.getName()));
            userListBuilder.append(String.format(tdStringFormat, user.getEmail()));
            userListBuilder.append("</tr>");
        }
        userListBuilder.append("</table>");

        return userListBuilder.toString();
    }

    private Map<String, String> headerParsing(BufferedReader br) throws IOException {
        Map<String, String> parsingMap = Maps.newHashMap();

        String line = br.readLine();
        String[] urlTokens = line.split(" ");

        String method = urlTokens[0];
        parsingMap.put("Method", method);
        parsingMap.put("Url", urlTokens[1]);
        parsingMap.put("Protocol", urlTokens[2]);

        line = br.readLine();
        while (!line.equals("")) {
            if (line.contains("Cookie")) {
                log.info("[HEADER] {}", line);
            }
            String[] headerToken = line.split(":");
            parsingMap.put(headerToken[0].trim(), headerToken[1].trim());
            line = br.readLine();
        }

        if (HttpRequestUtils.HTTP_METHOD_GET.equals(method)) {
            parsingMap.putAll(makeParamMapForGet(parsingMap));
        } else if (HttpRequestUtils.HTTP_METHOD_POST.equals(method)) {
            parsingMap.putAll(makeParamMapForPost(parsingMap, br));
        }

        return parsingMap;
    }

    private Map<String, String> makeParamMapForGet(Map<String, String> parsingMap) {
        String url = parsingMap.get("Url");

        int index = url.indexOf("?");
        if (index == -1) {
            parsingMap.put("Url", url);
            return parsingMap;
        }

        String requestPath = url.substring(0, index);

        parsingMap.put("Url", requestPath);
        parsingMap.putAll(HttpRequestUtils.parseQueryString(url.substring(index + 1, url.length())));

        return parsingMap;
    }

    private Map<String, String> makeParamMapForPost(Map<String, String> parsingMap, BufferedReader br) throws IOException {
        int contentLength = MapUtils.getIntValue(parsingMap, "Content-Length");

        parsingMap.putAll(HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength)));
        return parsingMap;
    }
}
