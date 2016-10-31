package bo;

import db.DataBase;
import model.HttpResponse;
import model.ResponseData;
import model.User;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import util.HttpRequestUtils;
import util.MemberUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author NAVER
 */
public class UserManageBOImpl implements UserManageBO {
    private DefaultResourceManageBO defaultResourceManageBO;

    public UserManageBOImpl() {
        this.defaultResourceManageBO = new DefaultResourceManageBOImpl();
    }

    @Override
    public ResponseData process(String url, Map<String, String> parsingMap) throws IOException {
        ResponseData data = new ResponseData(url);

        if (url.endsWith(".html")) {
            return defaultResourceManageBO.process(url, parsingMap);
        } else if (url.contains("/create")) {
            return create(parsingMap, data);
        } else if (url.contains("/login")) {
            return login(parsingMap, data);
        } else if (url.contains("/list")) {
            return list(parsingMap, data);
        }

        return data;
    }

    private ResponseData list(Map<String, String> parsingMap, ResponseData data) throws IOException {
        String cookie = parsingMap.get("Cookie");
        if (BooleanUtils.isFalse(isLogin(cookie))) {
            data.setResponseStatus(HttpResponse.STATUS_302.name());
            data.setRedirectionUrl("/user/login.html");
            return data;
        }

        data.setResponseBody(this.getUserList());
        data.setResponseStatus(HttpResponse.STATUS_200.name());
        return data;
    }

    private boolean isLogin(String cookie) {
        return BooleanUtils.isFalse(StringUtils.isBlank(cookie))
                && MapUtils.getBooleanValue(HttpRequestUtils.parseCookies(cookie), "logined");
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

    private ResponseData login(Map<String, String> parsingMap, ResponseData data) throws IOException {
        User user = DataBase.findUserById(MapUtils.getString(parsingMap, "userId"));
        if (user != null && user.getPassword().equals(MapUtils.getString(parsingMap, "password"))) {
            data.setLogin(true);
            data.setRedirectionUrl("/index.html");
        } else {
            data.setRedirectionUrl("/user/login_failed.html");
        }

        data.setResponseStatus(HttpResponse.STATUS_302.name());
        return data;
    }

    private ResponseData create(Map<String, String> parsingMap, ResponseData data) throws IOException {
        User user = MemberUtils.createUserObject(parsingMap);
        DataBase.addUser(user);

        data.setRedirectionUrl("/index.html");
        data.setResponseStatus(HttpResponse.STATUS_302.name());
        return data;
    }
}
