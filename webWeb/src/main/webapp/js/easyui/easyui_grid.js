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
		view();
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
//