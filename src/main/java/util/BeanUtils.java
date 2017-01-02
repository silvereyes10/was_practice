package util;

import bo.ResourceManageBO;
import bo.ResourceManageBOImpl;
import bo.UserManageBO;
import bo.UserManageBOImpl;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by silvereyes10 on 2016-11-28.
 */
public class BeanUtils {
    private static Map<String, Object> beanMap;

    static {
        UserManageBO userManageBO = new UserManageBOImpl();
        ResourceManageBO resourceManageBO = new ResourceManageBOImpl();

        beanMap = Maps.newHashMap();
        beanMap.put("userManageBO", userManageBO);
        beanMap.put("resourceMansgeBO", resourceManageBO);
    }

    public static <T> T getBean(String objectId) {
        return (T) beanMap.get(objectId);
    }

    public static void addBean(String beanId, Object bean) {
        beanMap.put(beanId, bean);
    }
}
