package com.ttianjun.model;

import java.util.List;

/**
 * Created by HiWin81 on 2014/11/15 0015.
 */
public class User  extends SeachModel<User> {
    public static final User dao = new User();

    public User checkUser(String name,String pwd){
        String sql="select * from user where name =? and password=? ";
        return findFirst(sql, name,pwd);
    }

    /**
     * 获取主要的菜单分组
     * @return
     */
    public List<Menu> getMenu(){
        String sql="select DISTINCT ppm.* "
                + "from role r "
                + "inner join role_menu rm "
                + "on r.id = rm.role_id "
                + "inner join menu m "
                + "on rm.menu_id = m.id "
                + "inner join user_role ur "
                + "on  ur.role_id = r.id "
                + "inner join user u "
                + "on ur.user_id = u.id "
                + "inner join menu pm "
                + "on m.pid = pm.id "
                + "inner join menu ppm "
                + "on pm.pid = ppm.id "
                + "where ur.user_id = ? "
                + "order by ppm.seq";
        return Menu.dao.findByCache("authority","menu_"+get("id"),sql,get("id"));
    }
    /**
     * 根据PID查询所以子menu
     * @param pid
     * @return
     */
    public List<Menu> getMenuSub(Integer pid){
        String sql="select DISTINCT pm.* "
                + "from role r "
                + "inner join role_menu rm "
                + "on r.id = rm.role_id "
                + "inner join menu m "
                + "on rm.menu_id = m.id "
                + "inner join user_role ur "
                + "on  ur.role_id = r.id "
                + "inner join user u "
                + "on ur.user_id = u.id "
                + "inner join menu pm "
                + "on m.pid = pm.id "
                + "inner join menu ppm "
                + "on pm.pid = ppm.id "
                + "where ur.user_id = ? and ppm.id=? "
                + "order by pm.seq";
        return Menu.dao.findByCache("authority","menusub_"+get("id")+"_"+pid,sql,get("id"),pid);
    }
    /**
     * 得到所有操作类型的menu
     * 缓存 authority menuCommon_get("id")
     * @return
     */
    public List<Menu> getMenuCommon(){
        String sql="select DISTINCT m.*  "
                + "from menu m "
                + "inner join role_menu rm "
                + "on rm.menu_id = m.id "
                + "inner join role r "
                + "on rm.role_id = r.id "
                + "inner join user_role ur "
                + "on ur.role_id=r.id "
                + "where  ur.user_id=? and m.type=1";

        return Menu.dao.findByCache("authority","menuCommon_"+get("id"),sql,get("id"));
    }
}
