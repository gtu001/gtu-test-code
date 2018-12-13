//-- util functions

function createShadowAroundDOM( DOM_element ){
	
	var shadow_table = document.createElement("TABLE");
	shadow_table.setAttribute("cellPadding" , 0 );
	shadow_table.setAttribute("cellSpacing" , 0 );
	shadow_table.className = ((shadow_table.className||"") + " shadow");
	
	var shadow_tBody = document.createElement("TBODY");
	
	shadow_table.appendChild(shadow_tBody);
	
	
	var shadow_tr = document.createElement("TR");
	var shadow_td = document.createElement("TD");
	shadow_tBody.appendChild(shadow_tr);
	shadow_tr.appendChild(shadow_td);
	shadow_td.appendChild(DOM_element);
	return shadow_table;
}

function setClass( node , className){
	node.className = ((node.className||"") +" "+ className);
}



//---------------------- start -----------------------------

var calendarBase;

function autoCreateDate( elem , isAlwaysROC ){

	if(!calendarBase || (isAlwaysROC === true && isAlwaysROC === false) ){	calendarBase = new create_Calendar(isAlwaysROC); }
		
	var inputs=[];
	var parent = isDOMObject(elem) ? elem : document.getElementById(elem);
	if(!parent ){parent = document.body; }
	var temp_inputs = parent.getElementsByTagName("INPUT");
	for(var i = 0 ; i < temp_inputs.length ; i = i + 1 ){
		var datatype = (temp_inputs[i].getAttribute("datatype") || "").toUpperCase();
		if( datatype && (datatype == "DATE" || datatype == "ROCDATE") ){
			inputs[inputs.length] = temp_inputs[i];
		}
	}
	
	
	if(inputs && inputs.length > 0){
		for(var i = 0 ; i < inputs.length ; i = i + 1 ){
			var input = inputs[i];
			
			input.setAttribute("maxLength" , isAlwaysROC ? 9 : 10 );
			input.setAttribute("size" , isAlwaysROC ? 8 : 9 );
			
			if(input.nextSibling){
				var nextSiblingNode = input.nextSibling;
				if(nextSiblingNode.tagName === "IMG"){
					var src = nextSiblingNode.getAttribute('src')||"";
					if(src && src.length >=12 && src.substr( src.length-12 ,12) == "calendar.gif"){
						if(!input.getAttribute("id")){
							nextSiblingNode.onclick = function(){ getCalendarFor( this.previousSibling ); }
						}
						continue;
					}
				}
			}
			var img = document.createElement("img");
			insertAfter(img,input);
			
			img.setAttribute("width" , "16");
			img.setAttribute("height" , "16");
			img.setAttribute("src","./calendar.gif");
			img.onclick = function(e){ 
                                getCalendarFor( e.target.previousSibling );
                        };
		}
		// SCRUtil.js ---> autoFormatToDate 控制input輸入
		if(window.autoFormatToDate){ autoFormatToDate(); }
	}
	
	function insertAfter(newElement,targetElement) {
		var parent = targetElement.parentNode;
		if (parent.lastChild == targetElement) {
			parent.appendChild(newElement);
		} else {
			parent.insertBefore(newElement,targetElement.nextSibling);
		}
	}
	
	function isDOMObject( node ){
		return !!( node && node.tagName && node.nodeType === 1); 
	}
}

function getCalendarFor( elem ){
	if(!( elem && elem.tagName && elem.nodeType === 1)) { 
		elem = document.getElementById(elem); 
	}
	if(!( elem && elem.tagName && elem.nodeType === 1)) { return; }
	
	if(! calendarBase) { calendarBase = new create_Calendar(); }
	calendarBase.showCalendar( elem );
}

function create_Calendar( isAlwaysROC ){
	
	if(calendarBase){ 
		document.body.removeChild(calendarBase.calendar_ShadowTable); 
		document.body.removeChild(calendarBase.cover); 
	}
	
	var isROC = isAlwaysROC ? true : false;
	
	var workingNode;
	
	//設定參數
	var month_display = ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"];
	var day_display=["日","一","二","三","四","五","六"];
	var pre = "\u25C4";
	var next = "\u25BA";
	var normalBlockSize = 20;
	var BiggerBlockWidth = 54.3333;
	var BiggerBlockHeight = 40;
	
	var displayYearDisCount;
	var displayTitleBeforeYear;
	var displayTitleAfterYear;
	var displayDateSplit;
	var spanBetweenYears;
	var maxYearLevel;
	var SimpleDateFormats = {};
	
	resetRocInfo(isROC);
	
	//calendar主體
	var calendarTable = document.createElement("table");
	var thead = document.createElement("thead");
	var tbody = document.createElement("tbody");
	calendarTable.appendChild(thead);
	calendarTable.appendChild(tbody);
	
	//----------------------- control Table ------------------
	var calendarTable2 = document.createElement("table");
	calendarTable2.setAttribute("width" , "100%");
	calendarTable2.setAttribute("border" , "0");
	calendarTable2.setAttribute("cellpadding" , "0");
	calendarTable2.setAttribute("cellspacing" , "0");

	var thead2 = document.createElement("thead");
	calendarTable2.appendChild(thead2);
	
	//操作與年月顯示區
	var tempTableRow = document.createElement("TR");
	thead2.appendChild(tempTableRow);
	
	var leftControl = document.createElement("TH");
	setWidthAndHeight( leftControl , normalBlockSize , normalBlockSize );
	tempTableRow.appendChild(leftControl);
	
	var displayCell = document.createElement("TH");
	displayCell.style.borderLeft = '1px solid #000';
	displayCell.style.borderRight = '1px solid #000';
	tempTableRow.appendChild(displayCell);
	
	var rightControl = document.createElement("TH");
	setWidthAndHeight( rightControl , normalBlockSize , normalBlockSize );
	tempTableRow.appendChild(rightControl);
	
	//----------------------- control Table ------------------
	
	var controlTableRow = document.createElement("TR");
	var controlTableCell = document.createElement("TH");
	thead.appendChild(controlTableRow);
	controlTableRow.appendChild(controlTableCell);
	controlTableCell.appendChild(calendarTable2);
	var calendar_ShadowTable = createShadowAroundDOM(calendarTable);
	setClass( calendar_ShadowTable , "calendarTable");
	calendar_ShadowTable.style.display = 'none';
	
	/* cover */
	var cover = document.createElement("DIV");
	setClass( cover , "cover");
	cover.style.display = 'none';
	cover.onclick = closecalendar ;
	cover.onmouseover = function(){ timeToClose(true); }
	cover.onmouseout = function(){ timeToClose(false); }
	
	document.body.appendChild(cover);
	
	document.body.appendChild(calendar_ShadowTable);
	
	this.calendar_ShadowTable = calendar_ShadowTable;
	this.cover = cover;
	
	this.showCalendar = showCalendar;
	function showCalendar( node ){
               
		workingNode = node;
		
		if(isAlwaysROC !== true ){
			var datatype = (workingNode.getAttribute("datatype") || "").toUpperCase();
			if( datatype && datatype === 'ROCDATE' ){
				if(!isROC){
					resetRocInfo(true);
				}
			}else{
				if(isROC){
					resetRocInfo(false);
				}
			}
		}
		
		//var maxLeft = document.body.scrollLeft + document.body.offsetWidth;
		//var maxTop = document.body.scrollTop + document.body.offsetHeight;
		function getOffsetTop(element) {
			var valueLeft = 0,
				valueTop = 0;
			do {
				valueTop += element.offsetTop || 0;
				valueLeft += element.offsetLeft || 0;
				element = element.offsetParent;
			} while (element);
			return { Left : valueLeft , Top : valueTop };
		}
		
		var dimension = getOffsetTop( node );
		var maxLeft = dimension.Left;
		var maxTop = dimension.Top;
		
		maxLeft -= 30;		//因為小日曆 icon 會有佔住30的大小
		maxTop += node.offsetHeight + 5;
		
		// var oEvent = event;
		//var left = document.body.scrollLeft + oEvent.clientX - 2;
		//var top = document.body.scrollTop + oEvent.clientY - 2;
		
		displayDateSplit = node.getAttribute("dateSplit") || (isROC?"":"-");
		
		if(window.isDate){
			var pattern = node.getAttribute("pattern");
			if(!pattern && displayDateSplit){
				pattern = isROC ? "yyy"+displayDateSplit+"MM"+displayDateSplit+"dd" : "yyyy"+displayDateSplit+"MM"+displayDateSplit+"dd";
			}
			if(pattern){
				SimpleDateFormats.displayFormat = new SimpleDateFormat( pattern , (isROC?SimpleDateFormat.TW:SimpleDateFormat.US) ); 
			
			}else{
				SimpleDateFormats.displayFormat = null;
			}
		}
		
		createDateSelectBoxByString(node.value);
		calendar_ShadowTable.style.display = '';
		
		//maxLeft = maxLeft - calendar_ShadowTable.offsetWidth - 20;
		//maxTop = maxTop - calendar_ShadowTable.offsetHeight - 20;
		
		calendar_ShadowTable.style.top = maxTop + 'px';   //((maxTop>0 && top>maxTop)?maxTop:top)+'px';
		calendar_ShadowTable.style.left = maxLeft + 'px';//((maxLeft>0 && left>maxLeft)?maxLeft:left)+'px';
		calendar_ShadowTable.style.position = "absolute";
		
		cover.style.display = '';
	}
	function myFunction(element, addClassName) {
                var arr = element.className.split(" ");
                if (arr.indexOf(addClassName) == -1) {
                        element.className += " " + addClassName;
                }
        }
	this.closecalendar = closecalendar;
	function closecalendar( value , isCheckTimeOut ){
		if(isCheckTimeOut === true ){return;}
		if( typeof(value) == 'string'){
			
			if(workingNode){
				if(workingNode.value !== value){
					workingNode.value = value;
					/*	TODO 
					if(Object.prototype.toString.apply(workingNode.onchange)  === '[object Function]' ){
						workingNode.onchange();
					}else{
						workingNode.fireEvent( "onchange" );
					}*/
				}
			}else{
				alert(node.getAttribute("value"));
			}
		}
		calendar_ShadowTable.style.display = 'none';
		calendar_ShadowTable.style.top = '';
		calendar_ShadowTable.style.left = '';
		
		cover.style.display = 'none';
	} 
	
	var isTimeToClose = false;
	function timeToClose( isStart ){
		if(isTimeToClose !== isStart ){
			isTimeToClose = !isTimeToClose;
			setTimeout('calendarBase.closecalendar( null , true )',3000);	
		}
	}
	
	
	function resetRocInfo(ROC){
		isROC = ROC;
		displayYearDisCount = isROC ? 1911 : 0;
		displayTitleBeforeYear = isROC ? "民國 " : "";
		displayTitleAfterYear = isROC ? " 年" : "";
		spanBetweenYears = isROC?" 至 " : " ~ ";
		maxYearLevel =  isROC ? 10 : 100;
		
	}
	
	//---------------------------- functions ---------------------------------
	
	function createDateSelectBoxByString(testDate){
		
		if(testDate){
			if( window.isDate){
				if(!isROC){
					if(isDate(testDate)){
						var dateObj = stringToDate_Y2K( testDate , true );
						createDateSelectBox( dateObj.getFullYear() , dateObj.getMonth() , dateObj.getDate() );
						return;
					}
				}else{
					if(isROCdate(testDate , false , true)){
						var dateObj = stringToDate_ROC( testDate , true );
						createDateSelectBox( dateObj.getFullYear() , dateObj.getMonth() , dateObj.getDate() );
						return;
					}
				}
			}else{
				var year = 0;
				var month = 0;
				var date = 0;
				if(!isROC){
					var splitDate = testDate.split(displayDateSplit || "-");
					year = parseInt((splitDate[0]||"").replace(/(^[0]*)/g, "") ,10 );
					month = parseInt((splitDate[1]||"").replace(/(^[0]*)/g, "")||1 ,10 )-1;
					date = parseInt((splitDate[2]||"").replace(/(^[0]*)/g, "")||1 ,10 );
				}else{
					var splitDate = testDate.split( (displayDateSplit||"/") );
					if(splitDate[0] === testDate){
						testDate = parseInt(testDate , 10);
						date = testDate % 100;
						month = (Math.floor(testDate/100) % 100)-1;
						year = Math.floor(testDate/10000) + displayYearDisCount;
					}else{
						year = parseInt((splitDate[0]||"").replace(/(^[0]*)/g, "") ,10 ) + displayYearDisCount;
						month = parseInt((splitDate[1]||"").replace(/(^[0]*)/g, "")||1 ,10 )-1;
						date = parseInt((splitDate[2]||"").replace(/(^[0]*)/g, "")||1 ,10 );
					}
				}
				if( year && year >= 0 && year <= 9999 && ( month || month === 0 ) && (!date || ( date > 0 && date < 32 ) ) ){
					createDateSelectBox( year , month , date );
					return;
				}
			}
		}
		var dateObj = new Date();
		createDateSelectBox( dateObj.getFullYear() , dateObj.getMonth() , dateObj.getDate() );
		
	}
	

	function createDateSelectBox(year,month,date){
		
		if(month >= 12 ){ year = year + 1 ; month = 0;}
		else if (month <= -1){ year = year - 1 ; month = 11; }
		//設定參數
		var oneDateTime = (24*60*60*1000);
		
		//共用參數
		var tempTableRow;
		var tempTableCell;
		var colsInLine = 7;
		
		//清除之前產生的資料
		clearAllChild(tbody);
		clearAllChild(displayCell);
		clearAllChild(leftControl);
		clearAllChild(rightControl);
		controlTableCell.setAttribute("colSpan",colsInLine);
		
		//抓取主要月份資料
		var fristDateOfMonth = new Date();
		fristDateOfMonth.setFullYear(year,month,1);
		
		var day_startOfMonth = fristDateOfMonth.getDay();
		
		//標題控制區
		appendLinkChild(displayCell , displayTitleBeforeYear + (year-displayYearDisCount) + displayTitleAfterYear +" "+ month_display[month] , function(){ createMonthSelectBox(year,month); } );
		
		//上下月控制區
		appendLinkChild(leftControl , pre , function(){ createDateSelectBox(year,parseInt(month,10)-1,0); } );
		appendLinkChild(rightControl , next , function(){ createDateSelectBox(year,parseInt(month,10)+1,0); } );
		
		
		//星期顯示區
		tempTableRow = document.createElement("TR");
		for(var i = 0 ; i < day_display.length ; i = i + 1 ){
			tempTableCell = document.createElement("TD");
			setClass( tempTableCell , "subTitle");
			setClass( tempTableCell , "smallSize");
			setNodeText(tempTableCell,day_display[i]);
			tempTableRow.appendChild(tempTableCell);
		}
		tbody.appendChild(tempTableRow);
		
		
		tempTableRow = document.createElement("TR");
		tbody.appendChild(tempTableRow);
		
		//處理前月日期以填滿選單
		if(day_startOfMonth ){
			fristDateOfMonth = new Date( fristDateOfMonth.valueOf() -( day_startOfMonth * oneDateTime) );
		}
		
		//日期內容顯示區(固定六行)
		var day_count = 0;
		
		do{
			tempTableCell = document.createElement("TD");
			tempTableRow.appendChild(tempTableCell);
			
			var isThisMonth = fristDateOfMonth.getMonth() == month;
			
			appendLinkChild(
				tempTableCell , 
				fristDateOfMonth.getDate() , (
					isThisMonth ?
					function(node){ closecalendar(node.getAttribute("value")) }
					: function(node){ 
						createDateSelectBoxByString(node.getAttribute("value")); 
					}
				), 
				{value:getFormateDate(fristDateOfMonth)} 
			);
			setWidthAndHeight( tempTableCell , normalBlockSize , normalBlockSize );
			
			
			
			day_count = day_count + 1 ;
			
			var countModCols = Math.floor(day_count%7);
			if(!isThisMonth){setClass( tempTableCell , "colorGray");}
			else if(date && fristDateOfMonth.getDate() == date){ setClass( tempTableCell , "selectBlock"); } 
			else if( countModCols == 1 ) { tempTableCell.style.color = 'red';  }
			else if( countModCols == 0 ) { tempTableCell.style.color = 'blue';  }
			
			fristDateOfMonth = new Date( fristDateOfMonth.valueOf() + oneDateTime);
			if( countModCols == 0 ){
				tempTableRow = document.createElement("TR");
				tbody.appendChild(tempTableRow);
			}
			
		}while( day_count < 41  )
		
		//TODAY button
		tempTableCell = document.createElement("TD");
		tempTableRow.appendChild(tempTableCell);
		appendLinkChild(
			tempTableCell , 
			"今" , 
			function(node){ createDateSelectBoxByString(); }
		);
		setClass( tempTableCell , "subTitle");
		setWidthAndHeight( tempTableCell , normalBlockSize , normalBlockSize );
	}
	
	function createMonthSelectBox( year , month){
		
		
		//共用參數
		var tempTableRow;
		var tempTableCell;
		var colsInLine = 3;
		
		//清除之前產生的資料
		clearAllChild(tbody);
		clearAllChild(displayCell);
		clearAllChild(leftControl);
		clearAllChild(rightControl);
		controlTableCell.setAttribute("colSpan",colsInLine);
		//displayCell.setAttribute("colSpan",4);
		
		//標題控制區
		appendLinkChild(displayCell , displayTitleBeforeYear + (year-displayYearDisCount) + displayTitleAfterYear , function(){ createYearSelectBox(year , 1 , true); } );
		
		//上下月控制區
		appendLinkChild(leftControl , pre , function(){ createMonthSelectBox(parseInt(year,10)-1); } );
		appendLinkChild(rightControl , next , function(){ createMonthSelectBox(parseInt(year,10)+1); } );
		
		tempTableRow = document.createElement("TR");
		var month_count = 0;
		do{
			tempTableCell = document.createElement("TD");
			tempTableRow.appendChild(tempTableCell);
			
			appendLinkChild(
				tempTableCell , 
				month_display[month_count] , 
				function(node){ createDateSelectBox(node.getAttribute("year") ,node.getAttribute("month") , 0 ); }  ,
				{ month:month_count, year:year } 
			);
			if(month && month_count == month){ setClass( tempTableCell , "selectBlock"); }
			setWidthAndHeight( tempTableCell , BiggerBlockWidth , BiggerBlockHeight );
			
			month_count = month_count + 1 ;

			if( Math.floor(month_count%colsInLine) == 0 ){
				tbody.appendChild(tempTableRow);
				tempTableRow = document.createElement("TR");
			}
			
		}while( month_count < 12)
		
	}
	
	function createYearSelectBox( year , yearLevel, isShowSelectButton ){
		
		if(!yearLevel){yearLevel = 1; }
		//共用參數
		var tempTableRow;
		var tempTableCell;
		var colsInLine = 3;
		
		
		//清除之前產生的資料
		clearAllChild(tbody);
		clearAllChild(displayCell);
		clearAllChild(leftControl);
		clearAllChild(rightControl);
		displayCell.setAttribute("colSpan",colsInLine);
		
		var yearArea = yearLevel*10;
		
		var startYear = (Math.floor(year/yearArea)*yearArea)+(displayYearDisCount?Math.floor(displayYearDisCount%yearArea):0);
		
		//標題控制區
		var titleShow = displayTitleBeforeYear + (startYear-displayYearDisCount )+spanBetweenYears+((startYear+yearArea-1)-displayYearDisCount) + displayTitleAfterYear;
		if( yearArea > maxYearLevel){
			setNodeText(displayCell , titleShow );
		}else{ 
			appendLinkChild(displayCell , titleShow  , function(){ createYearSelectBox(year , yearArea , true); } );
		}
		
		//上下月控制區
		appendLinkChild(leftControl , pre , function(){ createYearSelectBox(parseInt(year,10)-yearArea , yearLevel); } );
		appendLinkChild(rightControl , next , function(){ createYearSelectBox(parseInt(year,10)+yearArea , yearLevel); } );
		
		tempTableRow = document.createElement("TR");
		
		var showWithNotArea = yearLevel == 1;
		
		var year_count = -1;
		do{
			tempTableCell = document.createElement("TD");
			tempTableRow.appendChild(tempTableCell);
			
			var cell_year = (startYear + ( year_count * yearLevel ));
			
			var cell_Show = showWithNotArea ? 
				(cell_year - displayYearDisCount) : 
				(cell_year - displayYearDisCount) + spanBetweenYears + ( cell_year + ( yearLevel-1 ) - displayYearDisCount);
				
			var cell_function = showWithNotArea ?
				function(node){ createMonthSelectBox( node.getAttribute("year") , 0 ) } :
				function(node){ createYearSelectBox( node.getAttribute("year") , Math.floor(yearLevel/10) ) } ;
				
			
			var showSelect = isShowSelectButton && (
				( showWithNotArea && cell_year == year ) ||
				( ! showWithNotArea && cell_year <= year && ( cell_year + ( yearLevel-1 ) )  >= year )
			);
			appendLinkChild( tempTableCell , cell_Show + displayTitleAfterYear , cell_function , { year : cell_year  }	);
			
			if( showSelect ){ setClass( tempTableCell , "selectBlock"); }
			
			setWidthAndHeight( tempTableCell , BiggerBlockWidth , BiggerBlockHeight );
			
			if( year_count == -1 || year_count == 10 ){
				setClass( tempTableCell , "colorGray");
			}
			
			year_count = year_count + 1 ;
			if( Math.floor(( year_count + 1)%colsInLine) == 0 ){
				tbody.appendChild(tempTableRow);
				tempTableRow = document.createElement("TR");
			}
			
		}while( year_count < 11)
		
	}
	
	function appendLinkChild( parent , display , clickFunction , attrs){
		var tempLink = document.createElement("Font");
		setNodeText(tempLink, display );
		if(clickFunction){
			parent.onclick = function(){ clickFunction(tempLink); };
		}
		if(attrs){
			for(key in attrs){
				tempLink.setAttribute(key,attrs[key]);
			}	
		}
		parent.appendChild(tempLink);
	}
	
	function setWidthAndHeight( node , width , height ){
		if(width){
			node.style.width = width+'px' ;
		}else{
			node.style.width = "";
		}
		if(height){
			node.style.height = height+'px' ;
		}else{
			node.style.height = "" ;
		}
	}
	
	function clearAllChild(node){
		while(node.firstChild) {
			node.removeChild(node.firstChild); 
		} 		
	}
	
	function getFormateDate(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		if(SimpleDateFormats.displayFormat){
			var displayDate = SimpleDateFormats.displayFormat.format( year+(month<10?"-0"+month:"-"+month)+(day<10?"-0"+day:"-"+day));
			return displayDate;
		}else{
			return (year-displayYearDisCount) + displayDateSplit + (month<10?"0"+month:month)+ displayDateSplit + (day<10?"0"+day:day);
		}
		
	}
	
	function setNodeText(node,text){
	if(node.textContent != undefined)
	  node.textContent=text;
	else
	  node.innerText=text;
	}

}

