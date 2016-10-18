package model;

import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author NAVER
 */
public enum HttpResponse {
    STATUS_200 {
        @Override
        public void response(DataOutputStream dos, String body) throws IOException {
            responseHeader(dos, body.length());
            responseBody(dos, body.getBytes());
        }

        private void responseHeader(DataOutputStream dos, int bodyLength) throws IOException {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + bodyLength + "\r\n");
            dos.writeBytes("\r\n");
        }

        private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    },

    STATUS_302 {
        public void response(DataOutputStream dos, String redirectionUrl) throws IOException {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectionUrl + " \r\n");
            dos.writeBytes("\r\n");
        }
    };

    public abstract void response(DataOutputStream dos, String data) throws IOException;
}
