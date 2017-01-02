package model;

import com.google.common.collect.Maps;
import condition.HttpMethod;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public class HttpRequest {
    private BufferedReader br;

    private HttpMethod method;
    private String path;
    private String protocol;
    private Map<String, String> header;
    private Map<String, String> parameter;

    public HttpRequest(InputStream in) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        this.header = Maps.newHashMap();
        this.parameter = Maps.newHashMap();

        this.parse();
    }

    private void parse() throws IOException {
        this.parseFirstLine(br.readLine());
        this.parseHeader();
        this.parseParameter();
    }

    private void parseParameter() throws IOException {
        if (method.isGet()) {
            parameter = parseParameterForGet();
        } else if (method.isPost()) {
            parameter = parseParameterForPost();
        }
    }

    public Map<String, String> parseParameterForGet() {
        int index = path.indexOf("?");
        if (index == -1) {
            return MapUtils.EMPTY_MAP;
        }

        String parameterString = path.substring(index + 1, path.length());
        path = path.substring(0, index);
        return HttpRequestUtils.parseQueryString(parameterString);
    }

    public Map<String, String> parseParameterForPost() throws IOException {
        int contentLength = MapUtils.getIntValue(header, "Content-Length");

        return HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
    }

    private void parseHeader() throws IOException {
        String line = br.readLine();
        while (!StringUtils.isBlank(line)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);

            if (pair != null) {
                header.put(pair.getKey(), pair.getValue());
            }

            line = br.readLine();
        }
    }

    private void parseFirstLine(String header) {
        String[] urlTokens = header.split(" ");
        method = HttpMethod.valueOf(urlTokens[0]);
        path = urlTokens[1];
        protocol = urlTokens[2];

    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    public String getParameterValue(String key) {
        return parameter.get(key);
    }

    public Map<String, String> getParameter() {
        return Collections.unmodifiableMap(parameter);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
