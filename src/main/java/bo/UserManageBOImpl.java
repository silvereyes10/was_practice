package bo;

import db.DataBase;
import model.HttpResponseEnum;
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
public class UserManageBOImpl {
    public ResponseData list(Map<String, String> parsingMap) throws IOException {
        ResponseData data = new ResponseData(MapUtils.getString(parsingMap, "Url"));
        String cookie = parsingMap.get("Cookie");
        if (BooleanUtils.isFalse(isLogin(cookie))) {
            data.setResponseStatus(HttpResponseEnum.STATUS_302.name());
            data.setRedirectionUrl("/user/login.html");
            return data;
        }

        data.setResponseBody(this.getUserList());
        data.setResponseStatus(HttpResponseEnum.STATUS_200.name());
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

    public ResponseData login(Map<String, String> parsingMap) throws IOException {
        ResponseData data = new ResponseData(MapUtils.getString(parsingMap, "Url"));
        User user = DataBase.findUserById(MapUtils.getString(parsingMap, "userId"));
        if (user != null && user.getPassword().equals(MapUtils.getString(parsingMap, "password"))) {
            data.setLogin(true);
            data.setRedirectionUrl("/index.html");
        } else {
            data.setRedirectionUrl("/user/login_failed.html");
        }

        data.setResponseStatus(HttpResponseEnum.STATUS_302.name());
        return data;
    }

    public ResponseData create(Map<String, String> parsingMap) throws IOException {
        ResponseData data = new ResponseData(MapUtils.getString(parsingMap, "Url"));
        User user = MemberUtils.createUserObject(parsingMap);
        DataBase.addUser(user);

        data.setRedirectionUrl("/index.html");
        data.setResponseStatus(HttpResponseEnum.STATUS_302.name());
        return data;
    }
}
