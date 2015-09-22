package com.ttianjun.model;

import com.jfinal.plugin.activerecord.Page;
import com.ttianjun.interceptor.Seach;


public class LoginLog extends SeachModel<LoginLog> {
	private static final long serialVersionUID = 1L;
	public static LoginLog dao = new LoginLog();
	
	public Page<LoginLog> paginate(int pageNumber,int rows,Seach seach){
		String select="select l.*,u.name as user_name";
		String sqlExceptSelect = " from login_log l left join user u on l.user_id = u.id where 1=1 ";
		return paginateBySelect(select, sqlExceptSelect, pageNumber, rows, seach);
	}
}
