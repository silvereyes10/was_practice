package bo;

import model.HttpRequest;
import model.ResponseData;

import java.io.IOException;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public interface ResourceManageBO {
    ResponseData getResource(HttpRequest request) throws IOException;
}
