var centerTabs;
var tabsMenu;
function addTab(title, url) {
	if (centerTabs.tabs('exists', title)) {
		refreshTab(title)
		centerTabs.tabs('select', title);
	} else {
		$.messager.progress({
			text : '正在加载...',
			interval : 100
		});
		window.setTimeout(function() {
			try {
				$.messager.progress('close');
			} catch (e) {
			}
		}, 0);

		var content = '<iframe scrolling="auto" frameborder="0"  src="' + url
				+ '" style="border:0;width:100%;height:99.4%;"></iframe>';
		centerTabs.tabs('add', {
			title : title,
			content : content,
			closable : true,
			tools : [ {
				iconCls : 'icon-mini-refresh',
				handler : function() {
					refreshTab(title);
				}
			} ]
		});
	}
}
function refreshTab(title) {
	var tab = centerTabs.tabs('getTab', title);
	centerTabs.tabs('update', {
		tab : tab,
		options : tab.panel('options')
	});
}

$(function() {
	tabsMenu = $('#tabsMenu').menu(
			{
				onClick : function(item) {
					var curTabTitle = $(this).data('tabTitle');
					var type = $(item.target).attr('type');

					if (type === 'refresh') {
						refreshTab(curTabTitle);
						return;
					}

					if (type === 'close') {
						var t = centerTabs.tabs('getTab', curTabTitle);
						if (t.panel('options').closable) {
							centerTabs.tabs('close', curTabTitle);
						}
						return;
					}

					var allTabs = centerTabs.tabs('tabs');
					var closeTabsTitle = [];

					$.each(allTabs, function() {
						var opt = $(this).panel('options');
						if (opt.closable && opt.title != curTabTitle
								&& type === 'closeOther') {
							closeTabsTitle.push(opt.title);
						} else if (opt.closable && type === 'closeAll') {
							closeTabsTitle.push(opt.title);
						}
					});

					for (var i = 0; i < closeTabsTitle.length; i++) {
						centerTabs.tabs('close', closeTabsTitle[i]);
					}
				}
			});

	centerTabs = $('#centerTabs').tabs({
		fit : true,
		border : false,
		onContextMenu : function(e, title) {
			e.preventDefault();
			tabsMenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			}).data('tabTitle', title);
		}
	});
});