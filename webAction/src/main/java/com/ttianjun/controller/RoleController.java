package com.ttianjun.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.ttianjun.interceptor.Seach;
import com.ttianjun.model.Role;
import com.ttianjun.pageModel.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class RoleController extends BaseController<Role>{

	public RoleController() {
		super(Role.class);
	}

	/**
	 * 下拉列表多选
	 */
	public void combotree() {

		String sql="select id,name from role";
		if(isParaExists(0)){		
			//不选自己
			sql+=" where id <> "+getPara();
		}
		
		List<Record> dataList = Db.find(sql);
		List<TreeNode> list = new ArrayList<TreeNode>();
		for(Record r : dataList ){
			TreeNode rootNode = new TreeNode();
			rootNode.setId(r.getInt("id"));
			rootNode.setText(r.getStr("name"));
			rootNode.setIconCls("icon-home");
			rootNode.setCanChk(false);
			list.add(rootNode);
		}
		this.renderJson(list);
	}
	
	@Override
	protected void otherSeach(Seach seach) {
		// TODO Auto-generated method stub
		super.otherSeach(seach);
		seach.setOrderBy(" order by seq ");
	}

	@Override
	protected void otherSave(Model<Role> m) {
		// TODO Auto-generated method stub
		super.otherSave(m);
		Integer id = m.getInt("id");
		Db.update("delete from role_menu where role_id=?", id);//先删除所有权限
		String menuIds = m.getStr("menuIds");
		for(String id_str : menuIds.split(",")){
			Integer sid = Integer.parseInt(id_str);
			Db.update("insert into role_menu(role_id,menu_id,create_time) values(?,?,now())", id, sid);
		}
		
	}
	
	
	

}