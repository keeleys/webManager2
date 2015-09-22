//定义默认的操作 调用之前先给变量 model_name赋值
function ooip(url,title){
		$.messager.progress({text:'数据加载中....',interval:100});
		$('#Dialog_').dialog({
		    title: '查看-'+model_title,   
		    width: 800,   
		    top:0, 
		    cache: false,
		    maximizable:true,
		    href: url,   
		    modal: true,
		    onLoad:function(){
		    	$.messager.progress('close');
		    },
		    onLoadError:function(){
		    	$.messager.progress('close');
		    },
			buttons:[ {
				text:"关闭",
				handler:function() {
					$('#Dialog_').dialog('close');
				}
			}]
		});
	}

function del() {
	var rows = datagrid.datagrid('getSelections');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : model_name+"/del",
					data : {
						ids : ids.join(',')
					},
					dataType : 'json',
					success : function(r) {
						datagrid.datagrid('load');
						//datagrid.treegrid('load');
						datagrid.datagrid('unselectAll');
						if (r.success) {
							$.messager.show({
								msg:r.msg,
								title:'提示'
							});
							editRow = undefined;
						} else {
							$.messager.alert("操作错误",r.msg,'error');
						}
					}
				});
			}
		});
	} else {
		$.messager.alert('提示', '请选择要删除的记录！', 'error');
	}
}
//编辑
function edit() {
	var rows = datagrid.datagrid('getSelections');
	if (rows.length == 1) {
		add(rows[0].id);
	} else {
		$.messager.alert('提示', '请选择一条记录！', 'error');
	}
}

//新增
function add(id_) {
	$.messager.progress({
	text:'数据加载中....',
	interval:100
	});
	$('#Dialog_').dialog({
	    title:(id_==undefined?"新增":"编辑")+"-"+model_title,   
	    cache: false,
	    maximizable:true,
	    resizable:true,
	    width:800,
	    top:0,
	    href: model_name+'/edit/'+(id_==undefined?'':id_),   
	    modal: false,
	    onLoad:function(){$.messager.progress('close');
	    	try{
	    	editor=KindEditor.create('.keditor', {
				themeType : 'default',
				uploadJson : '/file/up',
				fileManagerJson : '/file/manager',
				allowFileManager : false
				});
	    	}catch(e){}
	    	},
		buttons:[ {
			text:id_==undefined?"保存":"修改",
			iconCls:'icon-save',
			handler:function() {
				editForm = $('#editForm').form({
					url:model_name+'/save',
					success:function(data) {
						var json = $.parseJSON(data);
						if (json && json.success) {
							$.messager.show({
								title:'成功',
								msg:json.msg
							});
							datagrid.datagrid('reload');
							//datagrid.treegrid('reload');
							$("#poid").val(json.obj);
							$('#Dialog_').dialog('close');
						} else {
							$.messager.show({
								title:'失败',
								msg:json.msg
							});
						}
					}
				});
				if(editor!=undefined){
					editor.sync();//ajax提交时需要执行此方法
					editor.sync();
				}
				editForm.submit();
				}
			},
			{
			text:"关闭",
			iconCls:'icon-cancel',
			handler:function() {
				$('#Dialog_').dialog('close');
			}
		}]
	}); 
}
//查看
function view(id_,title){
	if(id_==undefined){
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			id_=rows[0].id;
		}else{
			$.messager.alert('提示', '请选择一条记录！', 'error');
			return;
		}
		ooip(model_name+"/view/"+id_,title);
	}
}


//查询
function _search() {
	datagrid.datagrid('load', sy.serializeObject($("#searchForm")));
	//datagrid.treegrid('load', sy.serializeObject($("#searchForm")));
}
//清除查询
function cleanSearch() {
	datagrid.datagrid('load', {});
	//datagrid.treegrid('reload', {});
	$("#searchForm").find('input').val('');
}
//显示图片
function hoverShow(a_){
	$("#imgShow>img").attr("src",$(a_).attr('url')).height();
	$("#imgShow").css("left",10+event.x).css("top",event.y-50).show();
	$("#imgShow>img").attr("src",$(a_).attr('url'));
}