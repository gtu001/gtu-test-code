package com.cathay.xx.zx.module.test;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.util.AuthUtil;
import com.cathay.util.DBTestCase;
import com.cathay.xx.vo.DTXXTP01;
import com.cathay.xx.zx.module.XX_ZX0100;

public class XX_ZX0100Test extends DBTestCase {

    public XX_ZX0100Test(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private static XX_ZX0100 XX_ZX0100 = new XX_ZX0100();

    private static Logger log = Logger.getLogger(XX_ZX0100Test.class);

    public void testinsert() {

        UserObject user = new AuthUtil().getUserObjByID("A28248646B");

        log.debug("====================insert負向測試開始====================");
        try {
            XX_ZX0100.insert(null, null);
            log.debug("------------負向測試1失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試1成功------------");
            log.debug("", e);
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("BIRTHDAY", "TTTT");
            XX_ZX0100.insert(reqMap, user);
            log.debug("------------負向測試2失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試2成功------------");
            log.debug("", e);
        }
        log.debug("====================insert負向測試結束====================");
        log.debug("====================insert正向測試開始====================");
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11111");
            reqMap.put("BIRTHDAY", "1991-01-01");
            reqMap.put("DIV_NO", "11");
            reqMap.put("EMP_NAME", "11");
            reqMap.put("POSITION", "11");
            XX_ZX0100.insert(reqMap, user);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            assertFalse(true);
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11112");
            XX_ZX0100.insert(reqMap, user);
            log.debug("------------正向測試2成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試2失敗------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================insert正向測試結束====================");
    }

    public void testupdate() {

        UserObject user = new AuthUtil().getUserObjByID("A28248646B");

        log.debug("====================update負向測試開始====================");
        try {
            XX_ZX0100.update(null, null);
            log.debug("------------負向測試1失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試1成功------------");
            log.debug("", e);
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("BIRTHDAY", "TTTTT");
            XX_ZX0100.update(reqMap, user);
            log.debug("------------負向測試2失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試2成功------------");
            log.debug("", e);
        }

        log.debug("====================update負向測試結束====================");

        log.debug("====================update正向測試開始====================");
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11111");
            reqMap.put("BIRTHDAY", "1992-02-02");
            reqMap.put("DIV_NO", "11");
            reqMap.put("EMP_NAME", "22");
            reqMap.put("POSITION", "22");
            XX_ZX0100.update(reqMap, user);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            fail();
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11112");
            XX_ZX0100.update(reqMap, user);
            log.debug("------------正向測試2成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試2失敗------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================update正向測試結束====================");
    }

    public void testquery() {

        log.debug("====================query正向測試開始====================");
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11111");
            reqMap.put("DIV_NO", "11");
            XX_ZX0100.query(reqMap);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================query正向測試結束====================");
    }

    public void testconfirm() {
        log.debug("====================confirm負向測試開始====================");
        try {
            XX_ZX0100.confirm(null);
            log.debug("------------負向測試1失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試1成功------------");
            log.debug("", e);
        }
        log.debug("====================confirm負向測試結束====================");
        log.debug("====================confirm正向測試開始====================");
        try {
            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID("11111");
            XX_ZX0100.confirm(vo);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================confirm正向測試結束====================");
    }

    public void testreject() {
        log.debug("====================reject負向測試開始====================");
        try {
            XX_ZX0100.reject(null);
            log.debug("------------負向測試1失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試1成功------------");
            log.debug("", e);
        }
        log.debug("====================reject負向測試結束====================");
        log.debug("====================reject正向測試開始====================");
        try {
            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID("11111");
            XX_ZX0100.reject(vo);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================reject正向測試結束====================");
    }

    public void testapprove() {
        log.debug("====================approve負向測試開始====================");
        try {
            XX_ZX0100.approve(null);
            log.debug("------------負向測試1失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試1成功------------");
            log.debug("", e);
        }
        log.debug("====================approve負向測試結束====================");
        log.debug("====================approve正向測試開始====================");
        try {
            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID("11111");
            XX_ZX0100.approve(vo);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================approve正向測試結束====================");
    }

    public void testdelete() {
        log.debug("====================delete負向測試開始====================");
        try {
            XX_ZX0100.delete(null);
            log.debug("------------負向測試1失敗------------");
            fail();
        } catch (Exception e) {
            log.debug("------------負向測試1成功------------");
            log.debug("", e);
        }
        log.debug("====================delete負向測試結束====================");

        log.debug("====================delete正向測試開始====================");
        try {
            String EMP_ID = "11111";
            XX_ZX0100.delete(EMP_ID);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            fail();
        }
        try {
            String EMP_ID = "11112";
            XX_ZX0100.delete(EMP_ID);
            log.debug("------------正向測試1成功------------");
        } catch (Exception e) {
            log.debug("------------正向測試1失敗------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================delete正向測試結束====================");
    }

}
