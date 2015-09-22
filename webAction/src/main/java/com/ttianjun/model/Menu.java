package com.ttianjun.model;

import java.util.List;

public class Menu  extends SeachModel<Menu>{
	private static final long serialVersionUID = 1L;
	public static final Menu dao = new Menu();
	
	/**
	 * 获取子菜单
	 * @return
	 */
	public List<Menu> getSubList(){
		String sql ="select * from menu where pid = ?";
		return Menu.dao.find(sql,get("id"));
	}
}
