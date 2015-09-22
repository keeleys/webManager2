package com.ttianjun.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.ehcache.CacheKit;
import com.ttianjun.ext.Content;
import com.ttianjun.interceptor.LoginAdminInterceptor;
import com.ttianjun.model.LoginLog;
import com.ttianjun.model.Menu;
import com.ttianjun.model.User;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ClearInterceptor
public class HomeController  extends BaseController {
	
	public HomeController() {
		super(null);
	}
    public void index() {
		User user = getCurrentUser();
		if(user==null)
			render("/common/login.html");
		else
			render("/common/index.html");
	}
	public void login(){
		String username = getPara("username");
		String password = getPara("password");
		User user=User.dao.checkUser(username, password);
		if(user==null){
			this.rendJson_(false, "用户名密码不正确");
			return;
		}
		
		String ip = this.getRequest().getHeader("X-Real-IP");
		Date time=new Date();
		if(ip==null){
			ip=this.getRequest().getRemoteAddr();
		}
		LoginLog loginLog=new LoginLog();
		loginLog.set("user_id", user.getInt("id")).set("ip", ip).set("login_time", time).save();
		
		
		
		
		this.setCookie(Content.SESSION_LOGIN_LOG_ID,loginLog.getLong("id").toString(),60*12);//12小时
		setSessionAttr(Content.SESSION_LOGIN_USER, user);
		
		CacheKit.removeAll("authority");//登录的时候 删除所有权限缓存
		this.rendJson_(true, "登录成功");
	}
	@Before(SessionInViewInterceptor.class)
	public void north(){
		render("/common/north.html");
	}
	
	@Before(LoginAdminInterceptor.class)
	public void west(){
		
		User user =getCurrentUser();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for(Menu pm : user.getMenu()){
			Map<String, Object> mMap = pm.getAttrMaps();//将model转换成map 
			mMap.put("slist", user.getMenuSub(pm.getInt("id")));//然后把子菜单放入到map里面
			mapList.add(mMap);
		}
		setAttr("menuList",mapList);
		render("/common/west.html");
	}
	
	
	public void logout(){
		String loginLogId=this.getCookie(Content.SESSION_LOGIN_LOG_ID);
		if(loginLogId!=null&&!"".equals(loginLogId)){
			LoginLog.dao.findById(loginLogId).set("logout_time", new Date()).update();
		}
		this.removeCookie(Content.SESSION_LOGIN_LOG_ID);
		this.removeSessionAttr(Content.SESSION_LOGIN_USER);
		this.redirect("/");
	}
}