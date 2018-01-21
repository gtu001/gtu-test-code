	re1 = /^\d{4}-\d{2}-\d{2}$/;
	re2 = /^\d{4}\d{2}\d{2}$/;

	var partYear ="";
	var partMonth ="";
	var partDay ="";
	var formalDate = "";
	var leapYearFlag = "";

function perpetualCalendar(fieldName,inputDate){
  if(inputDate != ""){
	formalDate = correctForm(fieldName,inputDate);
	if(formalDate == ""){
            fieldName.focus();
            return;
	  }
	leapYearFlag = checkLeapYear(partYear);
	checkDateFormat(leapYearFlag,partMonth,partDay,fieldName);
  }
}

function checkDateFormat(lyf,partM,partD,fieldName){
	if(partM < 1 || partM > 12){
		alert("\u8f38\u5165\u4e86\u932f\u8aa4\u7684\u6708\u4efd:" + partMonth + "\n\u8acb\u91cd\u586b!!");
                fieldName.value = "";
                fieldName.focus();
                return;
	}
	if(partD < 1 || partD > 31){
		alert("\u8f38\u5165\u4e86\u932f\u8aa4\u7684\u65e5\u671f: " + partDay + "\n\u8acb\u91cd\u586b!!");
		fieldName.value = "";
                fieldName.focus();
		return;
	}

	if((partM == 2) && (lyf == true) && partD > 29){
		alert("\u4e8c\u6708\u6c92\u6709" + partD + "\u5929" + "\n\u8acb\u91cd\u586b!!");
		fieldName.value = "";
                fieldName.focus();
		return;
	}else if((partM == 2) && (lyf == false) && partD > 28){
		alert("\u4eca\u5e74\u4e0d\u662f\u958f\u5e74,\n\u6216\u662f\n\u8f38\u5165\u4e86\u932f\u8aa4\u7684\u65e5\u671f:" + partD + "\n\u8acb\u91cd\u586b!!");
		fieldName.value = "";
                fieldName.focus();
		return;
	}
	if((partM == 4 || partM == 6 || partM == 9 || partM == 11) && partD > 30){
		alert(partM + "\u6708\u4efd\u6c92\u6709" + partD + "\u5929" + "\n\u8acb\u91cd\u586b!!");
		fieldName.value = "";
                fieldName.focus();
		return;
	}
}

function checkLeapYear(inputYear){
	if((inputYear % 4 ==0 && inputYear % 100 != 0) || inputYear % 400 == 0){
		return true;
	}else{
		return false;
	}
}

function correctForm(fieldName,inputDate){
	if (re1.test(inputDate)){
		alert("\u6709\u6a6b\u7dda\u7684!!" + inputDate);
		//alert("\u6709\u6a6b\u7dda\u7684!!" + inputDate);
		partYear =inputDate.substring(0,4);
		partMonth =inputDate.substring(5,7);
		partDay =inputDate.substring(8,10);
		return inputDate;
	}else if(re2.test(inputDate)){
		alert("\u6c92\u7684!!" +inputDate);
		partYear =inputDate.substring(0,4);
		partMonth =inputDate.substring(4,6);
		partDay =inputDate.substring(6,8);
		//alert("\u683c\u5f0f\u5df2\u8f49\u63db!!");
		fieldName.value = partYear + "-" + partMonth + "-" + partDay;
		return partYear + "-" + partMonth + "-" + partDay;
	}else{
		alert("\u65e5\u671f\u683c\u5f0f\u932f\u8aa4:" + inputDate + "\n\u683c\u5f0f:20050101");
		fieldName.value = "";
                fieldName.focus();
		return "";
	}
}

function yearAndDay(fieldName,inputDate){
  window.focus();
  if(inputDate != ""){
   if(!(/^([0-9]*)$/g.test(inputDate))){
          fieldName.value = "";
          alert("\u50c5\u80fd\u8f38\u5165\u6578\u5b570~9 !!");
          fieldName.focus();
          return ;
    }
   else{
        if(inputDate.length != 6 && inputDate.length != 4){
            fieldName.value = "";
            alert("\u65e5\u671f\u683c\u5f0f\u932f\u8aa4:" + inputDate + "\n\u683c\u5f0f:200501\u62162004");
            fieldName.focus();
            return ;
         }
         else{
   	       if(inputDate.length == 6){
                    var year =inputDate.substring(0,4);
                    var month =inputDate.substring(4,6);
                          if(year < 1900 || year > 2020){
                              fieldName.value = "";
                              alert("\u5e74\u4efd\u50c5\u80fd\u4ecb\u65bc1900\u5e74~2020\u5e74 !!");
                              fieldName.focus();
                              return ;
                           }
                          if(month < 1 || month > 12){
                              fieldName.value = "";
                              alert("\u6708\u4efd\u8f38\u5165\u932f\u8aa4:" + month);
                              fieldName.focus();
                              return ;
                           }
                  }
               if(inputDate.length == 4){
                     if(inputDate < 1900 || inputDate > 2020){
                           fieldName.value = "";
                           alert("\u5e74\u4efd\u50c5\u80fd\u4ecb\u65bc1900\u5e74~2020\u5e74 !!");
                           fieldName.focus();
                           return ;
                      }
                }
           return inputDate;
          }
        }
      }else{
        return;
      }
    }

function todayCalendar(obj){
      if(obj.value == ""){
          var temp = new Date();
          var currentYear = temp.getYear();
          var currentMonth = temp.getMonth()+1;
          var currentDay = temp.getDate();
          if(currentMonth < 10){
             currentMonth = "0"+currentMonth;
          }
          if(currentDay < 10){
             currentDay = "0"+currentDay;
          }
          var currentDate = currentYear +"-"+ currentMonth +"-"+ currentDay;
              obj.value = currentDate;
        }else{
          	alert("\u5df2\u8f38\u5165\u65e5\u671f\u70ba :"+obj.value);
          }
      }

