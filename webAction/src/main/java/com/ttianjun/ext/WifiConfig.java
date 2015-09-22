package com.ttianjun.ext;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.ttianjun.interceptor.AuthorityIntercept;
import com.ttianjun.interceptor.LoginAdminInterceptor;

import freemarker.template.TemplateModelException;

/**
 * Created by HiWin81 on 2014/11/15 0015.
 */
public class WifiConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants constants) {
        constants.setDevMode(true);
        constants.setBaseViewPath("/template");
    }

    @Override
    public void configRoute(Routes routes) {
        routes.add(new WifiRoutes());
    }

    @Override
    public void configPlugin(Plugins plugins) {
        PropKit.use("jdbc.txt");
        DruidPlugin dp = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
        dp.addFilter(new StatFilter());
        WallFilter wall = new WallFilter();
        wall.setDbType("mysql");
        dp.addFilter(wall);
        plugins.add(dp);

        plugins.add(ArpFactory.createArp(dp));

        plugins.add(new EhCachePlugin());

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        interceptors.add(new LoginAdminInterceptor());
        interceptors.add(new AuthorityIntercept());

    }

    @Override
    public void configHandler(Handlers handlers) {
    	handlers.add(new ContextPathHandler("root"));
    }

	@Override
	public void afterJFinalStart() {
		try {
			FreeMarkerRender.getConfiguration().setSharedVariable("root", JFinal.me().getContextPath());
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}

	
    
    
}
