package com.ttianjun.interceptor;

import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <ul>
 * <li>普通查询拦截器</li>
 * <li>前端提交 seach_name</li>
 * <li>那么就是查询 and name =? </li>
 * <li>_分割 前部分是查询类型 后部分是查询字段 </li>
 * @author tianjun
 *
 */
public class Seach extends PrototypeInterceptor {

	public static final String SEACH = "seach";//等于
	public static final String SEACHLIKE = "seachLike";//like
	public static final String SEACHGT = "seachGt";//大于
	public static final String SEACHLT = "seachLt";//小于
	
	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
	}
	public void setSqlParam(List<Object> sqlParam) {
		this.sqlParam = sqlParam;
	}
	private String sqlWhere;
	private List<Object> sqlParam;
	private String orderBy="";
	@Override
	public void doIntercept(ActionInvocation ai) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		Controller ctrl=ai.getController();
		Map<String, String[]> parasMap =ctrl.getParaMap();
		sqlParam = new ArrayList<Object>();
		for (Entry<String, String[]> e : parasMap.entrySet()) {
			String paraKey = e.getKey();
			int index = paraKey.indexOf("_");
			if(index>0){
				String paraName = paraKey.substring(0,index);
				String paraValue =paraKey.substring(index+1);
				String value= e.getValue()[0];
				if(StrKit.isBlank(value)) continue;
				if(paraName.equals(SEACHLIKE)){
					sb.append(" and "+paraValue+" like ?");
					value= "%"+value+"%";
				}else if (paraName.equals(SEACHGT)){
					sb.append(" and "+paraValue+" > = ?");
				}else if (paraName.equals(SEACHLT)) {
					sb.append(" and "+paraValue+" < = ?");
				}else if (paraName.equals(SEACH)) {
					sb.append(" and "+paraValue+" = ?");
				}
				sqlParam.add(value);
			}
		}
		sqlWhere=sb.toString();
		
		orderBy = ctrl.getPara("orderBy");
		if(orderBy==null){
			orderBy = "";
		}
		ctrl.setAttr("seachBean", this);
		ctrl.keepPara();
		ai.invoke();
	}
	public String getSqlWhere() {
		return sqlWhere;
	}
	public List<Object> getSqlParam() {
		return sqlParam;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
}
