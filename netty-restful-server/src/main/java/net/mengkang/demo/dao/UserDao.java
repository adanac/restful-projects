package net.mengkang.demo.dao;

import net.mengkang.demo.entity.UserLite;
import net.mengkang.nettyrest.mysql.MySelect;
import net.mengkang.nettyrest.mysql.Mysql;

import java.util.List;

public class UserDao {

    /**
     * 单独获取用户的名称
     * 面向过程的方式
     * 仅仅为演示其 Mysql 类的使用方便，不推荐这种方式来获取结果
     *
     * @param id
     * @return
     */
    public String getName(int id) {
        String sql = "select name from user where id=?";
        return Mysql.getValue(sql, id);
    }

    public UserLite get(int id) {
        MySelect mySelect = new MySelect<>(new UserLite());
        String sql = "select id,name,icon,birthday from user where id=?";
        return (UserLite) mySelect.get(sql, id);
    }

    @SuppressWarnings("unchecked")
    public List<UserLite> list(int pageNo,int pageSize) {
        MySelect mySelect = new MySelect<>(new UserLite());
        String sql = "select id,name,icon,birthday from user limit ?,?";
        return mySelect.list(sql,pageNo,pageSize);
    }

    public int insert(UserLite user){
        String sql = "insert into user(name,icon) values ?,?";
        return Mysql.insert(sql,user.getName(),user.getIcon());
    }

    public int update(UserLite user){
        String sql = "update user set name = ?, icon = ? where id=?";
        return Mysql.update(sql,user.getName(),user.getIcon(),user.getId());
    }

    public int delete(int uid){
        String sql = "delete from user where id = ?";
        return Mysql.delete(sql,uid);
    }

}
