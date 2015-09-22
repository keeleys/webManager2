package com.ttianjun.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.token.TokenManager;
import com.ttianjun.model.Menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuController extends BaseController<Menu>{

	public MenuController() {
		super(Menu.class);
	}
	
	public void treeDataGrid() {
		
		List<Record> list= Db.find(
                "select a.*,p.name pname  from menu a left join menu p on p.id=a.pid order by a.seq ");
		treeDataGrid(list);
	}
	public void edit() {
		try {
			Long id = this.getParaToLong(1, 0L);
			Integer pid = this.getParaToInt(0, 0);
			if(pid!=0){
				po = new Menu();
				po.set("pid",pid);
			}
			if (id != 0L) {
				po = get(id);
			}else{
				setAttr("now",new Date());
			}
			TokenManager.createToken(this, tokenName, tokenTout);
			
			this.setAttr("po",po);
			 
		} catch (Exception e) {
			log.error("编辑异常", e);
		}
	}
	public void combotree() {
		if(!isParaBlank("ids")){
			String ids = getPara("ids");
			List<Integer> idList = new ArrayList<Integer>();
			for(String id : ids.split(",")){
				idList.add(Integer.parseInt(id));
			}
			combotree(idList);
		}else{
			combotree(null);
		}
	}

}
