package manage;

import model.ResponseData;

import java.io.IOException;
import java.util.Map;

/**
 * Created by silvereyes10 on 2016-11-14.
 */
public interface UrlConnector {
    ResponseData process(Map<String, String> parsingMap) throws IOException;
}
