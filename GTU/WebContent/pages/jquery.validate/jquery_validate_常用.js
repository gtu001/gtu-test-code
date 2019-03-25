44var formCommonValidate = new function() {
	$.fn["getDate"] = function() {
		var value = $(this).val();
		var re = /^\d{4}\/\d{1,2}\/\d{1,2}$/;
		if (re.test(value)) {
			var adata = value.split('/');
			var yyyy = parseInt(adata[0], 10);
			var dd = parseInt(adata[2], 10);
			var mm = parseInt(adata[1], 10);
			var xdata = new Date(yyyy, mm - 1, dd);
			if ((xdata.getFullYear() === yyyy) && (xdata.getMonth() === mm - 1)
					&& (xdata.getDate() === dd)) {
				return xdata;
			}
		}
		return null;
	};

	return {
		applyForm : function(form) {
			$(".required", form).each(function() {
				$(this).append("<span style=\"color: red\">*</span>");
			});
			
			// 共通設定 -----------------------------
			jQuery.validator.setDefaults({
				highlight : function(element) {
					jQuery(element).closest('.form-group').addClass('has-error');
				},
				unhighlight : function(element) {
					jQuery(element).closest('.form-group').removeClass('has-error');
				},
				errorElement : 'span',
				errorClass : 'label label-danger',
			});
			// 共通驗證 -----------------------------
			jQuery.validator.addMethod("dateFormatChk", function(value, element) {
				return this.optional(element) || ($(element).getDate() != null ? true : false);
			}, "日期格式錯誤");


			jQuery.validator.addMethod("dateFormatChk_between", function(value,
					element, options) {
				var begin = $(options[0]).getDate();
				var end = $(options[1]).getDate();
				if (begin == null || end == null) {
					return true;
				} else if (begin != null && end != null && begin <= end) {
					return true;
				}
				return false;
			}, "開始日期必須小於結束日期");
			
			jQuery.validator.addMethod("isAnyInputChk", function(value,
					element, options) {
				for(var ii in options){
					if($.trim($(options[ii]).val()) != ''){
						return true;
					}
				}
				return false;
			}, "至少輸入一個欄位");
		}
	}
};
