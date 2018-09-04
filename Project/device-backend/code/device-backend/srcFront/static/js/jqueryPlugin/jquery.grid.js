/*!
 *	[定义模块 - 表格模块]
 *
 *	@ 依赖：jquery
 *
 *	@ Version 1.0
 *	@ Author Gang
 *	@ Latest Update 2018-02-12
 */

define(['jquery'],function($){

	'use strict';

	var Grid;

	// Internal methods
	var _methods = {
		
		_init: function(settings){
			// console.log(this);

			this._initGrid(settings);

			if (settings.autoLoad){
				this._loadPage(settings);
			}
		},

		_initGrid: function(settings){

			var that = this, $grid = settings.$grid;

			// Init dom
			$grid.wrap('<div class="table-wrap data-clear"></div>');

			// multi-check
			if (settings.checkbox){
				that._initCheckbox(settings);
			}

			// Trigger tr
			that._triggerTr(settings);
		},

		_triggerTr: function(settings){

			var that = this, ss =settings, $grid = settings.$grid;

			$grid.on('click','tbody tr',function(){
				var cls = 'tr-selected';
				var isSelected = $(this).hasClass(cls);
				$grid.find('.'+cls).removeClass(cls);
				if (!isSelected){
					$(this).addClass(cls);
				}
				else {
					$(this).removeClass(cls);
				}
			});
		},

		_draw: function(settings){

			var that = this,
				ss = settings,
				rowsData = settings.gridData,
				len = rowsData.length,
				columns = settings.columns ? settings.columns : [],
				$grid = settings.$grid,
				$newTbody = $('<tbody></tbody>');

			// Build trs
			if (len){
				for (var i=0; i<len; i++){

					var $tr = $('<tr></tr>');

					// Checkbox
					if (ss.checkbox){

						var isCheck = that._initRowCheck(rowsData[i][ss.checkboxField],settings);

						$tr.append('<td class="td-checkbox"><span class="checkbox-inline"><input name="'+ss.checkboxField+'" type="checkbox" value="'+rowsData[i][ss.checkboxField]+'" '+(isCheck ? 'checked' : '')+' /><i></i></span></td>');
					}

					// Build tds
					for (var j=0,colLen=columns.length; j<colLen; j++){

						var $td = $('<td></td>'),
							cls = columns[j]['class'],
							hidden = columns[j]['hidden'],
							// onCheck = columns[j]['onCheck'],
							render = columns[j]['render'],
							content = rowsData[i][columns[j]['data']];

						// Filter inject char
						content = Object.prototype.toString.call(content) === '[object String]' ? content.replace(/</ig,'&lt;').replace(/>/ig,'&gt;') : content;

						// td hidden
						hidden && $td.css('display','none');

						// td class
						cls && $td.addClass(cls);

						// td content - render
						if (render && Object.prototype.toString.call(render) === '[object Function]'){

							$td.html(render(content,rowsData[i],i+1,settings));
							$tr.append($td);
							continue;
						}

						// td content - text
						content && $td.html(content);
						$tr.append($td);
					}

					// tr appendTo tbody
					$newTbody.append($tr);
				}
			}
			else {
				$newTbody.append('<tr class="nodata"><td colspan="'+$grid.find('thead th').length+'">无数据</td></tr>')
			}

			// Delete old tbody
			var $tbody = $grid.find('tbody');
			if ($tbody.length){
				$tbody.remove();
			}
			
			// Append to dom
			$grid.append($newTbody);

			settings.$gridBody = $newTbody;

			// Trigger Event -  onDraw
			settings.onDraw && settings.onDraw(settings);
		},

		// Handler page no
		_handlePageNo: function(pageNo,settings){
			return settings.gridDataTotal ? (pageNo <= 0 && 1 || pageNo > settings.pages && settings.pages || pageNo) : 1;
		},

		// Handler Data
		_handleData: function(settings){

			var that = this, 
				ss = settings,
				data = Object.prototype.toString.call(ss.data) === '[object Object]' ? ss.data : {};

			// handle page
			if (data.page){
				if ($.isNumeric(data.page)){ ss.page = data.page; }
				delete ss.data.page;
			}

			// handle pageSize
			if (data.pageSize){
				if ($.isNumeric(data.pageSize)){ ss.pageSize = data.pageSize; }
				delete ss.data.pageSize;
			}

			// Set settings - send data
			ss.dataSend = $.extend(ss.isPaging ? {page: ss.page, pageSize: ss.pageSize } : {},ss.data);
		},

		// Hanlder data after ajax success
		_handleSuccessData: function(json,settings){

			var that = this, 
				ss = settings,
				dataHash = {};

			// Set settings - grid data, total
			ss.gridData = json.data ? json.data : [];
			ss.gridDataTotal = json.total ? json.total : 0;

			// Set settings - pages, page
			ss.pages = Math.ceil(ss.gridDataTotal / ss.pageSize);
			ss.page = that._handlePageNo(ss.page,settings);
			ss.pages = ss.pages ? ss.pages : 1;

			// Update data hash
			if (ss.isPaging){
				if (ss.page > 1){ dataHash.page = ss.page; }
				if (ss.pageSize != ss.pageSizeDefault){ dataHash.pageSize = ss.pageSize; }
			}
			$.extend(dataHash,ss.data);

			// Set settings - hash data
			ss.dataHash = dataHash;
		},

		// Add Loading
		_addLoading: function(settings){

			var $wrap = settings.$grid.parent(),
				$loading = $('<div class="table-loading"><div class="table-mask"></div><span class="loading"><i class="icon-loading"></i></span></div>');

			$wrap.append($loading);
		},

		// Clear Loading
		_delLoading: function(settings){

			settings.$grid.parent().find('.table-loading').remove();
		},

		// Get data
		_ajaxData: function(settings,fn){

			var that = this, 
				ss = settings;

			ss.ajax && ss.ajax.abort();

			// Set settings - ajax data
			ss.ajax = $.ajax({
				url: ss.url,
				data: ss.dataSend,
				type: ss.type,
				cache: false,
				dataType: 'json',
				beforeSend: function(xhr){
					
					that._addLoading(ss);
					ss.onBeforeSend && ss.onBeforeSend(xhr);				// Trigger Event -  onBeforeSend
				},
				success: function(json, textStatus, xhr){

					// Clear loading
					that._delLoading(ss);

					var json = Object.prototype.toString.call(json) === '[object Object]' ? json : {};
					
					that._handleSuccessData(json,settings);					// Hanlder data after ajax success	
					
					ss.onSuccess && ss.onSuccess(ss.dataHash,json,xhr);		// Trigger Event -  onSuccess

					// Callback
					if (json.data && Object.prototype.toString.call(json.data) === '[object Array]'){
						fn && fn(json);
					}

					ss.ajax = null;
				},
				error: function(xhr, textStatus){
					
					// Clear loading
					that._delLoading(ss);
					
					if (xhr.statusText != 'abort'){
						ss.onError && ss.onError(xhr, textStatus);	// Trigger Event -  onError
					}
					ss.ajax = null;
				}
			})

		},

		// Load grid page
		_loadPage: function(settings, pageNo){

			var that = this, ss = settings;

			// Handle page no.
			if (pageNo){
				ss.page = that._handlePageNo(pageNo,ss);
			}

			// Handle data
			that._handleData(ss);

			ss.$grid.parent().removeClass('data-clear');

			// Ajax data
			that._ajaxData(ss,function(json){

				// console.log('[ajax success]',ss);

				// If - clear all checked, unchecked data
				if (ss.checkbox && ss.checkRange == 'page'){
					that._clearCheckData(ss);
				}

				// Draw grid
				that._draw(ss);

				// Reset page checkbox
				if (ss.checkbox){

					that._setPageCheck(settings);
				}

				// Create pagination
				if (ss.isPaging){
					!ss.initPagination && that._initPagination(ss);		// Init pagination Dom	
					that._setPageInfo(ss);								// Set page info
					that._setPageStatus(ss);							// Set paging		
				}
			});
		},

		// Init pagination dom
		_initPagination: function(settings){

			var that = this,
				ss = settings,
				$pgs = $('div[data-grid="'+ss.id+'"]');

			if (!$pgs.length){
				$pgs = $('<div data-grid="'+ss.id+'" data-total data-go data-jump></div>'); 	// New one
				ss.$grid.after($pgs);
			}

			// Init dom
			$pgs.each(function(){

				var $self = $(this),
					data = $self.data(),
					jump = [
						'<div class="page-jump">',
							'<input type="text"/>',
							'<div class="page-jump-info">/<span></span></div>',
							''+(data.go != undefined ? '<button type="button" class="btn default"><i class="icon-search icon-only"></i>Go</button>' : '')+'',
						'</div>'
					],
					$bd = $([
						'<div class="table-page">',
							'<div class="inner">',
								''+(data.total != undefined ? '<div class="page-data-total"></div>' : '')+'',
								''+(data.info != undefined ? '<div class="page-info"></div>' : '')+'',
								'<div class="paging">',
									// '<a href="javascript:void(0);" class="first" title="首页"><i class="fa fa-angle-left"></i></a>',
									'<a href="javascript:void(0);" class="prev" title="上一页"><i class="fa fa-angle-left"></i></a>',
									'<a href="javascript:void(0);" class="next" title="下一页"><i class="fa fa-angle-right"></i></a>',
									// '<a href="javascript:void(0);" class="last" title="末页"><i class="fa fa-angle-right"></i></a>',
								'</div>',
								''+(data.jump != undefined ? jump.join(''): '')+'',
						'</div>'
					].join(''));

				$self.append($bd);
			});

			// Set settings - Init page status
			ss.pageStatus = {first: false, last: false};

			// Init event - first, prev, next, last
			$pgs.on('click','.first',function(){
				if (!ss.pageStatus.first){ that._loadPage(ss, 1); }
			});

			$pgs.on('click','.prev',function(){
				if (!ss.pageStatus.first){ that._loadPage(ss, ss.page - 1); }
			});

			$pgs.on('click','.next',function(){
				if (!ss.pageStatus.last){ that._loadPage(ss, ss.page + 1); }
			});

			$pgs.on('click','.last',function(){
				if (!ss.pageStatus.last){ that._loadPage(ss, ss.pages); }
			});

			// Init page jump event
			$pgs.on('input keyup','.page-jump input',function(e){

				var $self = $(this);
				var val = $self.val().replace(/[^0-9]/g,'').replace(/^0([0-9]*)/,'$1');
				$self.val(val);

				if (e.keyCode == 13 && val){
					that._loadPage(ss, val);
				}
			});

			$pgs.on('click','.page-jump button',function(){
				var val = $(this).parent().find('input').val();
				that._loadPage(ss, val ? val : 1);
			});

			// Set settings - pagination objects
			ss.$pgs = $pgs;

			// Set settings - flag
			ss.initPagination = true;
		},

		// Set page info and jump
		_setPageInfo: function(settings){

			var $pgs = settings.$pgs, ss = settings, p = ss.page;

			$pgs.find('.page-data-total').html(ss.gridDataTotal+' 条');
			$pgs.find('.page-info').html(p+'/'+ss.pages+'');
			$pgs.find('.page-jump-info span').html(ss.pages);
			$pgs.find('.page-jump input').val(p ? p : '').width(p ? (''+p+'').length*8+20 : 30);
		},

		// Set page status
		_setPageStatus: function(settings){

			var ss = settings,
				first = ss.page <= 1,
				last = ss.page >= ss.pages,
				$pgs = ss.$pgs;

			// Update dom status
			$pgs.find('.first').toggleClass('disabled',first);
			$pgs.find('.prev').toggleClass('disabled',first);
			$pgs.find('.next').toggleClass('disabled',last);
			$pgs.find('.last').toggleClass('disabled',last);

			// Set settings - page status
			ss.pageStatus = {first: first, last: last};
		},


		// Init row check
		_initRowCheck: function(rowId, settings){

			var ss = settings;
			var isCheck = false;

			if (ss.checkAll){
				if (!ss._.unchecked[rowId]){
					isCheck = true;
				}
			}
			else {
				if (ss._.checked[rowId]){
					isCheck = true;
				}
			}

			return isCheck;
		},

		// Set page check all input
		_setPageCheck: function(settings){

			var that = this,
				ss = settings,
				selector = 'input[name="'+ss.checkboxField+'"]:not(:disabled)',
				selectorChecked = 'input[name="'+ss.checkboxField+'"]:not(:disabled):checked';

			$('#'+ss.pageCheckID).prop('checked',(ss.$grid.find(selector).length == ss.$grid.find(selectorChecked).length) ? true : false).removeAttr('disabled');

		},

		_clearCheckData: function(settings){

			var ss = settings;

			ss._.checked = {};
			ss._.checkedData = [];
			ss._.unchecked = {};
			ss._.uncheckedData = [];
		},


		// Init checkbox
		_initCheckbox: function(settings){

			var that = this,
				ss = settings,
				$grid = ss.$grid,
				$pageCheck = $('<input type="checkbox" disabled title="全选/反选（当前页）"/>'),
				$th = $('<th class="th-multiCheck"><span class="checkbox-inline"><i></i></span></th>'),
				selector = 'input[name="'+ss.checkboxField+'"]:not(:disabled)';


			// Dom - append th checkbox
			$th.find('span').prepend($pageCheck);
			$grid.find('thead tr').prepend($th);

			// Add ID
			ss.pageCheckID = ss.id+'-page-checkall';
			ss.$pageCheck = $pageCheck;

			// Page check all trigger
			$pageCheck.attr('id',ss.pageCheckID).on('click',function(){

				var isCheck = $(this).prop('checked');

				// Dom
				$grid.find(selector).prop('checked',isCheck ? true : false);

				// Set check data
				if (ss.gridData){
					that._setPageCheckData(isCheck, ss.gridData, settings);
				}

				// Callback
				ss.onCheck && ss.onCheck(that._getCheckData(ss));
			});


			// Row check trigger
			$grid.on('click',selector,function(e){

				// Dom
				that._setPageCheck(ss);
				e.cancelBubble = true;
                e.stopPropagation();

				var $this = $(this);
				var isCheck = $this.prop('checked');
				var index = $this.closest('tr').index();

				// Set settings - selected data
				that._setRowCheckData(isCheck, index, ss);

                // Callback
                ss.onCheck && ss.onCheck(that._getCheckData(ss));
			});

			// Tr check
			// $grid.on('click','tbody tr',function(e){
			// 	$(this).find(selector).trigger('click');
			// });
		},

		_setPageCheckData: function(isCheck, data, settings){

			var that = this;
			var ss = settings;

			for (var i=0, len=data.length; i<len; i++){
				that._setRowCheckData(isCheck, i, settings);
			}
		},

		// Add selected data
		_setRowCheckData: function(isCheck, rowIdx, settings){

			var ss = settings,
				checked = ss._.checked,
				unchecked = ss._.unchecked,
				item = ss.gridData[rowIdx],
				key = item[ss.checkboxField];

			// 默认全选
			if (ss.checkAll){

				// 记录未选数据
				if (isCheck && unchecked[key]){ delete unchecked[key]; }
				if (!isCheck && !unchecked[key]){ unchecked[key] = $.extend(true,{},item); }
			}

			// 默认反选
			else {

				// 记录已选数据
				if (isCheck && !checked[key]){ checked[key] = $.extend(true,{},item); }
				if (!isCheck && checked[key]){ delete checked[key]; }
			}

			// console.log('checked',checked);
			// console.log('unchecked',unchecked);
		},

		_getCheckData: function(settings){

			var ss = settings;
			var checked = settings._.checked;
			var data = [];
			var unchecked = settings._.unchecked;
			var data2 = [];

			for (var key in checked){
				data.push(checked[key]);
			}

			for (var key2 in unchecked){
				data2.push(unchecked[key2]);
			}

			// console.log('_getSelected',data);
			return {checkAll: ss.checkAll, total: ss.gridDataTotal, checked: data, unchecked: data2};
		},

		_getSelectedIDS: function(settings){

			var ss = settings,
				$checked = ss.$grid.find('input[name="'+ss.checkboxField+'"]:not(:disabled):checked'),
				res = [];
			
			$checked.each(function(){
				res.push($(this).val());
			});

			return res.join(',');
		},

		_setSelectMode: function(isCheckAll, settings){

			var that = this;
			var ss = settings;
			var selector = 'input[name="'+ss.checkboxField+'"]:not(:disabled)';

			if (ss.checkAll == isCheckAll){ return; }

			ss.checkAll = isCheckAll;

			// Reset check data
			that._clearCheckData(ss);

			// Set dom check
			ss.$grid.find(selector).prop('checked', isCheckAll);
			$('#'+ss.pageCheckID).prop('checked', isCheckAll);
		},


		_clearGrid: function(settings){

			var that = this,
				ss = settings;

			ss.$grid.parent().addClass('data-clear');
			ss.$gridBody && ss.$gridBody.remove();
			ss.$pageCheck.prop('disabled',true);
			ss.$pgs && ss.$pgs.find('.table-page').remove();

			ss.initPagination = false;
			ss.page = 1;
			ss.pages = 0;
		}
	};

	// Export methods
	var _ext = {

		getSelectedIDS: function(){

			return $.fn.grid._methods._getSelectedIDS(this.settings);
		},

		setSelectMode: function(isCheckAll){

			return $.fn.grid._methods._setSelectMode(isCheckAll, this.settings);
		},

		getCheckData: function(){

			return $.fn.grid._methods._getCheckData(this.settings)
		},

		getTotal: function(){

			return this.settings.gridDataTotal;
		},

		refresh: function(){

			$.fn.grid._methods._loadPage(this.settings);
		},

		clear: function(){

			$.fn.grid._methods._clearGrid(this.settings);
		},

		load: function(params){

			var that = this , ss = that.settings;

			ss.page = 1;
			ss.data = {};

			// Reset checkbox
			$.fn.grid._methods._clearCheckData(ss);
			// ss.checkAll = ss._checkAll;

			// Check params
			if (Object.prototype.toString.call(params) === '[object Object]'){
				ss.data = params;
			}

			$.fn.grid._methods._loadPage(ss);
		}

	};

	Grid = function(options){

		var that = this;
		var defaults = {
			autoLoad: true,
			url: '',
			type: 'get',
			data: '',
			gridDataTotal: 0,

			checkbox: false,
			checkboxField: 'id',
			checkAll: false,
			checkRange: 'page',

			isPaging: true,
			pageSizeDefault: 15,
			pageSize: 15,
			page: 1,
			pages: 1
		};
		var settings = $.extend(defaults,options);
		
		// Init properties
		that.settings = settings;
		that.settings.$grid = $(that);
		that.settings.id = $(that).attr('id');
		that.settings._ = {
			checked: {},
			checkedData: [],
			unchecked: {},
			uncheckedData: []
		};

		that.settings._checkAll = that.settings.checkAll;

		// Extend export methods
		$.extend(that,_ext);

		// Init
		$.fn.grid._methods._init(that.settings);

		return that;
	};

	// Set Grid internal methods
	Grid._methods = _methods;

	// Set Grid version
	Grid.version = '1.0';

	// Jquery extend method
	$.fn.grid = Grid;

});
