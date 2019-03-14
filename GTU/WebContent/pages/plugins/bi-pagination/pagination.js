(function($) {
	
	var pluginName = 'pagination';
	
	$.fn[pluginName] = function(options, pageable) {
		
		var container = $(this);
		var attributes = $.extend({}, $.fn[pluginName].defaults, options);
		var pageable = $.extend({}, $.fn[pluginName].pageable, pageable);
		var pagination = {
			model: {},
			generateHTML: function() {
		        var self = this;
		        var currentPage = self.getCurrentPage();
		        var totalPage = self.getTotalPage();
		        var totalRow = self.getTotalRow();
				var html = '';
				html += '<ul class="pagination">';
				html += '<li class="First-left-etc firstPage"> <a href="javascript: void(0)"> </a> </li>';
				html += '<li class="left-etc prePage"><a href="javascript: void(0)"> </a></li>';
				html += '<li>';
				html += '<select class="page">';
				if (totalPage) {
					$.each(new Array(totalPage), function(i) {i++;html += '<option '+(currentPage == i ? 'selected' : '')+'>'+(i)+'</option>';});
				} else {
					html += '<option>0</option>';
				}
				html += '</select>';
				html += '</li>';
				html += '<li class="right-etc nextPage"><a href="javascript: void(0)"></a></li>';
				html += '<li class="Last-right-etc lastPage"><a href="javascript: void(0)"></a></li>';
				html += '</ul>';
				html += '<span class="page-label">第<span class="currentPage">'+currentPage+'</span>頁/共<span class="totalPage">'+totalPage+'</span>頁，共<span class="totalRow">'+totalRow+'</span>筆</span>';
		        return html;
			},
			observer: function() {
				var self = this;
		        var model = self.model;
		        var el = self.el;
		        // Page number select click
		        el.delegate('.page', 'change', function() {
		        	var pageNumber = parseInt($(this).val());
		        	pageable.page = --pageNumber;
		        	self.dataCallback();
		        });
		        
		        // Previous button click
		        el.delegate('.prePage', 'click', function() {
		        	var currentPage = self.getCurrentPage();
					if (currentPage > 1) {
						pageable.page = currentPage - 2;
						self.dataCallback();
					}
		        });
		        
		        // Next button click
		        el.delegate('.nextPage', 'click', function() {
		        	var currentPage = self.getCurrentPage();
					var totalPage = self.getTotalPage();
					if (currentPage < totalPage) {
						pageable.page = currentPage;
						self.dataCallback();
					}
		        });
		        
		        // First button click
		        el.delegate('.firstPage', 'click', function() {
		        	var currentPage = self.getCurrentPage();
					if (currentPage > 1) {
						pageable.page = 0;
						self.dataCallback();
					}
		        });
		        
		        // Last button click
		        el.delegate('.lastPage', 'click', function() {
		        	var currentPage = self.getCurrentPage();
					var totalPage = self.getTotalPage();
					if (currentPage < totalPage) {
						pageable.page = --totalPage;
						self.dataCallback();
					}
		        });
			},
			getCurrentPage: function() {
				var val = this.getTotalPage() > 0 ? pageable.number+1 : pageable.number;
				return val || 0;
			},
			getTotalRow: function() {
				return pageable.totalElements || 0;
			},
			getTotalPage: function() {
				return pageable.totalPages || 0;
			},
			dataCallback: function() {
				var self = this;
				attributes.callback(pageable, self.render);
			},
			build: function() {
				var self = this;
				var el = $('<div class="paginationjs"></div>');
				self.el = el;
				el.html(self.generateHTML());
				// Clean pagination element in the container
				self.destory();
				// Append pagination element to the container
				container['append'](el);
				// Bind events
				self.observer();
			},
			destory: function() {
				var el = this.el;
				$('.paginationjs', container).remove();
			},
			render: function(result) {
				var self = pagination;
				pageable = $.extend(pageable, {
					'number': result.number,
					'totalElements': result.totalElements,
					'totalPages': result.totalPages,
					'page': result.number,
					'size': result.size
				});
				self.build();
			},
			initialize: function() {
				var self = this;
				
		        if (attributes.pagingOnInit) {
		        	self.dataCallback();
		        } else {
		        	self.build();
				}
			}
		};
		
		pagination.initialize();
		
		return this;
	};
	
	$.fn[pluginName].defaults = {
		// Init load
		'pagingOnInit': true,
		// Pagination callback
		'callback': function() {}
	};
	
	$.fn[pluginName].pageable = {
		// Default page
		'page': 0,
		// Entries of per page
		'size': 10,
		// Sort
		'sort': [],
	};
	
})(jQuery);