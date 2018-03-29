Vue
		.component(
				'date-picker',
				{
					template : '<input type="text" class="form-control" v-bind:hasMinDate="minDate" />',
					props : [ 'minDate' ],
					mounted : function() {
						var self = this;
						var ops = {
							dayNames : [ "星期日", "星期一", "星期二", "星期三", "星期四",
									"星期五", "星期六" ],
							dayNamesMin : [ "日", "一", "二", "三", "四", "五", "六" ],
							monthNames : [ "一月", "二月", "三月", "四月", "五月", "六月",
									"七月", "八月", "九月", "十月", "十一月", "十二月" ],
							monthNamesShort : [ "一月", "二月", "三月", "四月", "五月",
									"六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ],
							prevText : "上月",
							nextText : "次月",
							weekHeader : "週",
							showMonthAfterYear : true,
							dateFormat : "yy-mm-dd",
							onSelect : function(date) {
								//alert("onSelect : " + date);
								self.$emit('update-date', date);
							},
							change : function(date){
								//alert("change : " + date);
								self.$emit('update-date', date);
							}
						};

						// 設定最小日
						var _hasMinDate = $(this.$el).attr("hasMinDate");
						if (_hasMinDate || _hasMinDate == "true") {
							$.extend(ops, {
								minDate : new Date()
							});
						}

						$(this.$el).datepicker(ops);
						/*
						$(this.$el).blur(function() {
							var dateElement = $(this);
							setTimeout(function() {
								var dateStr = $(dateElement).datepicker("getDate");
								alert("blur : " + dateStr);
								self.$emit('update-date', dateStr);
							}, 1);
						});
						*/
					},
					beforeDestroy : function() {
						$(this.$el).datepicker('hide').datepicker('destroy');
					}
				});

function dataURLtoBlob(dataurl) {
	var arr = dataurl.split(',');
	var mime = arr[0].match(/:(.*?);/)[1];
	var bstr = atob(arr[1]);
	var n = bstr.length;
	var u8arr = new Uint8Array(n);
	while (n--) {
		u8arr[n] = bstr.charCodeAt(n);
	}
	return new Blob([ u8arr ], {
		type : mime
	});
}

var app = new Vue(
		{
			el : '#app',
			data : {
				businesses : Business,
				groups : Group,
				campaigns : Campaign,
				step : 1,
				adBlock : '',
				adName : '',
				startDate : null,
				endDate : null,
				imgAlt : '',
				imgUrl : '',
				image : '',
				imageName : '',
				img2Alt : '',
				img2Url : '',
				image2 : '',
				image2Name : '',
				adGroup : [],
				isDefault : '',
				auditLevel : '',
				blockphoto : '',
				nowAdImages : [],
				imageSize : null,
				image2Size : null,
				newAdMode : true,
			},
			methods : {
				submitPage : function(e) {
					if (this.step == 2) {
						var File1 = document.getElementById("File1").value;
						var File2 = ''; // document.getElementById("File2").value;
						var img1Alt = document.getElementById("img1Alt").value;
						var img1Url = document.getElementById("img1Url").value;
						var img2Alt = ''; // document.getElementById("img2Alt").value;
						var img2Url = ''; // document.getElementById("img2Url").value;
						if (File1 != '') {
							/*
							if (img1Alt == '' || img1Url == '') {
								alert('圖片的廣告描述或鏈結網址未寫入');
								return false;
							}
							*/
							if (img1Alt == '') {
								alert('圖片的廣告描述未寫入');
								return false;
							}
						}

						File2 = '';
						if (File2 != '') {
							if (img2Alt == '' || img2Url == '') {
								alert('圖片2的廣告描述或鏈結網址未寫入');
								return false;
							}
						}
					}
					if (this.step == 3) {
						var customerCheck = ($("input[name='Choosecustomer']:checked").length);
						if (customerCheck == 0) {
							alert('選擇客群未勾選');
							return false;
						}
					}

					var info = new FormData();
					info.adBlock = this.adBlock;
					info.adName = this.adName;
					info.startDate = this.startDate;
					info.endDate = this.endDate;
					info.imgAlt = this.imgAlt;
					info.imgUrl = this.imgUrl;
					info.image = this.image;
					info.imageName = this.imageName;
					info.img2Alt = this.img2Alt;
					info.img2Url = this.img2Url;
					info.image2 = this.image2;
					info.image2Name = this.image2Name;
					info.adGroup = this.adGroup;
					info.isDefault = this.isDefault;
					info.newAdMode = this.newAdMode;
					setValue(info);

				},
				nextPage : function(e) {
					var step = this.step;

					var CheckAdRadio = checkadBlockRadio();
					if (!CheckAdRadio && step < 2) {
						alert('選擇區塊未勾選');
						return;
					}
					if (step < 2) {
						var adName = document.getElementById("adName").value;
						if (adName == '') {
							alert('廣告名稱需輸入');
							return;

						}
						var startDate = $('#startDate').val();
						var endDate = $('#endDate').val();
						var tempStartDate = StartEnd[0];
						var tempEndDate = StartEnd[1];
						if (tempStartDate == ''
								|| (tempStartDate != startDate && startDate != ''))
							changeStart(startDate);
						if (tempEndDate == ''
								|| (tempEndDate != endDate && endDate != ''))
							changeEnd(endDate)
						startDate = StartEnd[0];
						endDate = StartEnd[1];
						if (startDate == '') {
							alert('開始日期需輸入');
							return;
						} else if (endDate == '') {
							alert('結束日期需輸入');
							return;
						}
						var checkDay = checkStartEnd(startDate, endDate);
						if (!checkDay) {
							alert('結束日期不可早於開始日期');
							return;
						}
					}
					if (step == 2) {
						var CheckAd = doCreateAd('AdtoCheck');
						if (!CheckAd)
							return;
					}
					this.step++;
					$(e.target).blur();
				},
				prevPage : function(e) {
					this.step--;
					$(e.target).blur();
				},
				updateStartDate : function(date) {
					if ($.trim(date) == '') {
						return;
					}
					if (getTodayDiffDays(date) < 0) {
						alert("開始時間小於今日時間,請確認是否正確");
					}
					this.startDate = date;
				},
				updateEndDate : function(date) {
					if ($.trim(date) == '') {
						return;
					}
					if (getTodayDiffDays(date) < 0) {
						alert("結束時間小於今日時間,請重新設定");
						return;
					}
					this.endDate = date;
				},
				validateEndDate : function(date, element) {
					alert(date + " ,,," + element);
				},
				onFileChange : function(e) {
					var files = e.target.files || e.dataTransfer.files;
					var fileTypes = [ 'jpg', 'jpeg', 'png' ];
					var extension = files[0].name.split('.').pop()
							.toLowerCase();
					var isSuccess = fileTypes.indexOf(extension) > -1;
					if (!files.length)
						return;
					if (!isSuccess) {
						alert('上傳圖檔只接受jpg, jpeg, png格式')
						this.image = '';
						e.target.value = '';
					} else {
						// 檢查檔名是否存在
						if (!checkAdprjidIsOk(files[0].name)) {
							return;
						}
						this.createImage(files[0]);
					}
				},
				createImage : function(file) {
					var image = new Image();
					var reader = new FileReader();
					var vm = this;
					var format_float = function(num, pos) {
						var size = Math.pow(10, pos);
						return Math.round(num * size) / size;
					}

					reader.onload = function(e) {
						vm.image = e.target.result;

						// 新增模式才能改檔名
						if (this.newAdMode) {
							vm.imageName = file.name;
						}

						vm.imageSize = format_float(e.total / 1024, 2);
					};
					reader.readAsDataURL(file);
				},
				onFileChange2 : function(e) {
					var files = e.target.files || e.dataTransfer.files;
					var fileTypes = [ 'jpg', 'jpeg', 'png' ];
					var extension = files[0].name.split('.').pop()
							.toLowerCase();
					var isSuccess = fileTypes.indexOf(extension) > -1;
					if (!files.length)
						return;
					if (!isSuccess) {
						alert('上傳圖檔只接受jpg, jpeg, png格式')
						this.image2 = '';
						e.target.value = '';
					} else {
						this.createImage2(files[0]);
					}
				},
				createImage2 : function(file) {
					var image = new Image();
					var reader = new FileReader();
					var vm = this;
					var format_float = function(num, pos) {
						var size = Math.pow(10, pos);
						return Math.round(num * size) / size;
					}

					reader.onload = function(e) {
						vm.image2 = e.target.result;
						vm.image2Name = file.name;
						vm.image2Size = format_float(e.total / 1024, 2);
					};
					reader.readAsDataURL(file);
				},
				sendAudit : function(idx, campaign) {
					sendAuditHandler(idx, campaign);
				},
				editAgain : function(idx, campaign) {
					editAgainHandler(idx, campaign);
				},
				orderby : function(orderColumn, ascOrDesc) {
					var isAsc = false;
					if (ascOrDesc == 'asc') {
						isAsc = true;
					}
					document.location.href = "adEdit?orderColumn="
							+ orderColumn + "&isAsc=" + isAsc;
				},
				stopAD : function(idx, campaign) {
					stopADHandler(idx, campaign);
				},
				blockRadioChoice : function(business) {
					this.blockphoto = business.blockphoto;
					this.getNowAdmobPics(business.blockcode, '');
				},
				getNowAdmobPics : function(blockcode, ugid) {
					getNowAdmobPicsHandler(blockcode, ugid);
				},
				applyForEditPage : function(mainVo, picVo) {
					this.adBlock = mainVo.blockcode;
					this.adName = mainVo.adprjname;
					this.startDate = mainVo.stimeForUI;
					this.endDate = mainVo.etimeForUI;
					this.imgAlt = picVo.desc;
					this.imgUrl = picVo.liinkto;
					this.image = picVo.photo;
					this.imageName = mainVo.adprjid;
					this.isDefault = picVo.isDefault;
					this.imageSize = this.toPicSize(picVo.photo);
					this.image2Size = 0;
					this.newAdMode = false;// update mode
					$('#startDate').datepicker('setDate', this.startDate);
					$('#endDate').datepicker('setDate', this.endDate);
				},
				toPicSize : function(dataurl) {
					var d = dataURLtoBlob(dataurl);
					return Math.round(d.size / 1024);
				},
			},
			watch : {
				step : function(val, oldVal) {
					// alert("step == "+ val + " , " + oldVal + " --> " +
					// this.step);
				},
			}
		});

/*$(function (){
function format_float(num, pos)
{
    var size = Math.pow(10, pos);
    return Math.round(num * size) / size;
}
function preview(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('.preview').attr('src', e.target.result);
            var KB = format_float(e.total / 1024, 2);
            if (KB > 200) {
              $('.size').html('<span class="text-danger">超過建議檔案大小(' + KB + " KB)</span>")
            } else {
              $('.size').text("檔案大小：" + KB + " KB");
            }
        }
        reader.readAsDataURL(input.files[0]);
    }
}
function preview2(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('.preview2').attr('src', e.target.result);
            var KB = format_float(e.total / 1024, 2);
            if (KB > 200) {
              $('.size2').html('<span class="text-danger">超過建議檔案大小(' + KB + " KB)</span>")
            } else {
              $('.size2').text("檔案大小：" + KB + " KB");
            }
        }
        reader.readAsDataURL(input.files[0]);
    }
}
$("body").on("change", ".upl", function(){
    preview(this);
})
$("body").on("change", ".upl2", function(){
    preview2(this);
})
});*/