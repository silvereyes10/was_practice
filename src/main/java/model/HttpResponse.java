package model;

import java.io.DataOutputStream;
import java.io.IOException;

import static util.HttpResponseConstants.HTTP_200_RESPONSE_FORMAT;
import static util.HttpResponseConstants.HTTP_302_RESPONSE_FORMAT;


/**
 * @author NAVER
 */
public enum HttpResponse {
    STATUS_200 {
        @Override
        public void response(DataOutputStream dos, String body) throws IOException {
            byte[] bodyBytes = body.getBytes();
            responseHeader(dos, bodyBytes.length);
            responseBody(dos, bodyBytes);
        }

        private void responseHeader(DataOutputStream dos, int bodyLength) throws IOException {
            dos.writeBytes(String.format(HTTP_200_RESPONSE_FORMAT, bodyLength));
        }

        private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    },

    STATUS_302 {
        public void response(DataOutputStream dos, String redirectionUrl) throws IOException {

            dos.writeBytes(String.format(HTTP_302_RESPONSE_FORMAT, redirectionUrl));
        }
    };

    public abstract void response(DataOutputStream dos, String data) throws IOException;
}
