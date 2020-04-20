<#import "/lib.ftl" as my>  
<#import "ctbc_dao_maker.ftl" as ct>  

package com.sinosoft.lis.kttest;

import com.sinosoft.utility.CError;
import com.sinosoft.utility.CErrors;
import com.sinosoft.utility.VData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ${ct.getFunObj()['uiClass']} {
    private static final Logger logger = LoggerFactory.getLogger(${ct.getFunObj()['uiClass']}.class);

    public CErrors mErrors = new CErrors();
    private VData mInputData = new VData();
    private VData mResult = new VData();

    public ${ct.getFunObj()['uiClass']}() {
    }

    public boolean submitData(VData cInputData, String cOperate) {
        mInputData = (VData) cInputData.clone();
        ${ct.getBlObj()['blClass']} t${ct.getBlObj()['blClass']} = new ${ct.getBlObj()['blClass']}();
        if (!t${ct.getBlObj()['blClass']}.submitData(mInputData, cOperate)) {
            this.mErrors.copyAllErrors(t${ct.getBlObj()['blClass']}.mErrors);
            CError tError = new CError();
            tError.moduleName = "${ct.getFunObj()['uiClass']}";
            tError.functionName = "submitData";
            tError.errorMessage = t${ct.getBlObj()['blClass']}.mErrors.getFirstError();
            this.mErrors.addOneError(tError);
            return false;
        }
        mResult = t${ct.getBlObj()['blClass']}.getResult();
        return true;
    }

    public VData getResult() {
        return mResult;
    }

    public CErrors getErrors() {
        return mErrors;
    }
}
