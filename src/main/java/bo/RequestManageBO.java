package bo;

import model.ResponseData;

import java.io.IOException;
import java.util.Map;

/**
 * Created by silvereyes10 on 2016-10-31.
 */
public interface RequestManageBO {
    ResponseData process(String url, Map<String, String> parsingMap) throws IOException;
}
