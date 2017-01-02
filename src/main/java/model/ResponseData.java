package model;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * @author NAVER
 */
public class ResponseData {
    private String requestUrl;

    private String responseBody;

    private String redirectionUrl;

    private String responseStatus;

    private int contentLength = 0;

    private Map<String, String> headerMap;

    public ResponseData(String requestUrl) {
        this.requestUrl = requestUrl;
        this.headerMap = Maps.newHashMap();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void addHeader(String key, String value) {
        this.headerMap.put(key, value);
    }

    public Map<String, String> getHeaderMap() {
        return Collections.unmodifiableMap(this.headerMap);
    }
}
