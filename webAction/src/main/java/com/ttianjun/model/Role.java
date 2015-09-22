package com.ttianjun.model;

import java.util.List;

public class Role extends SeachModel<Role>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Role dao = new Role();
	
	public List<User> getUsers(){
		String sql="from user where role_id = ?";
		return User.dao.find(sql, this.getInt("id"));
	}
	/**
	 * 根据权限ID查询出对应的所有类型为功能的menuURL
	 * @param role_id
	 * @return
	 */
	public List<String> getMenuUrl(Integer role_id)
	{
		String sql="select m.url from menu m "
				+ "inner join role_menu rm on m.id =rm.menu_id"
				+ "inner join role r on r.id = rm.role_id "
				+ "where role.id =? and m.url is not null and m.type = 1";
		return  getAttr(sql, "url", role_id);
	}
}
