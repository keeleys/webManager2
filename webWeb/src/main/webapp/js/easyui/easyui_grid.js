//定义一些默认的列表样式
$.fn.datagrid.defaults.toolbar='#grid_toolbar';
$.fn.datagrid.defaults.idField = 'id';
$.fn.datagrid.defaults.pagination = true;
$.fn.datagrid.defaults.pageSize = 10;
$.fn.datagrid.defaults.ctrlSelect = true;
$.fn.datagrid.defaults.pageList = [10,20,30,50];
$.fn.datagrid.defaults.loadMsg="数据加载中，请稍后";
$.fn.datagrid.defaults.rownumbers=true;
$.fn.datagrid.defaults.ctrlSelect=true;
$.fn.datagrid.defaults.onDblClickRow=function(rowIndex, rowData){
		$(this).datagrid('unselectAll');
		$(this).datagrid('selectRow', rowIndex);
		gridCommon.view();
};
//扩展easyui datagrid的两个方法.动态添加和删除toolbar的项
$.extend($.fn.datagrid.methods, {  
    addToolbarItem: function(jq, items){  
        return jq.each(function(){  
            var toolbar = $(this).parent().prev("div.datagrid-toolbar");
            for(var i = 0;i<items.length;i++){
                var item = items[i];
                if(item === "-"){
                    toolbar.append('<div class="datagrid-btn-separator"></div>');
                }else{
                    var btn=$("<a href=\"javascript:void(0)\"></a>");
                    btn[0].onclick=eval(item.handler||function(){});
                    btn.appendTo(toolbar).linkbutton($.extend({},item,{plain:true}));
                    //从前面开始添加 
                    //btn.css("float","left").appendTo(toolbar).linkbutton($.extend({},item,{plain:true}));
                }
            }
            toolbar = null;
        });  
    },
    removeToolbarItem: function(jq, param){  
        return jq.each(function(){  
            var btns = $(this).parent().prev("div.datagrid-toolbar").children("a");
            var cbtn = null;
            if(typeof param == "number"){
                cbtn = btns.eq(param);
            }else if(typeof param == "string"){
                var text = null;
                btns.each(function(){
                    text = $(this).data().linkbutton.options.text;
                    if(text == param){
                        cbtn = $(this);
                        text = null;
                        return;
                    }
                });
            } 
            if(cbtn){
                var prev = cbtn.prev()[0];
                var next = cbtn.next()[0];
                if(prev && next && prev.nodeName == "DIV" && prev.nodeName == next.nodeName){
                    $(prev).remove();
                }else if(next && next.nodeName == "DIV"){
                    $(next).remove();
                }else if(prev && prev.nodeName == "DIV"){
                    $(prev).remove();
                }
                cbtn.remove();    
                cbtn= null;
            }                        
        });  
    }                 
});

//
//1 $('#tt').datagrid("addToolbarItem",[{"text":"xxx"},"-",{"text":"xxxsss","iconCls":"icon-ok"}])
//1 $('#tt').datagrid("removeToolbarItem","GetChanges")//根据btn的text删除
//1 $('#tt').datagrid("removeToolbarItem",0)//根据下标删除


//定义默认的操作 调用之前先init赋值
var gridCommon = (function(gridCommon) {
	//打开弹出窗的方法
	var model_title;
	var model_name;
	var datagrid;
	function init(title,name,dg,isTree){
		model_title=title;
		model_name=name;
		datagrid=function(args){
			if(isTree){
				return dg.treegrid.call(dg,args);
			}else{
				return dg.datagrid.call(dg,args);
			}
		};
	}
	function ooip(url, title) {
		$.messager.progress({
			text : '数据加载中....',
			interval : 100
		});
		$('#Dialog_').dialog({
			title : '查看-' + model_title,
			width : 800,
			top : 0,
			cache : false,
			maximizable : true,
			href : url,
			modal : true,
			onLoad : function() {
				$.messager.progress('close');
			},
			onLoadError : function() {
				$.messager.progress('close');
			},
			buttons : [ {
				text : "关闭",
				handler : function() {
					$('#Dialog_').dialog('close');
				}
			} ]
		});
	}

	function del() {
		var rows = datagrid('getSelections');
		var ids = [];
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
				if (r) {
					for (var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : model_name + "/del",
						data : {
							ids : ids.join(',')
						},
						dataType : 'json',
						success : function(r) {
							datagrid('load');
							// datagrid.treegrid('load');
							datagrid('unselectAll');
							if (r.success) {
								$.messager.show({
									msg : r.msg,
									title : '提示'
								});
								editRow = undefined;
							} else {
								$.messager.alert("操作错误", r.msg, 'error');
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	// 编辑
	function edit() {
		var rows = datagrid('getSelections');
		if (rows.length == 1) {
			add(rows[0].id);
		} else {
			$.messager.alert('提示', '请选择一条记录！', 'error');
		}
	}

	// 新增
	function add(id_) {
		$.messager.progress({
			text : '数据加载中....',
			interval : 100
		});
		$('#Dialog_').dialog({
			title : (id_ == undefined ? "新增" : "编辑") + "-" + model_title,
			cache : false,
			maximizable : true,
			resizable : true,
			width : 800,
			top : 0,
			href : model_name + '/edit/' + (id_ == undefined ? '' : id_),
			modal : false,
			onLoad : function() {
				$.messager.progress('close');
				try {
					editor = KindEditor.create('.keditor', {
						themeType : 'default',
						uploadJson : '/file/up',
						fileManagerJson : '/file/manager',
						allowFileManager : false
					});
				} catch (e) {
				}
			},
			buttons : [ {
				text : id_ == undefined ? "保存" : "修改",
				iconCls : 'icon-save',
				handler : function() {
					editForm = $('#editForm').form({
						url : model_name + '/save',
						success : function(data) {
							var json = $.parseJSON(data);
							if (json && json.success) {
								$.messager.show({
									title : '成功',
									msg : json.msg
								});
								datagrid('reload');
								// datagrid.treegrid('reload');
								$("#poid").val(json.obj);
								$('#Dialog_').dialog('close');
							} else {
								$.messager.show({
									title : '失败',
									msg : json.msg
								});
							}
						}
					});
					if (editor != undefined) {
						editor.sync();// ajax提交时需要执行此方法
						editor.sync();
					}
					editForm.submit();
				}
			}, {
				text : "关闭",
				iconCls : 'icon-cancel',
				handler : function() {
					$('#Dialog_').dialog('close');
				}
			} ]
		});
	}
	// 查看
	function view(id_, title) {
		if (id_ == undefined) {
			var rows = datagrid('getSelections');
			if (rows.length == 1) {
				id_ = rows[0].id;
			} else {
				$.messager.alert('提示', '请选择一条记录！', 'error');
				return;
			}
			ooip(model_name + "/view/" + id_, title);
		}
	}

	// 查询
	function _search() {
		datagrid('load', sy.serializeObject($("#searchForm")));
		// datagrid.treegrid('load', sy.serializeObject($("#searchForm")));
	}
	// 清除查询
	function cleanSearch() {
		datagrid('load', {});
		// datagrid.treegrid('reload', {});
		$("#searchForm").find('input').val('');
	}
	// 显示图片
	function hoverShow(a_) {
		$("#imgShow>img").attr("src", $(a_).attr('url')).height();
		$("#imgShow").css("left", 10 + event.x).css("top", event.y - 50).show();
		$("#imgShow>img").attr("src", $(a_).attr('url'));
	}
	//取消选中 TreeGrid
	function expandAll(){
		
		var node = datagrid('getSelected');
		if (node) {
			datagrid('expandAll',node.id);
		} else {
			datagrid('expandAll');
		}
	}
	//折叠 TreeGrid
	function collapseAll(){
		var node = datagrid('getSelected');
		if (node) {
			datagrid('collapseAll',node.ID);
		} else {
			datagrid('collapseAll');
		}
	}
	
	gridCommon.init=init;
	gridCommon.del=del;
	gridCommon.edit=edit;
	gridCommon.add=add;
	gridCommon.view=view;
	gridCommon._search=_search;
	gridCommon.cleanSearch=cleanSearch;
	gridCommon.hoverShow = hoverShow;
	gridCommon.expandAll=expandAll;
	gridCommon.collapseAll=collapseAll;
	return gridCommon;
})(window.gridCommon || {});