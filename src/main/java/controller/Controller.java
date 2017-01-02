package controller;

import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public interface Controller {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
