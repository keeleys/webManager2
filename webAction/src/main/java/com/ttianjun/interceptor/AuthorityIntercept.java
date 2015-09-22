package com.ttianjun.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.ttianjun.ext.Content;
import com.ttianjun.model.Menu;
import com.ttianjun.model.User;


import java.util.List;

/**
 * 权限拦截
 * @author TianJun
 *
 */
public class AuthorityIntercept implements Interceptor {
	
	private List<Menu> menuList;//用户的操作权限
	public void intercept(ActionInvocation ai) {
		// TODO Auto-generated method stub
		
		//不需要判断权限的方法名（一般是些ajax组件 不是页面）
		String filter ="combobox,grid,combotree,treeDataGrid,swfup";
		
		Controller c = ai.getController();
		User user = c.getSessionAttr(Content.SESSION_LOGIN_USER);
		menuList = user.getMenuCommon();
		
		String actionKey = ai.getActionKey();// 访问请求路径  例 /product/add
		String method = ai.getMethodName();//访问的方法名   例add
		String controllerKey =ai.getControllerKey();//访问的控制器 例/product
		
		//如果系统管理员 直接跳过权限验证
		if(user.getInt("id")==1){
			c.setAttr("hasAdd",true);
			c.setAttr("hasEdit",true);
			c.setAttr("hasDel",true);
			c.setAttr("hasView",true);
			ai.invoke();
			return;
		}
		//过滤一些不需要验证的权限
		if(method!=null&&filter.indexOf(method)!=-1){
			System.out.println(actionKey+" 有权限");
			ai.invoke();
			return;
		}
		
		//如果方法是save 并且有and或者edit权限
		
		if(method.equals("save")){
			if(hasPerm(controllerKey+"/add") || hasPerm(controllerKey+"/edit") ){
				ai.invoke();
				return;
			}
		}
		//判断是否有请求权限
		if( hasPerm(actionKey)){
			if(method.equals("index")){
				//如果是index 添加页面按钮判断
				c.setAttr("hasAdd",hasPerm(controllerKey+"/add"));
				c.setAttr("hasEdit",hasPerm(controllerKey+"/edit"));
				c.setAttr("hasDel",hasPerm(controllerKey+"/del"));
				c.setAttr("hasView",hasPerm(controllerKey+"/view"));
			}
			System.out.println(actionKey+ " 有权限");
			ai.invoke();
			return;
		}
		
		System.out.println(actionKey+" 没权限");
		c.render("/common/authority_error.html");
	}

	public  boolean hasPerm(String key){
		//是否有权限 返回true代表有权限
		if ( key != null) {
			for(Menu m : menuList){
				if(key.equals( m.get("url") ) )
					return true;
			}
		}
		return false;
	}
}
