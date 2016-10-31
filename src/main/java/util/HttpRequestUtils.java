package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    public static String HTTP_METHOD_GET = "GET";
    public static String HTTP_METHOD_POST = "POST";

    /**
     * @param queryString
     *                  은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies
     *            값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0].trim(), tokens[1].trim());
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static Map<String, String> parseHeader(BufferedReader br) throws IOException {
        Map<String, String> parsingMap = parseFirstLine(br.readLine());
        String line = br.readLine();
        while (!line.equals("")) {
            Pair pair = parseHeader(line);

            if (pair != null) {
                parsingMap.put(pair.getKey(), pair.getValue());
            }

            line = br.readLine();
        }

        String method = parsingMap.get("Method");
        if (HttpRequestUtils.HTTP_METHOD_GET.equals(method)) {
            parsingMap.putAll(parseParameterForGet(parsingMap));
        } else if (HttpRequestUtils.HTTP_METHOD_POST.equals(method)) {
            parsingMap.putAll(parseParameterForPost(parsingMap, br));
        }

        return parsingMap;
    }

    private static Map<String, String> parseFirstLine(String header) {
        Map<String, String> parsingMap = Maps.newHashMap();
        String[] urlTokens = header.split(" ");

        parsingMap.put("Method", urlTokens[0]);
        parsingMap.put("Url", urlTokens[1]);
        parsingMap.put("Protocol", urlTokens[2]);

        return parsingMap;
    }

    public static Map<String, String> parseParameterForGet(Map<String, String> parsingMap) {
        String url = parsingMap.get("Url");

        int index = url.indexOf("?");
        if (index == -1) {
            parsingMap.put("Url", url);
            return parsingMap;
        }

        String requestPath = url.substring(0, index);

        parsingMap.put("Url", requestPath);
        parsingMap.putAll(HttpRequestUtils.parseQueryString(url.substring(index + 1, url.length())));

        return parsingMap;
    }

    public static Map<String, String> parseParameterForPost(Map<String, String> parsingMap, BufferedReader br) throws IOException {
        int contentLength = MapUtils.getIntValue(parsingMap, "Content-Length");

        parsingMap.putAll(HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength)));
        return parsingMap;
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
