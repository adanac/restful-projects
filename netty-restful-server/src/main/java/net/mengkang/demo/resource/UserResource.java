package net.mengkang.demo.resource;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import net.mengkang.demo.bo.Icon;
import net.mengkang.demo.bo.UserCreateSuccess;
import net.mengkang.demo.bo.UserInfo;
import net.mengkang.demo.entity.User;
import net.mengkang.nettyrest.StatusCode;
import net.mengkang.nettyrest.response.Info;
import net.mengkang.nettyrest.response.Result;
import net.mengkang.nettyrest.ApiProtocol;
import net.mengkang.demo.service.UserService;
import net.mengkang.nettyrest.BaseResource;
import net.mengkang.nettyrest.utils.HttpRequestUtil;
import net.mengkang.nettyrest.utils.RequestParser;
import org.apache.http.MethodNotSupportedException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserResource extends BaseResource {

    public UserResource(ApiProtocol apiProtocol) {
        super(apiProtocol);
    }

    public Result get() {
        UserInfo userInfo = null;

        int uid;

        Object uidCheck = parameterCheck(apiProtocol, "uid");
        if (uidCheck instanceof Result) {
            return (Result) uidCheck;
        } else {
            uid = Integer.parseInt((String) uidCheck);
        }

        UserService userService = new UserService(apiProtocol);
        User user = userService.get(uid);
        if (null != user) {
            userInfo = new UserInfo(user);
        }

        return new Result<Info>(userInfo);
    }

    public Result post2() {
        UserCreateSuccess userCreateSuccess = new UserCreateSuccess();
        userCreateSuccess.setId(2);
        userCreateSuccess.setUrl("http://netty.restful.api.mengkang.net/user/2");
        userCreateSuccess.setCode(StatusCode.CREATED_SUCCESS);
        return new Result<>(userCreateSuccess);
    }

    public Result post() {
        Info info = new Info();
        try {
            // Map<String, String> param = new RequestParser(request).parse();
            UserService userService = new UserService(apiProtocol);

            User user = parseUserFromProtocol(apiProtocol);
            boolean flag = userService.update(user);
            info.setCode(flag == true ? StatusCode.UPDATED_SUCCESS : StatusCode.UPDATED_SUCCESS + 1);
            info.setCodeMessage(StatusCode.codeMap.get(StatusCode.UPDATED_SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<Info>(info);
    }

    private User parseUserFromProtocol(ApiProtocol apiProtocol) {
        User user = new User();
        if (apiProtocol.getParameters().containsKey("uid")) {
            user.setId(Integer.parseInt((String) parameterCheck(apiProtocol, "uid")));
        }
        if (apiProtocol.getParameters().containsKey("username")) {
            user.setName((String) parameterCheck(apiProtocol, "username"));
        }
        if (apiProtocol.getParameters().containsKey("iconUrl")) {
            user.setIcon(new Icon(10, 10, (String) parameterCheck(apiProtocol, "iconUrl")));
        }
        return user;
    }

    public Result delete() {
        Info info = new Info();
        UserService userService = new UserService(apiProtocol);
        Integer uid = Integer.parseInt((String) parameterCheck(apiProtocol, "uid"));
        boolean flag = userService.delete(uid);
        info.setCode(flag == true ? StatusCode.DELETED_SUCCESS : StatusCode.DELETED_SUCCESS + 1);
        info.setCodeMessage(StatusCode.codeMap.get(StatusCode.DELETED_SUCCESS));
        return new Result(info);
    }

}
