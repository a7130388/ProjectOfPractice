package com.cathay.xx.qk.module.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.util.AuthUtil;
import com.cathay.common.util.DATE;
import com.cathay.util.DBTestCase;
import com.cathay.xx.qk.module.XX_QK0200;

public class XX_QK0100Test extends DBTestCase {
    private static final Logger log = Logger.getLogger(XX_QK0100Test.class);
    
    public XX_QK0100Test(String arg) {
        super(arg);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testQueryFutTypeList() {
     
        XX_QK0200 theXX_QK0200 = new XX_QK0200();
        log.debug("*************************************************** test queryStandardByTYPE_ID strat ***************************************************");
        try {
            String TYPE_ID="9400500";            
            List<Map> rtnList = theXX_QK0200.queryStandardByTYPE_ID(TYPE_ID);
            log.debug(rtnList);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test queryStandardByTYPE_ID  ok ***************************************************");
        
        log.debug("*************************************************** test queryStandardByTYPE_ID Empty ***************************************************");
        try {
            String TYPE_ID=" ";            
            List<Map> rtnList = theXX_QK0200.queryStandardByTYPE_ID(TYPE_ID);
            log.debug(rtnList);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test queryStandardByTYPE_ID Empty ok ***************************************************");
       
        
        log.debug("*************************************************** test updateStandardSTD strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            reqMap.put("KPI_NAME", "程式產量");
            reqMap.put("STD_LOW", "3.5");
            reqMap.put("STD_UP", "4");
            reqMap.put("KPI_ID", "10001");
            reqMap.put("TYPE_ID", "9400500");
            reqMap.put("UNIT", "人天");
            reqMap.put("PERIOD", "程式產量週平均");
            reqMap.put("JUDGE", "U");
            reqMap.put("ACORD_ID", "TOTAL_DAY");
            reqMap.put("TYPE", "D");
            reqMap.put("MODULE_CLASS", "com.cathay.xx.qk.module.XX_QKK001");
            reqMap.put("MODULE_METHOD", "queryProgramProduce");
            reqMap.put("MODULE_PARAM", "reqMap,DIV_NO,EMP_ID,workingDay,standardList");
                       
            UserObject user1 = new AuthUtil().getUserObjByID("E18172309C");
            
            theXX_QK0200.updateStandardSTD(reqMap,user1);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test updateStandardSTD  ok ***************************************************");
      
        
        log.debug("*************************************************** test updateStandardSTD Empty strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            UserObject user = null;            
            theXX_QK0200.updateStandardSTD(reqMap,user);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test updateStandardSTD Empty ok ***************************************************");
      
        log.debug("*************************************************** test updateStandardDetail strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            reqMap.put("KPI_DETAIL", "標準人天下限");
            reqMap.put("KPI_DETAIL_ID", "A1");
            reqMap.put("KPI_ID", "10001");
            reqMap.put("TYPE_ID", "10001");
            reqMap.put("ORI_ID", "A1");
                       
            UserObject user1 = new AuthUtil().getUserObjByID("E18172309C");
            
            theXX_QK0200.updateStandardDetail(reqMap);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test updateStandardDetail  ok ***************************************************");
      
        
        log.debug("*************************************************** test updateStandardDetail Empty strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            UserObject user = null;            
            theXX_QK0200.updateStandardDetail(reqMap);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test updateStandardDetail Empty ok ***************************************************");
      
        log.debug("*************************************************** test updateStandardDetail strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            reqMap.put("KPI_DETAIL", "標準人天下限");
            reqMap.put("KPI_DETAIL_ID", "A1");
            reqMap.put("KPI_ID", "10001");
            reqMap.put("TYPE_ID", "9400500");
            reqMap.put("ORI_ID", "A1");
                       
            UserObject user1 = new AuthUtil().getUserObjByID("E18172309C");
            
            theXX_QK0200.updateStandardDetail(reqMap);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test updateStandardDetail  ok ***************************************************");
      
        
        log.debug("*************************************************** test updateStandardDetail Empty strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            UserObject user = null;            
            theXX_QK0200.updateStandardDetail(reqMap);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test updateStandardDetail Empty ok ***************************************************");
      
        log.debug("*************************************************** test insertKPIDetail strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            reqMap.put("KPI_DETAIL", "標準人天下限");
            reqMap.put("KPI_DETAIL_ID", "A3");
            reqMap.put("KPI_ID", "10001");
            reqMap.put("TYPE_ID", "9400500");

            UserObject user1 = new AuthUtil().getUserObjByID("E18172309C");
            
            theXX_QK0200.insertKPIDetail(reqMap);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test insertKPIDetail  ok ***************************************************");
      
        log.debug("*************************************************** test insertKPIDetail SNO=1 strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            reqMap.put("KPI_DETAIL", "標準人天下限");
            reqMap.put("KPI_DETAIL_ID", "A3");
            reqMap.put("KPI_ID", "10029");
            reqMap.put("TYPE_ID", "9400500");

            UserObject user1 = new AuthUtil().getUserObjByID("E18172309C");
            
            theXX_QK0200.insertKPIDetail(reqMap);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test insertKPIDetail SNO=1 ok ***************************************************");
      
        
        log.debug("*************************************************** test insertKPIDetail Empty strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            UserObject user = null;            
            theXX_QK0200.insertKPIDetail(reqMap);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test insertKPIDetail Empty ok ***************************************************");
      
        
        log.debug("*************************************************** test insertKPISTD strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            reqMap.put("KPI_NAME", "程式產量");
            reqMap.put("STD_LOW", "3.5");
            reqMap.put("STD_UP", "4");
            reqMap.put("KPI_ID", "10001");
            reqMap.put("TYPE_ID", "9400500");
            reqMap.put("UNIT", "人天");
            reqMap.put("PERIOD", "程式產量週平均");
            reqMap.put("JUDGE", "U");
            reqMap.put("ACORD_ID", "TOTAL_DAY");
            reqMap.put("TYPE", "D");
            reqMap.put("MODULE_CLASS", "com.cathay.xx.qk.module.XX_QKK001");
            reqMap.put("MODULE_METHOD", "queryProgramProduce");
            reqMap.put("MODULE_PARAM", "reqMap,DIV_NO,EMP_ID,workingDay,standardList");

            UserObject user1 = new AuthUtil().getUserObjByID("E18172309C");
            
            theXX_QK0200.insertKPISTD(reqMap,user1);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test insertKPISTD  ok ***************************************************");
      
        log.debug("*************************************************** test insertKPISTD SNO=1 strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            reqMap.put("KPI_NAME", "程式產量");
            reqMap.put("STD_LOW", "3.5");
            reqMap.put("STD_UP", "4");          
            reqMap.put("TYPE_ID", "123456");
            reqMap.put("UNIT", "人天");
            reqMap.put("PERIOD", "程式產量週平均");
            reqMap.put("JUDGE", "U");
            reqMap.put("ACORD_ID", "TOTAL_DAY");
            reqMap.put("TYPE", "D");
            reqMap.put("MODULE_CLASS", "com.cathay.xx.qk.module.XX_QKK001");
            reqMap.put("MODULE_METHOD", "queryProgramProduce");
            reqMap.put("MODULE_PARAM", "reqMap,DIV_NO,EMP_ID,workingDay,standardList");

            UserObject user1 = new AuthUtil().getUserObjByID("E18172309C");
            
            theXX_QK0200.insertKPISTD(reqMap,user1);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test insertKPISTD SNO=1 ok ***************************************************");
        
        log.debug("*************************************************** test insertKPISTD Empty strat ***************************************************");
        try {
            Map reqMap = new HashMap();            
            UserObject user = null;            
            theXX_QK0200.insertKPISTD(reqMap,user);
            //log.debug(rtnList);
            log.debug(reqMap);
        } catch (Exception e) {
            log.debug(e);
        }        
        log.debug("*************************************************** test insertKPISTD Empty ok ***************************************************");
      
    }
    
    public static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }

}
