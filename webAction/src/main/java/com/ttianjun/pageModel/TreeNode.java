package com.ttianjun.pageModel;

import java.util.List;
import java.util.Map;

/**
 * easyui使用的tree模型
 * @author tianjun
 * TreeNode rootNode = new TreeNode();
		rootNode.setId(0L);
		rootNode.setText("");
		rootNode.setIconCls("icon-home");
		rootNode.setCanChk(false);
		fillTree(dataList, rootNode,"cname",false,idlist);
		
$(function(){
$('#tt').tree({url:'/sys/partment/combotree',valueField:'ID',textField:'CNAME',animate:true,onSelect:function(node){
	$("#partid").val(node.id);
	_search();
}});
});
 */
public class TreeNode implements java.io.Serializable {
	private static final long serialVersionUID = 5490659661376921458L;
	private Integer id;
	/** 树节点名称 */
	private String text;
	/** 前面的小图标样式 */
	private String iconCls; //icon-home
	/** 是否勾选状态 */
	private Boolean checked;
	/** 其他参数 */
	private Map<String, Object> attributes;
	/** 子节点 */
	private List<TreeNode> children;
	/** 是否展开(open,closed) */
	private String state = "open";
	/** 是否能够选择 */
	private Boolean canChk;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Boolean getCanChk() {
		return canChk;
	}

	public void setCanChk(Boolean canChk) {
		this.canChk = canChk;
	}

}
