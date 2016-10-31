package bo;

import model.ResponseData;

import java.io.IOException;
import java.util.Map;

/**
 * @author NAVER
 */
public interface UserManageBO {
    ResponseData process(String url, Map<String, String> parsingMap) throws IOException;
}
