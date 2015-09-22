package com.ttianjun.controller;

import com.jfinal.plugin.activerecord.Db;
import com.ttianjun.interceptor.Seach;
import com.ttianjun.model.FileBox;

import java.io.File;
import java.io.IOException;

/**
 * Created by HiWin81 on 2014/12/22 0022.
 */
public class FileBoxController extends BaseController<FileBox> {
    public FileBoxController() {
        super(FileBox.class);
    }

    @Override
    protected void otherSeach(Seach seach) {
        // TODO Auto-generated method stub
        seach.setOrderBy(" order by id desc");
    }

    public void del() {
        String ids=this.getPara("ids");
        try{
            String[] idList=ids.split(",");
            for(String id:idList){
                FileBox fb=FileBox.dao.findById(id);
                try{
                    String fp=this.getRequest().getRealPath("/")+FileController.uploadroot+"/"+fb.getStr("filePath");
                    log.info(fp);
                    File f=new File(fp);
                    f.delete();
                }catch(Exception e){

                }
            }
            Db.update("delete from filebox where id in(" + ids + ")");
            rendJson_(true, "删除成功！");
        }catch(Exception e){
            log.error("删除文件站错误",e);
            rendJson_(false, "删除异常！");
        }
    }
}