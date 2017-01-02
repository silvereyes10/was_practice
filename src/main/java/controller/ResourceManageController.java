package controller;

import bo.ResourceManageBO;
import model.HttpRequest;
import model.HttpResponse;
import model.ResponseData;
import util.BeanUtils;

import java.io.IOException;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public class ResourceManageController extends AbstractController {
    private ResourceManageBO resourceManageBO;

    public ResourceManageController() {
        resourceManageBO = BeanUtils.getBean("resourceMansgeBO");
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        ResponseData data = resourceManageBO.getResource(request);
        response.response(data);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        doGet(request, response);
    }
}
