package com.cathay.xx.qk.module.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cathay.common.exception.ModuleException;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.util.AuthUtil;
import com.cathay.util.CTestCase;
import com.cathay.xx.qk.module.XX_QK0300;
import common.Logger;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class XX_QK0300Test extends CTestCase {

    public XX_QK0300Test(String name) {
        super(name);

    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private static final Logger log = Logger.getLogger(XX_QK0300Test.class);

    public void testQueryException() {

        XX_QK0300 theXX_QK0300 = new XX_QK0300();
        log.debug("----------------------------------------testQueryException case1 start------------------------------------------");
        try {
            Map reqMap = new HashMap();
            reqMap.put("START_DATE", "2019-05-01");
            reqMap.put("END_DATE", "2019-05-20");
            String EMP_ID = "";
            String DIV_NO = "9400500";
            List rtnList = theXX_QK0300.queryException(reqMap, DIV_NO, EMP_ID);
            log.debug(rtnList);
            List rtnList2 = theXX_QK0300.queryExceptionDays(reqMap, DIV_NO, EMP_ID);
            log.debug(rtnList2);


        } catch (ModuleException e) {
            log.debug(e);
        }
        log.debug("----------------------------------------testQueryException case2 start------------------------------------------");
        try {
            Map reqMap = new HashMap();
            reqMap.put("START_DATE", "2019-05-01");
            reqMap.put("END_DATE", "2019-05-20");
            String EMP_ID = "A18557172D";
            String DIV_NO = "9400500";
            List rtnList = theXX_QK0300.queryException(reqMap, DIV_NO, EMP_ID);
            log.debug(rtnList);

        } catch (ModuleException e) {
            log.debug(e);
        }
        log.debug("----------------------------------------testQueryException case3 start------------------------------------------");
        try {
            Map reqMap = new HashMap();
            String EMP_ID = "";
            String DIV_NO = "";
            List rtnList = theXX_QK0300.queryException(reqMap, DIV_NO, EMP_ID);
            log.debug(rtnList);

        } catch (ModuleException e) {
            log.debug(e);
        }
    }
    public void testQueryExceptionDays() {
        XX_QK0300 theXX_QK0300 = new XX_QK0300();
        log.debug("------------------------------testQueryExceptionDays case1 start--------------------------------------");
        try {
            Map reqMap = new HashMap();
            reqMap.put("START_DATE", "2019-05-01");
            reqMap.put("END_DATE", "2019-05-20");
            String EMP_ID = "";
            String DIV_NO = "9400500";
            List rtnList = theXX_QK0300.queryExceptionDays(reqMap, DIV_NO, EMP_ID);
            log.debug(rtnList);

        } catch (ModuleException e) {
            log.debug(e);
        }
        log.debug("------------------------------testQueryExceptionDays case2 start--------------------------------------");
        try {
            Map reqMap = new HashMap();
            reqMap.put("START_DATE", "2019-05-01");
            reqMap.put("END_DATE", "2019-05-20");
            String EMP_ID = "A18557172D";
            String DIV_NO = "9400500";
            List rtnList = theXX_QK0300.queryExceptionDays(reqMap, DIV_NO, EMP_ID);
            log.debug(rtnList);

        } catch (ModuleException e) {
            log.debug(e);
        }
        log.debug("-------------------------------testQueryExceptionDays case3 start-------------------------------------");
        try {
            Map reqMap = new HashMap();
            String EMP_ID = "";
            String DIV_NO = "";
            List rtnList = theXX_QK0300.queryExceptionDays(reqMap, DIV_NO, EMP_ID);
            log.debug(rtnList);

        } catch (ModuleException e) {
            log.debug(e);
        }
    }
    public void testInsertExceptions() {
        XX_QK0300 theXX_QK0300 = new XX_QK0300();
        
        log.debug("------------------------------testInsertExceptions case1 start--------------------------------------");
        try {
            Map reqMap = new HashMap();
            UserObject user = new AuthUtil().getUserObjByID("A18557172D");
            reqMap.put("EMP_ID", "A18557172D");
            reqMap.put("EXC_DATE", "2019-05-15");
            reqMap.put("EXC_HOURS", "4");
            reqMap.put("MEMO", "111");
            reqMap.put("EMP_NM", "¤ýO»¨");
            String DIV_NO = "9400500";
           
            theXX_QK0300.insertException(reqMap, DIV_NO, user);
            log.debug("------------------------------testInsertExceptions case1 end OK--------------------------------------");
        } catch (ModuleException e) {
            log.debug(e);
            log.debug("------------------------------testInsertExceptions case1 end fail--------------------------------------");
        }
        log.debug("------------------------------testInsertExceptions case2 start--------------------------------------");
        try {
            Map reqMap = new HashMap();
            String DIV_NO = "";
            UserObject user = new AuthUtil().getUserObjByID("A18557172D");
            theXX_QK0300.insertException(reqMap, DIV_NO, user);
            log.debug("------------------------------testInsertExceptions case2 end OK--------------------------------------");
        } catch (ModuleException e) {
            log.debug(e);
            log.debug("------------------------------testInsertExceptions case2 end fail--------------------------------------");
        }
    }

}
