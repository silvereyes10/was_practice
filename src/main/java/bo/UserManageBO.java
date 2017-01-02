package bo;

import model.HttpRequest;
import model.ResponseData;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public interface UserManageBO {
    ResponseData list(HttpRequest request);

    ResponseData login(HttpRequest request);

    ResponseData create(HttpRequest request);
}
