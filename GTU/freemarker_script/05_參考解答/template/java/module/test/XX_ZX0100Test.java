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

        log.debug("====================insert�t�V���ն}�l====================");
        try {
            XX_ZX0100.insert(null, null);
            log.debug("------------�t�V����1����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����1���\------------");
            log.debug("", e);
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("BIRTHDAY", "TTTT");
            XX_ZX0100.insert(reqMap, user);
            log.debug("------------�t�V����2����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����2���\------------");
            log.debug("", e);
        }
        log.debug("====================insert�t�V���յ���====================");
        log.debug("====================insert���V���ն}�l====================");
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11111");
            reqMap.put("BIRTHDAY", "1991-01-01");
            reqMap.put("DIV_NO", "11");
            reqMap.put("EMP_NAME", "11");
            reqMap.put("POSITION", "11");
            XX_ZX0100.insert(reqMap, user);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            assertFalse(true);
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11112");
            XX_ZX0100.insert(reqMap, user);
            log.debug("------------���V����2���\------------");
        } catch (Exception e) {
            log.debug("------------���V����2����------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================insert���V���յ���====================");
    }

    public void testupdate() {

        UserObject user = new AuthUtil().getUserObjByID("A28248646B");

        log.debug("====================update�t�V���ն}�l====================");
        try {
            XX_ZX0100.update(null, null);
            log.debug("------------�t�V����1����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����1���\------------");
            log.debug("", e);
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("BIRTHDAY", "TTTTT");
            XX_ZX0100.update(reqMap, user);
            log.debug("------------�t�V����2����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����2���\------------");
            log.debug("", e);
        }

        log.debug("====================update�t�V���յ���====================");

        log.debug("====================update���V���ն}�l====================");
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11111");
            reqMap.put("BIRTHDAY", "1992-02-02");
            reqMap.put("DIV_NO", "11");
            reqMap.put("EMP_NAME", "22");
            reqMap.put("POSITION", "22");
            XX_ZX0100.update(reqMap, user);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            fail();
        }
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11112");
            XX_ZX0100.update(reqMap, user);
            log.debug("------------���V����2���\------------");
        } catch (Exception e) {
            log.debug("------------���V����2����------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================update���V���յ���====================");
    }

    public void testquery() {

        log.debug("====================query���V���ն}�l====================");
        try {
            Map reqMap = new HashMap();
            reqMap.put("EMP_ID", "11111");
            reqMap.put("DIV_NO", "11");
            XX_ZX0100.query(reqMap);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================query���V���յ���====================");
    }

    public void testconfirm() {
        log.debug("====================confirm�t�V���ն}�l====================");
        try {
            XX_ZX0100.confirm(null);
            log.debug("------------�t�V����1����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����1���\------------");
            log.debug("", e);
        }
        log.debug("====================confirm�t�V���յ���====================");
        log.debug("====================confirm���V���ն}�l====================");
        try {
            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID("11111");
            XX_ZX0100.confirm(vo);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================confirm���V���յ���====================");
    }

    public void testreject() {
        log.debug("====================reject�t�V���ն}�l====================");
        try {
            XX_ZX0100.reject(null);
            log.debug("------------�t�V����1����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����1���\------------");
            log.debug("", e);
        }
        log.debug("====================reject�t�V���յ���====================");
        log.debug("====================reject���V���ն}�l====================");
        try {
            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID("11111");
            XX_ZX0100.reject(vo);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================reject���V���յ���====================");
    }

    public void testapprove() {
        log.debug("====================approve�t�V���ն}�l====================");
        try {
            XX_ZX0100.approve(null);
            log.debug("------------�t�V����1����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����1���\------------");
            log.debug("", e);
        }
        log.debug("====================approve�t�V���յ���====================");
        log.debug("====================approve���V���ն}�l====================");
        try {
            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID("11111");
            XX_ZX0100.approve(vo);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================approve���V���յ���====================");
    }

    public void testdelete() {
        log.debug("====================delete�t�V���ն}�l====================");
        try {
            XX_ZX0100.delete(null);
            log.debug("------------�t�V����1����------------");
            fail();
        } catch (Exception e) {
            log.debug("------------�t�V����1���\------------");
            log.debug("", e);
        }
        log.debug("====================delete�t�V���յ���====================");

        log.debug("====================delete���V���ն}�l====================");
        try {
            String EMP_ID = "11111";
            XX_ZX0100.delete(EMP_ID);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            fail();
        }
        try {
            String EMP_ID = "11112";
            XX_ZX0100.delete(EMP_ID);
            log.debug("------------���V����1���\------------");
        } catch (Exception e) {
            log.debug("------------���V����1����------------");
            log.debug("", e);
            fail();
        }
        log.debug("====================delete���V���յ���====================");
    }

}
