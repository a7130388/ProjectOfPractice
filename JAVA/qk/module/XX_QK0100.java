package com.cathay.xx.qk.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.im.util.VOTool;
import com.cathay.util.Transaction;
import com.igsapp.db.DataSet;

/**
 * 
 * <pre>
* 模組名稱    全科KPI查詢模組
* 模組ID    XX_QK0100
* 概要說明    全科KPI查詢模組
 *</pre>
 *
 * @author i9400562 黃名玄
 * @since  2019/05/07
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XX_QK0100 {

    private static final String SQL_queryGroupInfo_001 = "com.cathay.xx.qk.module.XX_QK0100.SQL_queryGroupInfo_001";

    private static final String SQL_queryEmployee_001 = "com.cathay.xx.qk.module.XX_QK0100.SQL_queryEmployee_001";

    private static final String SQL_queryGroupEmp_001 = "com.cathay.xx.qk.module.XX_QK0100.SQL_queryGroupEmp_001";

    private static final String SQL_queryDetailByTYPE_ID_001 = "com.cathay.xx.qk.module.XX_QK0100.SQL_queryDetailByTYPE_ID_001";

    /**
     * 查詢KPI細項By科別
     * @param DIV_NO
     * @return rtnList
     * @throws ModuleException
     */
    public List<Map> queryDetailByTYPE_ID(String TYPE_ID) throws ModuleException {

        if (StringUtils.isBlank(TYPE_ID)) {
            throw new ErrorInputException("類別不可為空");
        }

        DataSet ds = Transaction.getDataSet(); //取得連線
        ds.setField("TYPE_ID", TYPE_ID);

        return VOTool.findToMaps(ds, SQL_queryDetailByTYPE_ID_001);
    }

   /**
    * 查詢組別
    * @param DIV_NO
    * @param GRP_ID
    * @param EMP_ID
    * @return
    * @throws ModuleException
    */
    public List<Map> queryGroupInfo(String DIV_NO, String GRP_ID, String EMP_ID) throws ModuleException {

        if (StringUtils.isBlank(DIV_NO)) {
            throw new ErrorInputException("科別不可為空");
        }

        DataSet ds = Transaction.getDataSet(); //取得連線

        if (StringUtils.isNotBlank(GRP_ID)) {
            ds.setField("GRP_ID", GRP_ID);
        }

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }

        ds.setField("DIV_NO", DIV_NO); //設定參數

        return VOTool.findToMaps(ds, SQL_queryGroupInfo_001);
    }

    /**
     * 查詢科別
     * @return
     * @throws ModuleException
     */
    public List<Map> queryDIV() throws ModuleException {

        List<Map> rtnList = new ArrayList();//TODO 目前只會有三個開發科
        Map Map = new HashMap();
        Map.put("DIV_NO", "9400500");
        Map.put("DIV_NM", "台中開發科");
        rtnList.add(Map);
        Map = new HashMap();
        Map.put("DIV_NO", "9300600");
        Map.put("DIV_NM", "投資程設科");
        rtnList.add(Map);
        Map = new HashMap();
        Map.put("DIV_NO", "9200800");
        Map.put("DIV_NM", "壽險程設科");
        rtnList.add(Map);

        return rtnList;
    }

    /**
     * 讀取全科人員檔資料
     * @param DIV_NO,EMP_ID
     * @param EMP_ID
     * @return
     * @throws ModuleException
     */
    public List<Map> queryEmployeeList(String DIV_NO, String EMP_ID) throws ModuleException {

        if (StringUtils.isBlank(DIV_NO)) {
            throw new ErrorInputException("科別不可為空");
        }

        DataSet ds = Transaction.getDataSet();

        ds.setField("DIV_NO", DIV_NO);

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }

        return VOTool.findToMaps(ds, SQL_queryEmployee_001);
    }

    /**
     * 讀取組別人員檔資料
     * @param DIV_NO,EMP_ID
     * @param EMP_ID
     * @return
     * @throws ModuleException
     */
    public List<Map> queryGroupEmpList(String DIV_NO, String GRP_ID, String EMP_ID) throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "科別不可為空");
        }
        if (StringUtils.isBlank(GRP_ID)) {
            eie = this.getEie(eie, "組別不可為空");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();

        ds.setField("DIV_NO", DIV_NO);
        ds.setField("GRP_ID", GRP_ID);

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }

        return VOTool.findToMaps(ds, SQL_queryGroupEmp_001);
    }

    /**
     * 設定錯誤訊息
     * @param eie
     * @param msg
     */
    private ErrorInputException getEie(ErrorInputException eie, String msg) {
        if (eie == null) {
            eie = new ErrorInputException();
        }
        eie.appendMessage(msg);
        return eie;
    }
}
