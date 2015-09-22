package com.ttianjun.ext;

import com.jfinal.config.Routes;
import com.ttianjun.controller.*;


public class WifiRoutes extends Routes {
    @Override
    public void config() {
        add("/", HomeController.class,"/common");
        add("/loginLog", LoginLogCtrl.class);
        add("/user", UserController.class);
        add("/filebox", FileBoxController.class);
        add("/file", FileController.class);
        add("/menu", MenuController.class);
        add("/role", RoleController.class);
    }
}
