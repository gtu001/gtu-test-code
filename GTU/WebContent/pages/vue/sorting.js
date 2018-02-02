function SortConf(thisVarName, callbackFunc, arry) {
	this.sortConfig = new Map();
	this.arry = arry;
	this.getTitleLink = function(column, label) {
		var sortType = this.getSortType(column);
		if (sortType == 'asc') {
			sortType = "↓";
		} else if (sortType == 'desc') {
			sortType = "↑";
		}
		var rtnVal = "<a href=\"javascript:" + thisVarName + ".doSort(\'"
				+ column + "\')\">" + label + sortType + "</a>";
		return rtnVal;
	};

	this.getSortType = function(column) {
		var key = "";
		for ( var k in this.sortConfig) {
			key = k;
			break;
		}
		var value = this.sortConfig[key];
		if (key == column) {
			return value;
		}
		return "";
	};

	this.doSort = function(column) {
		var sortType = "asc";
		if (column in this.sortConfig) {
			sortType = this.sortConfig[column];
			if (sortType == 'asc') {
				sortType = 'desc';
			} else if (sortType == 'desc') {
				sortType = 'asc';
			}
		}
		this.sortConfig = new Map();
		this.sortConfig[column] = sortType;
		this.arry = this.arraySort(this.arry, column, sortType);
		callbackFunc();
	};

	this.arraySort = function(arry, column, sortType) {
		var dataArry = new Array();
		for (var ii = 0; ii < arry.length; ii++) {
			dataArry.push(new String(arry[ii][column]));
		}
		dataArry.sort();
		if (sortType == 'desc') {
			dataArry.reverse();
		}
		var rtnArry = new Array();
		for ( var val in dataArry) {
			for (var ii = 0; ii < arry.length; ii++) {
				if (arry[ii][column] == dataArry[val]) {
					rtnArry.push(arry[ii]);
					arry.splice(ii, 1);
					break;
				}
			}
		}
		return rtnArry;
	};
}
