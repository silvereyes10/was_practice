package model;

import constant.HttpConstants;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
        ResponseData data = new ResponseData("/index.html");
        data.setResponseStatus(HttpConstants.HTTP_STATUS_200);
        response.response(data);
    }

    @Test
    public void responseRedirect() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        ResponseData data = new ResponseData("/index.html");
        data.setRedirectionUrl("/test.html");
        data.setResponseStatus(HttpConstants.HTTP_STATUS_302);
        response.response(data);
    }

    @Test
    public void responseCookies() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        ResponseData data = new ResponseData("/index.html");
        data.setRedirectionUrl("/test.html");
        data.addHeader("logined", "true");
        data.setResponseStatus(HttpConstants.HTTP_STATUS_302);
        response.response(data);
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
