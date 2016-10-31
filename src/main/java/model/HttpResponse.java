package model;

import util.HttpResponseHeaderUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author NAVER
 */
public enum HttpResponse {
    STATUS_200 {
        @Override
        public void response(DataOutputStream dos, ResponseData data) throws IOException {
            byte[] bodyBytes = data.getResponseBody().getBytes();
            dos.writeBytes(HttpResponseHeaderUtils.getHttpResponseHeader200(data.getRequestUrl(), bodyBytes.length));
            dos.write(bodyBytes, 0, bodyBytes.length);
            dos.flush();
        }
    },

    STATUS_302 {
        public void response(DataOutputStream dos, ResponseData data) throws IOException {
            dos.writeBytes(HttpResponseHeaderUtils.getHttpResponseHeader302(data.getRedirectionUrl(), data.isLogin()));
            dos.flush();
        }
    };

    private static Map<String, HttpResponse> statusMap = new HashMap<String, HttpResponse>() {
        {
            put(STATUS_200.name(), STATUS_200);
            put(STATUS_302.name(), STATUS_302);
        }
    };

    public abstract void response(DataOutputStream dos, ResponseData data) throws IOException;

    public static HttpResponse getHttpResponse(String responseStatus) {
        return statusMap.get(responseStatus);
    }
}
