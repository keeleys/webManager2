package com.ttianjun.model;

import com.jfinal.plugin.activerecord.*;
import com.ttianjun.interceptor.Seach;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HiWin81 on 2014/11/15 0015.
 */
public class SeachModel<T extends Model<T>> extends Model<T> {

    private static final long serialVersionUID = 7221585097803321618L;

    public Page<T> paginate(int pageNumber,int rows){
        return this.paginate(pageNumber,rows,null);
    }
    public Page<T> paginate(int pageNumber,int rows,Seach seach){
        String select="select * ";
        String sqlExceptSelect = " from "+getTableName()+" where 1=1 ";
        return this.paginateBySelect(select, sqlExceptSelect, pageNumber, rows, seach);
    }

    public Page<T> paginateBySelect(String select,String sqlExceptSelect,int pageNumber,int rows,Seach seach){
        //String select="select * ";
        //String sqlExceptSelect = " from product where 1=1 left join ";
        if(seach!=null){
            sqlExceptSelect+= seach.getSqlWhere();
            sqlExceptSelect+= seach.getOrderBy();
            return super.paginate(pageNumber,rows, select, sqlExceptSelect,seach.getSqlParam().toArray());
        }
        return super.paginate(pageNumber, rows, select, sqlExceptSelect);
    }

    private String getTableName(){
        return TableMapping.me().getTable(getClass()).getName();
    }

    /***
     * 把 model 转化为 list 找到其中的单个属性
     *
     * @param sql
     * @param attr
     * @return
     */
    public List<String> getAttr(String sql, String attr, Object... param)
    {

        List<String> list = new ArrayList<String>();

        List<Record> record = Db.find(sql, param);
        for (int i = 0; i < record.size(); i++) {
            Record t = record.get(0);
            list.add(t.getStr(attr));
        }
        return list;

    }
    public T findByName(String name)
    {
        return findFirst("select * from " + getTableName() + " where name =? ", name);
    }
    /**
     * 将model转换成map 只读
     * @return
     */
    public Map<String, Object> getAttrMaps() {
        return super.getAttrs();
    }
}
