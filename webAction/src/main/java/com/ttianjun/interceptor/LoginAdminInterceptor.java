package com.ttianjun.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.ttianjun.ext.Content;
import com.ttianjun.model.User;

public class LoginAdminInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		// TODO Auto-generated method stub
		Controller ctrl = ai.getController();
		User user = ctrl.getSessionAttr(Content.SESSION_LOGIN_USER);
		if (user == null) {

			if (ctrl.getRequest().getHeader("x-requested-with") != null
					&& ctrl.getRequest().getHeader("x-requested-with")
							.equalsIgnoreCase("XMLHttpRequest"))// 如果是ajax请求响应头会有，x-requested-with；
			{
				ctrl.getResponse().setHeader("sessionstatus", "timeout");// 在响应头设置session状态
				ctrl.renderText("timeout");
				return;
			} else {
				String js="<script>alert('登录超时, 请重新登录.');window.parent.location.href='/';</script>";
				ctrl.renderText(js, "text/html");
			}
		} else {
			ai.invoke();
		}
	}

}
