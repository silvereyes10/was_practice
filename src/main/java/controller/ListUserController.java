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
public class ListUserController extends AbstractController {
    private UserManageBO userManageBO;

    public ListUserController() {
        userManageBO = BeanUtils.getBean("userManageBO");
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        ResponseData data = userManageBO.list(request);
        response.response(data);
    }
}
