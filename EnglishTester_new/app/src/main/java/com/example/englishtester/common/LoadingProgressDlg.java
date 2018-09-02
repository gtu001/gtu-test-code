package com.example.englishtester.common;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by wistronits on 2018/8/10.
 */

public class LoadingProgressDlg {

    public static ProgressDialog createSimpleLoadingDlg(Context context) {
        ProgressDialog myDialog = new ProgressDialog(context);
        myDialog.setTitle(null);
        myDialog.setMessage("讀取中...");
        myDialog.setIndeterminate(true);
        myDialog.setCancelable(false);
        myDialog.setOnCancelListener(null);
        return myDialog;
    }
}
