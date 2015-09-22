package com.ttianjun.ext;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.ttianjun.model.*;

/**
 * Created by HiWin81 on 2014/12/22 0022.
 */
public class ArpFactory {
    public static ActiveRecordPlugin createArp(IDataSourceProvider dataSourceProvider){
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSourceProvider);
        arp.addMapping("user", User.class);
        arp.addMapping("role", Role.class);
        arp.addMapping("menu", Menu.class);
        arp.addMapping("login_log", LoginLog.class);
        arp.addMapping("filebox", FileBox.class);

        arp.setShowSql(true);
        return arp;
    }
}
