package model;

import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public class HttpRequest {
    private BufferedReader br;

    private String method;
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
        if (HttpRequestUtils.HTTP_METHOD_GET.equals(method)) {
            parameter = parseParameterForGet();
        } else if (HttpRequestUtils.HTTP_METHOD_POST.equals(method)) {
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
        method = urlTokens[0];
        path = urlTokens[1];
        protocol = urlTokens[2];

    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
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

    public String getParameter(String key) {
        return parameter.get(key);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
