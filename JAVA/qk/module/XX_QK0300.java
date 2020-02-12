package com.cathay.xx.qk.module;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.util.DATE;
import com.cathay.common.util.db.DBUtil;
import com.cathay.util.Transaction;
import com.igsapp.db.DataSet;

/**
 * 
 *<pre>
* 模組名稱    例外狀況查詢及維護模組
* 模組ID  XX_QK0300
* 概要說明    例外狀況查詢及維護模組
 *</pre>
 *
 * @author i9400561 王新豪
 * @since  2019/06/17
 *
 */
@SuppressWarnings("rawtypes")
public class XX_QK0300 {

    private static final String SQL_queryException_001 = "com.cathay.xx.qk.module.XX_QK0300.SQL_queryException_001";

    private static final String SQL_insertException_001 = "com.cathay.xx.qk.module.XX_QK0300.SQL_insertException_001";

    private static final String SQL_updateException_001 = "com.cathay.xx.qk.module.XX_QK0300.SQL_updateException_001";

    private static final String SQL_queryExceptionDays_001 = "com.cathay.xx.qk.module.XX_QK0300.SQL_queryExceptionDays_001";

    private static final String SQL_deleteException_001 = "com.cathay.xx.qk.module.XX_QK0300.SQL_deleteException_001";

    /**
     * 查詢例外狀況
     * @param reqMap,DIV_NO, EMP_ID
     * @return rtnList
     * @throws ModuleException
     */
    public List<Map> queryExcepttime(Map reqMap, String DIV_NO, String EMP_ID) throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "單位代號不可為空");
        }

        if (eie != null) {
            throw eie;
        }
        DataSet ds = Transaction.getDataSet();

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }

        ds.setField("DIV_NO", DIV_NO);

        return VOTool.findToMaps(ds, SQL_queryException_001);
    }

    /**
     * 新增例外狀況
     * @param reqMap,DIV_NO,user
     * @return 
     * @throws ModuleException
     */
    public void insertExcepttime(Map reqMap, String DIV_NO, UserObject user) throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "單位代號不可為空");
        }
        String EMP_ID = MapUtils.getString(reqMap, "EMP_ID");
        if (StringUtils.isBlank(EMP_ID)) {
            eie = this.getEie(eie, "員工編號不可為空");
        }
        String EXC_DATE = MapUtils.getString(reqMap, "EXC_DATE");
        if (StringUtils.isBlank(EXC_DATE)) {
            eie = this.getEie(eie, "例外日期不可為空");
        }
        String EXC_HOURS = MapUtils.getString(reqMap, "EXC_HOURS");
        if (StringUtils.isBlank(EXC_HOURS)) {
            eie = this.getEie(eie, "例外時間不可為空");
        }
        String MEMO = MapUtils.getString(reqMap, "MEMO");
        if (StringUtils.isBlank(MEMO)) {
            eie = this.getEie(eie, "例外原因不可為空");
        }
        String EMP_NM = MapUtils.getString(reqMap, "EMP_NM");
        if (StringUtils.isBlank(EMP_NM)) {
            eie = this.getEie(eie, "員工姓名不可為空");
        }
        if (user == null) {
            eie = this.getEie(eie, "使用者資訊不可為空");
        }
        if (eie != null) {
            throw eie;
        }
        DataSet ds = Transaction.getDataSet();

        ds.setField("DIV_NO", DIV_NO);
        ds.setField("EMP_ID", EMP_ID);
        ds.setField("EXC_DATE", EXC_DATE);
        ds.setField("EXC_HOURS", EXC_HOURS);
        ds.setField("MEMO", MEMO);
        ds.setField("EMP_NM", EMP_NM);
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", DATE.currentTime());

        DBUtil.executeUpdate(ds, SQL_insertException_001);
    }

    /**
     * 刪除例外狀況
     * @param reqMap,DIV_NO,user
     * @return 
     * @throws ModuleException
     */
    public void deleteExcepttime(Map reqMap, String DIV_NO) throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "單位代號不可為空");
        }
        String EMP_ID = MapUtils.getString(reqMap, "EMP_ID");
        if (StringUtils.isBlank(EMP_ID)) {
            eie = this.getEie(eie, "員工編號不可為空");
        }
        String EXC_DATE = MapUtils.getString(reqMap, "EXC_DATE");
        if (StringUtils.isBlank(EXC_DATE)) {
            eie = this.getEie(eie, "例外日期不可為空");
        }

        if (eie != null) {
            throw eie;
        }
        DataSet ds = Transaction.getDataSet();

        ds.setField("DIV_NO", DIV_NO);
        ds.setField("EMP_ID", EMP_ID);
        ds.setField("EXC_DATE", EXC_DATE);

        DBUtil.executeUpdate(ds, SQL_deleteException_001);
    }

    /**
     * 修改例外狀況
     * @param reqMap,DIV_NO,user
     * @return 
     * @throws ModuleException
     */
    public void updateExcepttime(Map reqMap, String DIV_NO, UserObject user) throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "單位代號不可為空");
        }
        String EMP_ID = MapUtils.getString(reqMap, "EMP_ID");
        if (StringUtils.isBlank(EMP_ID)) {
            eie = this.getEie(eie, "員工編號不可為空");
        }
        String EXC_DATE = MapUtils.getString(reqMap, "EXC_DATE");
        if (StringUtils.isBlank(EXC_DATE)) {
            eie = this.getEie(eie, "例外日期不可為空");
        }
        String EXC_HOURS = MapUtils.getString(reqMap, "EXC_HOURS");
        if (StringUtils.isBlank(EXC_HOURS)) {
            eie = this.getEie(eie, "例外時間不可為空");
        }
        String MEMO = MapUtils.getString(reqMap, "MEMO");
        if (StringUtils.isBlank(MEMO)) {
            eie = this.getEie(eie, "例外原因不可為空");
        }
        if (user == null) {
            eie = this.getEie(eie, "使用者資訊不可為空");
        }
        if (eie != null) {
            throw eie;
        }
        DataSet ds = Transaction.getDataSet();

        ds.setField("DIV_NO", DIV_NO);
        ds.setField("EMP_ID", EMP_ID);
        ds.setField("EXC_DATE", EXC_DATE);
        ds.setField("EXC_HOURS", EXC_HOURS);
        ds.setField("MEMO", MEMO);
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", DATE.currentTime());

        DBUtil.executeUpdate(ds, SQL_updateException_001);
    }

    /**
     * 查詢例外狀況天數
     * @param reqMap,DIV_NO, EMP_ID
     * @return rtnList
     * @throws ModuleException
     */
    public List<Map> queryExceptDays(Map reqMap, String DIV_NO, String EMP_ID) throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "單位代號不可為空");
        }
        String START_DATE = MapUtils.getString(reqMap, "START_DATE");
        if (StringUtils.isBlank(START_DATE)) {
            eie = this.getEie(eie, "起始日不可為空");
        }
        String END_DATE = MapUtils.getString(reqMap, "END_DATE");
        if (StringUtils.isBlank(END_DATE)) {
            eie = this.getEie(eie, "結束日不可為空");
        }
        if (eie != null) {
            throw eie;
        }
        DataSet ds = Transaction.getDataSet();

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }

        ds.setField("DIV_NO", DIV_NO);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);

        return VOTool.findToMaps(ds, SQL_queryExceptionDays_001);
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
