package net.mengkang.demo.service;

import net.mengkang.demo.bo.Icon;
import net.mengkang.demo.dao.UserDao;
import net.mengkang.demo.entity.User;
import net.mengkang.demo.entity.UserLite;
import net.mengkang.nettyrest.ApiProtocol;

import java.util.ArrayList;
import java.util.List;

public class UserService extends BaseService {

    public UserService(ApiProtocol apiProtocol) {
        super(apiProtocol);
    }

    /**
     * 查询单个用户信息
     *
     * @param uid
     * @return
     */
    public User get(int uid) {

        if (apiProtocol.getBuild() > 105) {
            return getFromDatabase(uid);
        }

        return new User(uid, "xxx", 25);
    }

    /**
     * 分页查询用户信息
     *
     * @return
     */
    public List<User> list(int pageNo, int pageSize) {

        if (apiProtocol.getBuild() > 105) {
            return getListFromDatabase(pageNo, pageSize);
        }

        List<User> users = new ArrayList<User>();
        users.add(new User(1, "mengkang.zhou", 25));
        users.add(new User(2, "meng.zhou", 23));

        return users;
    }

    public int inserst(User user) {
        if (apiProtocol.getBuild() > 105) {
            return insertFromDatabase(user);
        }
        return 0;
    }

    public boolean update(User user) {
        if (apiProtocol.getBuild() > 105) {
            return updateFromDatabase(user);
        }
        return false;
    }

    private int insertFromDatabase(User user) {
        UserDao userDao = new UserDao();
        UserLite userLite = new UserLite();
        userLite.setName(user.getName());
        userLite.setIcon(user.getIcon().getUrl());
        return userDao.insert(userLite);
    }


    private boolean updateFromDatabase(User user) {
        boolean flag = false;
        UserDao userDao = new UserDao();
        UserLite userLite = userDao.get(user.getId());
        if (null != userLite) {
            if (null != user.getName()) {
                userLite.setName(user.getName());
            }
            if (null != user.getIcon() && null != user.getIcon().getUrl()) {
                userLite.setIcon(user.getIcon().getUrl());
            }
            int update = userDao.update(userLite);
            if (update > 0) {
                flag = true;
            }
        }
        return flag;
    }


    public User getFromDatabase(int uid) {
        UserDao userDao = new UserDao();
        UserLite userLite = userDao.get(uid);
        if (null!=userLite) {
            return exchange(userLite);
        }
        return null;
    }

    public List<User> getListFromDatabase(int pageNo, int pageSize) {
        List<UserLite> userLites = new UserDao().list(pageNo, pageSize);
        List<User> users = new ArrayList<User>();

        for (UserLite userLite : userLites) {
            users.add(exchange(userLite));
        }

        return users;
    }

    public User exchange(UserLite userLite) {
        Icon icon = IconService.get(userLite.getIcon());
        int age = (((int) (System.currentTimeMillis() / 1000)) - userLite.getBirthday()) / (24 * 3600 * 365);
        return new User(userLite.getId(), userLite.getName(), icon, age);
    }

    public boolean delete(int uid) {
        UserDao userDao = new UserDao();
        int delete = userDao.delete(uid);
        return delete > 0 ? true : false;
    }
}
