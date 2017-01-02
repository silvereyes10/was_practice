package bo;

import constant.HttpConstants;
import db.DataBase;
import model.HttpRequest;
import model.ResponseData;
import model.User;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import util.HttpRequestUtils;
import util.MemberUtils;

import java.util.Collection;

/**
 * @author NAVER
 */
public class UserManageBOImpl implements UserManageBO {
    @Override
    public ResponseData list(HttpRequest request) {
        ResponseData data = new ResponseData(request.getPath());
        String cookie = request.getHeader("Cookie");
        if (BooleanUtils.isFalse(isLogin(cookie))) {
            data.setResponseStatus(HttpConstants.HTTP_STATUS_302);
            data.setRedirectionUrl("/user/login.html");
            return data;
        }
        String responseBody = getUserList();
        data.setResponseBody(responseBody);
        data.setContentLength(responseBody.getBytes().length);
        data.setResponseStatus(HttpConstants.HTTP_STATUS_200);
        return data;
    }

    private boolean isLogin(String cookie) {
        return BooleanUtils.isFalse(StringUtils.isBlank(cookie))
                && MapUtils.getBooleanValue(HttpRequestUtils.parseCookies(cookie), "Logined");
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

    @Override
    public ResponseData login(HttpRequest request) {
        ResponseData data = new ResponseData(request.getPath());
        User user = DataBase.findUserById(request.getParameterValue("userId"));
        if (user != null && user.getPassword().equals(request.getParameterValue("password"))) {
            data.addHeader("Logined", "true");
            data.setRedirectionUrl("/index.html");
        } else {
            data.setRedirectionUrl("/user/login_failed.html");
        }

        data.setResponseStatus(HttpConstants.HTTP_STATUS_302);
        return data;
    }

    @Override
    public ResponseData create(HttpRequest request) {
        ResponseData data = new ResponseData(request.getPath());
        User user = MemberUtils.createUserObject(request.getParameter());
        DataBase.addUser(user);

        data.setRedirectionUrl("/index.html");
        data.setResponseStatus(HttpConstants.HTTP_STATUS_302);
        return data;
    }
}
