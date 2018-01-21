/** 
 * 金額按千位逗號分割 
 * @version 1.2014.08.24.2143 
 *  Example 
 *  <code> 
 *      alert($.formatMoney(1234.345, 2)); //=>1,234.35 
 *      alert($.formatMoney(-1234.345, 2)); //=>-1,234.35 
 *      alert($.unformatMoney(1,234.345)); //=>1234.35 
 *      alert($.unformatMoney(-1,234.345)); //=>-1234.35 
 *  </code> 
 */  
;(function($)  
{  
    $.extend({  
        /** 
         * 數位千分位元格式化 
         * @public 
         * @param mixed mVal 數值 
         * @param int iAccuracy 小數位精度(默認為2) 
         * @return string 
         */  
        formatMoney:function(mVal, iAccuracy){  
            var fTmp = 0.00;//臨時變數  
            var iFra = 0;//小數部分  
            var iInt = 0;//整數部分  
            var aBuf = new Array(); //輸出緩存  
            var bPositive = true; //保存正負值標記(true:正數)  
            /** 
             * 輸出定長字串，不夠補0 
             * <li>閉包函數</li> 
             * @param int iVal 值 
             * @param int iLen 輸出的長度 
             */  
            function funZero(iVal, iLen){  
                var sTmp = iVal.toString();  
                var sBuf = new Array();  
                for(var i=0,iLoop=iLen-sTmp.length; i<iLoop; i++)  
                    sBuf.push('0');  
                sBuf.push(sTmp);  
                return sBuf.join('');  
            };  
  
            if (typeof(iAccuracy) === 'undefined')  
                iAccuracy = 2;  
            bPositive = (mVal >= 0);//取出正負號  
            fTmp = (isNaN(fTmp = parseFloat(mVal))) ? 0 : Math.abs(fTmp);//強制轉換為絕對值數浮點  
            //所有內容用正數規則處理  
            iInt = parseInt(fTmp); //分離整數部分  
            iFra = parseInt((fTmp - iInt) * Math.pow(10,iAccuracy) + 0.5); //分離小數部分(四捨五入)  
  
            do{  
                aBuf.unshift(funZero(iInt % 1000, 3));  
            }while((iInt = parseInt(iInt/1000)));  
            aBuf[0] = parseInt(aBuf[0]).toString();//最高段區去掉前導0 
            
            return ((bPositive)?'':'-') + aBuf.join(',') + ((0 === iFra)?'':'.'+funZero(iFra, iAccuracy));  
        },  
        /** 
         * 將千分位元格式的數位字串轉換為浮點數 
         * @public 
         * @param string sVal 數值字串 
         * @return float 
         */  
        unformatMoney:function(sVal){  
            var fTmp = sVal.replace(/,/g, '');  
            return (isNaN(fTmp) ? 0 : fTmp);  
        },  
    });  
})(jQuery);  



/**
 * 身份證字號檢核
 * @param originalStr 原始字串 
 * @param option 0: toHalf, 1:toFull
 */
function checkTwID(id){
    //建立字母分數陣列(A~Z)
    var city = new Array(
         1,10,19,28,37,46,55,64,39,73,82, 2,11,
        20,48,29,38,47,56,65,74,83,21, 3,12,30
    );
    id = id.toUpperCase();
    // 使用「正規表達式」檢驗格式
    if (id.search(/^[A-Z](1|2)\d{8}$/i) == -1) {
//         alertify.alert('<s:text name="alert.id.formatError"/>');
        return false;
    } else {
        //將字串分割為陣列(IE必需這麼做才不會出錯)
        id = id.split('');
        //計算總分
        var total = city[id[0].charCodeAt(0)-65];
        for(var i=1; i<=8; i++){
            total += eval(id[i]) * (9 - i);
        }
        //補上檢查碼(最後一碼)
        total += eval(id[9]);
        //檢查比對碼(餘數應為0);
        if(total%10 != 0){
//          alertify.alert('<s:text name="alert.id.invalidId"/>');        	
         return false;
        }
        return true;
    }
}

