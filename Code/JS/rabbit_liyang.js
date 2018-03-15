/**
 * Rabbit 组件提供针对JBIPP前台的封装
 * 封装包含：
 *      ajax 调用方式, 封装jQuery.ajax方式，提供统一的业务代码处理
 *          Rabbit/r.get(url, data, callback, dataType, async);
 * @Author lnliy
 */
var Rabbit = (function (win) {
	
	var layerDefaultSettings = {
		
	}
	
	var Rabbit = function (selecter) {
		return new Rabbit.fn.init(selecter);
	}
	
	Rabbit.fn = Rabbit.prototype = {
		constructor: Rabbit,
		init: function (selecter) {
			this.selecter = selecter;
			return this;
		}
	}
	
	$.extend(Rabbit, {
        injectionFn: function (plugin) {
            $.extend(this, plugin.prototype);
        },
        serializeObject: function (ser) {
            var o = {};
            var arr = ser.serializeArray();
            $.each(arr, function() {
            	if (this.name.indexOf('.') != -1) {
                    var key = this.name.slice(0, this.name.indexOf('.'));
                    var ikey = this.name.slice(this.name.indexOf('.') + 1, this.name.length);
                    o[key] = o[key] || {};
                    o[key][ikey] = this.value;
				} else {
                    if (o[this.name]) {
                        if (!o[this.name].push) {
                            o[this.name] = [o[this.name]];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
				}
            });
            return o;
        }
    });

	/**
     * 封装http请求的组件
     */
    Rabbit.http = function () {};
    Rabbit.http.prototype = {
        constructor: Rabbit.http,
        ajax: function () {
            if (arguments.length != 1) {
                throw new Error("Rabbit http ajax arguments is must not empty, Please consult jQuery ajax.");
            }
            //调用jQuery代码之前，对success函数进行封装
            var options = arguments[0], callback = options.success;
            options.success = function (res, status, xhr) {
                if (res && res.code == 0) {
                    callback && callback(res, status, xhr);
                } else {
                    layer.msg(res.message || '数据不听话，请联系运维人员处理...', {icon: 5});
                }
            };
            options.error || (options.error = function (res, status, errorThrow) {
                throw new Error('Rabbit request error status is ' + res.status + ' error readyState is ' + res.readyState
                    + 'error message is ' + errorThrow + 'request url is ' + options.url);
            });
            $.ajax(options);
        },
        _common: function (url, data, callback, type, dataType, async) {
            if (type != 'get') {
                data = JSON.stringify(data);
            }
            this.ajax({url: url, data: data, type: type, dataType: dataType || 'json', async: async, success: callback, contentType: 'application/json'});
        }
    }

    $.each(['get', 'post', 'put', 'delete'], function (i, method) {
        Rabbit.http.prototype[method] = function (url, data, callback, type, dataType, async) {
            Rabbit._common(url, data, callback, method, dataType, async);
        }
    });
    Rabbit.injectionFn(Rabbit.http);
    
    /**
     * Rabbit handlerbar模版处理插件
     */
    Rabbit.template = function () {};
    Rabbit.template.prototype = {
    	constructor: Rabbit.template,
    	__common__: function (/*模板内容*/ content, /*渲染数据*/ data) {
    		var template = Handlebars.compile(content);
    		return template({"data": data}); 
    	},
    	compile: function (/*模板ID*/ tmplId, /*渲染数据*/ data) {
    		return this.__common__($(tmplId).html(), data);
    	},
    	render: function (/*jquery样式选择元素*/ jqSelecter, /*模板ID*/ tmplId, /*渲染数据*/ data) {
    		if (data) {
    			$(jqSelecter).html(this.compile(tmplId, data));
    		} else {
    			$(jqSelecter).html('');
    		}
    	},
    	append: function (/*jquery样式选择元素*/ jqSelecter, /*模板ID*/ tmplId, /*渲染数据*/ data) {
    		if (data) {
    			$(jqSelecter).append(this.compile(tmplId, data));
    		} else {
    			$(jqSelecter).append('');
    		}
    	}
    }
    Rabbit.injectionFn(Rabbit.template);
    
    Rabbit.win = function () {};
    Rabbit.win.prototype = {
    	constructor: Rabbit.win,
    	openWin: function (webUrl, settings) {
    		layer.open($.extend({content: [webUrl, 'no']}, settings));
    	},
    	confirmWin: function (title, settings) {
    		layer.confirm(title, settings);
    	}
    }
    Rabbit.injectionFn(Rabbit.win);
    
    /**
     * Rabbit 表格组件
     */
    Rabbit.fn.table = function (options) {
    	if ( !$(this.selecter).length ) {
            if ( options && options.debug && window.console ) {
                console.warn( "Nothing selected, can't table, returning nothing." );
            }
            return;
        }
    	var ele = $(this.selecter)[0];
    	var table = $.data(ele, "table" );
    	if (table) {
            return table;
        }
    	
    	table = new Rabbit.table(options, ele);
    	$.data(ele, "table", table );
    	return table;
    }
    
    Rabbit.table = function (options, element) {
		this.settings = $.extend( true, {}, Rabbit.table.defaults, options);
		this.currEle = element;
		this.init();
		return this;
    }
    
    $.extend(Rabbit.table, {
    	defaults: {
    		title: '',
    		tableId: '',
    		modulesZhName: '',
    		buttonContainer: '.rabbit-button-container',
    		searchFrom: '.rabbit-search',
    		treeView: false,
    		treeId: "id",
    		treeField: "name",
    		treeRootLevel: 1,
    		isTreeClick: false,
    		autoHeight: true,
    		pagination: false,
    		sidePagination: 'client',
    		baseUrl: null,
    		bootstrapTableSettings: {uniqueId: 'id'},
    		winConfig: {
    			common: {},
    			save: {},
    			modify: {},
    			del: {
    				offset: '20%',
				    btn: ['是', '否'],
    			},
    			view: {}
    		}
    	},
    	prototype: {
    		constructor: Rabbit.table,
    		'init': function () {
    			var _t = this, _s = _t.settings;
    			_t.$table = $(_t.currEle).find('#' + _s.tableId);
    			_t.$bc = $(_t.currEle).find(_s.buttonContainer);
    			_t.$from = $(_t.currEle).find(_s.searchFrom);
    			
    			_t.initializationTable();
    			_t.initializationSearch();
    			_t.inittializationWinLinstener();
    		},
    		'initializationTable': function () {
    			var _t = this,_s = this.settings, tableId = _s.tableId, treeView = _s.treeView, treeId = _s.treeId, pagination = _s.pagination, sidePagination = _s.sidePagination,
    					treeField = _s.treeField, treeRootLevel = _s.treeRootLevel, baseUrl = _s.baseUrl; 
    			
    			var bootstrapTable = _t.$table.bootstrapTable($.extend({
    				url: baseUrl + (treeView ? '/tree' : pagination ? '/page' : '/list'),
    			    treeView: treeView,
    			    treeId: treeId,
    			    treeField: treeField,
    			    isTreeClick: _s.isTreeClick,
    			    dataType: 'json',
    			    contentType: "application/x-www-form-urlencoded",
    			    clickToSelect: true,
    			    mobileResponsive: true,
    			    iconSize: 'sm',
    			    sidePagination: sidePagination,
    			    pagination: pagination,
    			    paginationLoop: pagination,
    			    pageList: [10, 15, 25, 50, 100],
    			    striped: true,
    			    treeRootLevel: treeRootLevel,
    			    queryParams: function (params) {
    			    	var paramsData = Rabbit.serializeObject(_t.$from);
    			    	if (pagination) {
    			    		paramsData['pageSize'] = params.limit || 5;
    			    		paramsData['pageIndex'] = (params.offset / params.limit) + 1;
    			    	}
    			    	return paramsData;
    			    },
    			    responseHandler: function (res) {
    			    	if (pagination) {
    			    		return res;
    			    	} 
    			    	return res && (res.result || []);
    			    }
    			}, _s.bootstrapTableSettings));
    			
    			if (tableId && _s.autoHeight) {
    				var options = _t.$table.bootstrapTable('getOptions'), height = options.pagination ? 110 : 100;
    				
    				var calHeight = function () {
    					_t.$table.bootstrapTable('resetView', {
    		                height: $(window).height() - height
    		            });
    				}
    				setTimeout(calHeight, 100);
    		        $(window).on('resize', calHeight);
    		        
    			}
    		},
    		'initializationSearch': function () {
    			var _t = this, _s = _t.settings;
    			_t.$from.on('submit', function () {
    				_t.$table.bootstrapTable('removeAll');
    				_t.$table.bootstrapTable('refresh');
    				return false;
    			});
    		},
    		'inittializationWinLinstener': function () {
    			var _t = this;
    			_t._independentBtnLinstener();
    			_t._tableBtnLinstener();
    		},
    		/**
			 * 单独按钮监听
			 */
    		'_independentBtnLinstener': function () {
    			var _t = this,
					baseUrl = _t.settings.baseUrl,
    				btns = $(_t.currEle).find('.rabbit-btn');
    			
    			btns.on('click', function (e) {
    				var $source = $(this);
    				_t.btnHandler($source);
    			});
    		},
    		/**
			 * 表单下按钮的监听
			 */
    		'_tableBtnLinstener': function () {
    			var _t = this;
    			_t.$table.on('click', function (e) {
    				var $source = $(e.target);
    				if (!$source.hasClass('rabbit-btn')) {
    					$source = $source.parent();
    				}
    				if ($source.hasClass('rabbit-btn')) {
        				_t.btnHandler($source);
    				}
    			});
    		},
    		'btnHandler': function ($source) {
    			var _t = this, 
    				winConfig = _t.settings.winConfig,
    				winWith = $source.attr('win-with'),
    				winUrl = $source.attr('win-url');
    			
    			var btnType = 'view',
    				btns = [],
    				row = null;
    			
    			var selections = [];
    			
    			var title = _t.settings.modulesZhName,
    				baseUrl = _t.settings.baseUrl,
					reqUrl = '', 
					reqMethod = 'get';
    			
    			if ($source.hasClass('rabbit-btn-save')) {
    				btnType = 'save';
    				title = '新增' + title;
    				btns = ['保存', '取消'];
    				reqMethod = 'post';
    			} else if($source.hasClass('rabbit-btn-del')) {
    				btnType = 'del';
    				selections = _t.$table.bootstrapTable('getSelections');
    				if (!(selections && selections.length)) {
    					layer.alert('请选择需要删除的数据', {offset: '20%'});
    					return;
    				}
    				reqMethod = 'delete';
    			} else if($source.hasClass('rabbit-btn-modify')) {
    				btnType = 'modify';
    				title = '修改' + title;
    				btns = ['修改', '取消'];
    				row = _t.$table.bootstrapTable('getRowByUniqueId', $source.attr('data-id'))
    				reqMethod = 'put';
    			} else {
    				row = _t.$table.bootstrapTable('getRowByUniqueId', $source.attr('data-id'))
    				title = (row.name ? row.name : title) + '详情';
    			}
    			
    			var config = winConfig[btnType];
    			
    			if (btnType == 'view' && winConfig['common']['area'] && winConfig['common']['area'] != 'auto') {
    				var height = parseInt(winConfig['common']['area'][1].replace('px', '')) - 33;
    				config['area'] = [winConfig['common']['area'][0], height + 'px'];
    			}
    			
    			reqUrl = baseUrl + '/' + btnType;
    			
    			switch(winWith) {
    			case 'open':
    				Rabbit.openWin(winUrl, $.extend({'btn': btns}, _t.getDefaultSettings(btnType, {'title': title, 'reqUrl': reqUrl, 'reqMethod': reqMethod,'row': row}), winConfig['common'], config));
    				break;
    			case 'confirm':
    				Rabbit.confirmWin("您确认要删除选中数据?", $.extend(_t.getDefaultSettings(btnType, {'selections': selections, 'reqUrl': reqUrl}), config));
    				break;
    			default:
    				break;
    			}
    		},
    		'getDefaultSettings': function (type, data) {
    			var settings = {};
    			switch (type) {
    			case 'del': 
    				settings = this.getDelDefaultSettings(data);
    				break;
    			case 'view': 
    				settings = this.getViewDefaultSettings(data);
    				break;
    			case 'save':
    			case 'modify': 
    			default:
    				settings = this.getOtherDefaultSettings(data);
    				break;
    			}
    			return settings;
    		},
    		'getDelDefaultSettings': function (data) {
    			var _t = this;
    			return {
    				yes: function () {
				    	$.each(data['selections'], function (i, selection) {
				    		Rabbit.delete(data['reqUrl'] + '/' + selection.id, {}, function (res) {
				    			_t.$table.bootstrapTable('removeAll');
				    			_t.$table.bootstrapTable('refresh');
				    			layer.closeAll();
				    			layer.msg('数据删除成功！');
				    		});
				    	});
				    }
    			}
    		},
    		'getViewDefaultSettings': function (data) {
    			
    			var _t = this,
					title = data['title'],
					row = data['row'];
    			
    			return {
    				title: data['title'],
    				type: 2, 
    				area: 'auto', 
    				btnAlign: 'c', 
    				shade: 0.2, 
    				anim: 5, 
    				maxmin: true, 
    				closeBtn: 1,
    				success: function (layero, index) {
    					var win = $(layero).find("iframe")[0].contentWindow, 
							$dom = $(win.document), 
							$body = $dom.find('body'), 
							$form = $dom.find('form');
    					
    					$body.slimScroll({height: '100%', railOpacity: 0.4, wheelStep: 10});
    					
    					win.row = row;
    					
						for (var p in row) {
							var input = $form.find('input[name=' + p + '], select[name=' + p + ']');
							switch(input.attr('type')) {
							case 'radio':
								$.each(input, function (i, item) {
									$item = $(item);
									$item.val() == row[p] ? $item.prop('checked', 'checked'): $item.prop('checked', '');
								});
								break;
							default:
								input.val(row[p]);
								break;
							}
							
							input.attr('disabled', true);
							
						}
						
    				}
    			}
    			
    		},
    		'getOtherDefaultSettings': function (data) {
    			
    			var _t = this,
    				title = data['title'],
    				reqMethod = data['reqMethod'],
    				reqUrl = data['reqUrl'],
    				row = data['row'];
    			
    			return {
    				title: data['title'],
    				type: 2, 
    				area: 'auto', 
    				btnAlign: 'c', 
    				shade: 0.2, 
    				anim: 5, 
    				maxmin: true, 
    				closeBtn: 1,
    				success: function(layero, index){
    					var win = $(layero).find("iframe")[0].contentWindow, 
    						$dom = $(win.document), 
    						$body = $dom.find('body'), 
    						$form = $dom.find('form'),
    		                method = $form.attr('method') || reqMethod, 
    		                url = $form.attr('action') || reqUrl;
    					
    					$body.slimScroll({height: '100%', railOpacity: 0.4, wheelStep: 10});
    					var formValidate = $form.validate();
    					
						win.row = row;
						for (var p in row) {
							var input = $form.find('input[name=' + p + ']');
							switch(input.attr('type')) {
							case 'radio':
								$.each(input, function (i, item) {
									$item = $(item);
									$item.val() == row[p] ? $item.prop('checked', 'checked'): $item.prop('checked', '');
								});
								break;
							default:
								input.val(row[p]);
								break;
							}
						}
    					
    		            $form.on('submit', function (e) {
    		                if (formValidate.valid()) {
    		                    Rabbit[method](url, Rabbit.serializeObject($form), function (res) {
    		                    	_t.$table.bootstrapTable('removeAll');
    		                        _t.$table.bootstrapTable('refresh');
    		                        layer.close(index);
    		                    });
    		                }
    		                return false;
    		            });
    				},
    				yes: function (index, layero) {
                        var dom = $(layero).find("iframe")[0].contentWindow.document,
                            $dom = $(dom), $form = $dom.find('form');
                        $form.trigger('submit');
                    }
    			};
    		},
    		winConfig: function (key, settings) {
    			this.settings.winConfig[key] = settings;
    			return this;
    		}
    	}
    });
    
	Rabbit.fn.init.prototype = Rabbit.fn;
    window.R = Rabbit;

    return Rabbit;
	
})(window);