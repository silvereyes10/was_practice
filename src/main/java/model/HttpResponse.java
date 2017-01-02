package model;

import bo.ResourceManageBOImpl;
import org.apache.commons.lang3.StringUtils;
import util.HttpResponseUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by silvereyes10 on 2016-11-14.
 */
public class HttpResponse {
    private ResourceManageBOImpl resourceManagerBO;
    private OutputStream dos;

    public HttpResponse(OutputStream outputStream) {
        dos = outputStream;
        resourceManagerBO = new ResourceManageBOImpl();
    }

    public void response(ResponseData data) throws IOException {
        dos.write(HttpResponseUtils.getHttpResponseHeader(data).getBytes());

        if (StringUtils.isNotBlank(data.getResponseBody())) {
            byte[] bodyBytes = data.getResponseBody().getBytes();
            dos.write(bodyBytes, 0, bodyBytes.length);
        }

        dos.flush();
    }
}
