package com.ttianjun.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.token.TokenManager;
import com.ttianjun.ext.Content;
import com.ttianjun.interceptor.Seach;
import com.ttianjun.model.Menu;
import com.ttianjun.model.SeachModel;
import com.ttianjun.model.User;
import com.ttianjun.pageModel.DataGrid;
import com.ttianjun.pageModel.TreeNode;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

/**
 * Created by HiWin81 on 2014/11/15 0015.
 */
public class BaseController<M extends SeachModel<M>>  extends Controller {
    public Logger log=Logger.getLogger(getClass());

    protected Model<M> po;
    protected Class<M> modelClass;
    protected static String tokenName="token";
    protected int tokenTout=600;
    protected Table table;

    public BaseController(Class<M> modelClass) {
        super();
        this.modelClass = modelClass;
        if(modelClass!=null)
            this.table = TableMapping.me().getTable(this.modelClass);
    }
    public void index(){
    }
    public Model<M> get(Long id) {

        Model<M> model_;
        try {
            model_ = modelClass.newInstance();
            return model_.findById(id);//Db.findById(this.tableName, id);
        } catch (Exception e) {
        }
        return null;
    }
    public void view() {
        Long id= this.getParaToLong(0,0L);
        if(id!=null&&id!=0L)
            this.setAttr("po",get(id));
        else
            this.setAttr("po",new Record());
    }
    public void edit() {
        try {
            Long id = this.getParaToLong(0, 0L);
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
    public User getCurrentUser(){
        return getSessionAttr(Content.SESSION_LOGIN_USER);
    }

    @Before(Tx.class)
    public void save() {
        String msg;
        try {
            Model<M> m = this.getModel(modelClass);

            if(Arrays.asList(m.getAttrNames()).contains("pid")){//判断是否存在pid
                if(m.get("pid")==null)
                    m.set("pid",0);
            }

            Integer id = m.getInt("id");
            Long tokenl=this.getParaToLong("token");
            if(tokenl!=null){
                if(!TokenManager.validateToken(this, tokenName)){
                    this.rendJson_(false, "会话令牌错误！",id,new Random().nextLong());
                    return;
                }
            }

            Date now=new Date();
            if (id != null && id != 0) {
                msg="修改成功！";
                m.set("update_time",now);//修改时间
                otherSave(m);
                m.update();
            } else {
                msg="保存成功！";
                m.set("create_time",now);//创建时间
                m.save();
                otherSave(m);
            }

            id = m.getInt("id");
            rendJson_(true,msg, id,new Random().nextLong());
        } catch (Exception e) {
            log.error("保存异常", e);
            rendJson_(false, "保存异常！");
            e.printStackTrace();
        }
    }

    public void del() {
        try {
            Long id = this.getParaToLong(0);
            if (id == null||id==0L){
                String ids = this.getPara("ids");
                if (ids != null && !"".equals(ids)) {
                    Db.update("delete from " + this.table.getName() + " where id in ("+ ids+ ")");
                } else {
                    id = this.getParaToLong("ID");
                }
            }else{
                po = get(id);
                if(hashChild(id)==false){
                    Db.deleteById(this.table.getName(), id);
                }else{
                    rendJson_(false, "此数据有相关联的子集数据，请先删除子集数据项！");
                    return;
                }
            }
            rendJson_(true, "删除成功！",id);
        } catch (Exception e) {
            log.error("删除异常", e);
            rendJson_(false, "删除异常！");
        }
    }

    public void combotree() {
        combotree(null);
    }
    public void combotree(List<Integer> ckidList) {
        List<Record> dataList =null;
        String sql="select id,name,pid from "+table.getName();

        Integer id=getParaToInt(0,0);

        String orderBy="";
        if(table.hasColumnLabel("seq"))
            orderBy=" order by seq ";

        if(id==0) dataList = Db.find(sql+orderBy);
        else dataList = Db.find(sql+" where id <> ? "+orderBy,id);



        List<TreeNode> list = new ArrayList<TreeNode>();
        TreeNode rootNode = new TreeNode();
        rootNode.setId(0);
        rootNode.setText("");
        rootNode.setIconCls("icon-home");
        rootNode.setCanChk(false);
        //rootNode.setChecked(true);
        fillTree(dataList, rootNode,"name",true,ckidList);
        list.add(rootNode);
        this.renderJson(list);
    }
    public void combobox(){

        int id=getParaToInt(0,0);

        List<Record> list=null;
        String sql="select * from "+table.getName();

        if(id==0)  list = Db.find(sql);
        else  list = Db.find(sql+" where id <>?",id);

        this.renderJson(list);
    }

    /**树形表格*/
    public void treeDataGrid(List<Record> list) {
        DataGrid dg=new DataGrid();
        List<Map<String,Object>> list1=new ArrayList<Map<String,Object>>();
        for(Record r:list){
            Map<String,Object> d=new HashMap<String,Object>();
            for(String col:r.getColumnNames()){
                d.put(col,r.get(col));
            }
            Integer pid=r.getInt("pid");
            //Integer id=r.getInt("id");
            if(pid!=null&&pid!=0)
                d.put("_parentId",pid);//tree datagrid 需要使用此字段作为父节点关联

            //String sql="select * from "+table.getName()+" where pid=?";
            //if(Db.find(sql, id).size()>0)//判断是否有子节点
            //	d.put("state", "closed");
            list1.add(d);
        }
        dg.setRows(list1);
        dg.setTotal(list1.size());
        this.renderJson(dg);
    }

    /**
     * 通过pid字段关联
     * @param dataList 集合
     * @param pnode 父节点
     * @param cname 节点名称字段
     */
    protected void fillTree(List<Record> dataList,TreeNode pnode,String cname,Boolean canChk,List<Integer>ckidList) {
        List<TreeNode> childelist = new ArrayList<TreeNode>();
        for (Record m : dataList) {
            Integer pid_ = m.getInt("pid");
            if (pnode.getId() == pid_) {
                Integer id = m.getInt("id");
                TreeNode nodechild = new TreeNode();
                nodechild.setId(id);
                nodechild.setText(m.getStr(cname));
                nodechild.setCanChk(canChk);
                childelist.add(nodechild);
                if(ckidList!=null&& ArrayUtils.contains(ckidList.toArray(), id))
                    nodechild.setChecked(true);
                fillTree(dataList, nodechild,cname,canChk,ckidList);
            }
        }
        if (childelist.isEmpty() == false && childelist.size() > 0) {
            pnode.setChildren(childelist);
        }
    }
    /**
     * 保存修改的其他操作
     * @param m
     */
    protected void otherSave(Model<M> m){

    }
    /**是否有子集 针对树形数据*/
    private boolean hashChild(Long id){
        try{
            Table tableInfo = TableMapping.me().getTable(modelClass);
            if(tableInfo.hasColumnLabel("pid")){
                List<Record> list=Db.find("select * from "+tableInfo.getName()+" where pid=?", id);
                return list!=null&&list.isEmpty()==false;
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }

    @Before(Seach.class)
    public void grid(){
        Seach seach = getAttr("seachBean");
        int page=getParaToInt("page", 1);
        int rows=this.getParaToInt("rows", 20);
        otherSeach(seach);//一些其他的查询
        try {
            Page<M> date=modelClass.newInstance().paginate(page,rows,seach);
            rendDataGrid(date);
        } catch (Exception e) {
            log.error("gird",e);
            rendJson_(false, e.getMessage());
        }
    }


    /**
     * 返回easyui格式的列表数据
     * @param page 分页数据
     * @author 田俊
     */
    public void rendDataGrid(Page<M> page){
        renderJson( new DataGrid(page.getList(),page.getTotalRow()));
    }

    /**
     * 其他查询操作
     * @param seach
     */
    protected void otherSeach(Seach seach) {

    }
    public void rendJson_(boolean success,String msg,Object... obj){
        Map<String,Object> json=new HashMap<String,Object>();
        json.put("success",success);
        json.put("msg",msg);
        if(obj!=null&&obj.length>0){
            json.put("obj",obj[0]);
            if(obj.length>1){
                json.put("tokenid",obj[1]);
            }
        }
        this.renderJson(json);
    }
    /**
     * 是否有权限
     * @param key
     * @return
     */
    public  boolean hasPerm(String key){
        //是否有权限 返回true代表有权限
        User user = getCurrentUser();
        List<Menu> menuList = user.getMenuCommon();
        if ( key != null) {
            for(Menu m : menuList){
                if(key.equals( m.get("url") ) )
                    return true;
            }
        }
        return false;
    }

}
