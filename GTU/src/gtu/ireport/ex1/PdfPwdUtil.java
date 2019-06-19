/*
 * @(#)PdfPwdUtil.java 2016年5月16日
 *
 * Copyright (c) 2013 - Business Intelligence Info. Tech. 3F., No.363, Sec. 2,
 * Fuxing S. Rd., Da’an Dist., Taipei City 106, Taiwan (R.O.C.) All rights
 * reserved.
 */

package gtu.ireport.ex1;

/**
 * @author tzu-liang
 * @version
 *
 */

public class PdfPwdUtil {

    private static final BlowfishCipherProcessor bcp = new BlowfishCipherProcessor();

    /**
     * 產生PDF USER PASSWORD：userCode + password
     * 
     * @param ScsbAdmUsers
     *            user
     * @return USER PASSWORD
     */
    public static String getPdfUserPassword(String userCode, String password) {
        StringBuilder str = new StringBuilder("");
        if (null != userCode) {
            str.append(userCode);
            if (null != password) {
                str.append(bcp.decode(password));
            }
        }
        return str.toString();
    }

    /**
     * 產生PDF USER PASSWORD：客戶ID後8碼
     */
    public static String getPdfUserPasswordByCusIdn(String cusIdn) {
        return cusIdn == null ? "" : (cusIdn.length() <= 8 ? cusIdn : cusIdn.substring(cusIdn.length() - 8));
    }

    /**
     * 產生PDF OWNER PASSWORD："scsbwmsr6pdf@" + userCode
     * 
     * @param ScsbAdmUsers
     *            user
     * @return OWNER PASSWORD
     */
    public static String getPdfOwnerPassword(String userCode) {
        StringBuilder str = new StringBuilder("scsbwmsr6pdf@");
        if (null != userCode) {
            str.append(userCode);
        }
        return str.toString();
    }
}
