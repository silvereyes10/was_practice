package controller;

import condition.HttpMethod;
import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public class AbstractController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        HttpMethod method = request.getMethod();

        if (method.isGet()) {
            doGet(request, response);
        } else if (method.isPost()) {
            doPost(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) throws IOException {

    }

    public void doPost(HttpRequest request, HttpResponse response) throws IOException {

    }
}
