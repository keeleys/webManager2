package com.ttianjun.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.ttianjun.model.User;

public class UserController extends BaseController<User>{
	
	public UserController() {
		super(User.class);
	}

	@Override
	protected void otherSave(Model<User> m) {
		// TODO Auto-generated method stub
		Integer user_id = m.getInt("id");
		String []sidList=getParaValues("sids");
		String sids="";// 子商户ID串
		Db.update("delete from user_role where user_id=?", user_id);//先删除所有权限
		if(sidList!=null){
			for(String id_str : sidList) {
				sids+=id_str+",";
				Integer sid = Integer.parseInt(id_str);
				Db.update("insert into user_role(user_id,role_id,create_time) values(?,?,now())", user_id, sid);
			}
			if(sids.length()>1)  sids=sids.substring(0, sids.length()-1);
		}
		m.set("role_ids", sids);
		
	}
	
}
