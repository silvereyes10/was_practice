package controller;

import bo.UserManageBO;
import model.HttpRequest;
import model.HttpResponse;
import model.ResponseData;
import util.BeanUtils;

import java.io.IOException;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public class LoginUserController extends AbstractController {
    private UserManageBO userManageBO;

    public LoginUserController() {
        userManageBO = BeanUtils.getBean("userManageBO");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        ResponseData data = userManageBO.login(request);
        response.response(data);
    }
}
