package com.cathay.xx.qk.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.im.util.VOTool;
import com.cathay.util.Transaction;
import com.igsapp.db.DataSet;

/**
 * 
 * <pre>
 * 模組名稱    發生問題率KPI 
 * 模組ID    XX_QKK004
 * 概要說明    發生問題率KPI
 *</pre>
 *
 * @author i9400562 黃名玄
 * @since  2019/05/07
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XX_QKK004 {

    private static final String SQL_querryProblem_001 = "com.cathay.xx.qk.module.XX_QKK004.SQL_querryProblem_001";

    /**
     * 計算發生問題率
     * @param reqMap,EMP_ID
     * @return List<Map>
     * @throws ModuleException
     */
    public List<Map> querryProblem(Map reqMap, String EMP_ID) throws ModuleException {

        ErrorInputException eie = null;

        String DIV_NO = MapUtils.getString(reqMap, "DIV_NO");
        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "科別不可為空");
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

        DataSet ds = Transaction.getDataSet(); //取得連線
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);
        ds.setField("DIV_NO", DIV_NO);
        List<Map> Memo = VOTool.findToMaps(ds, SQL_querryProblem_001);

        List<String> IDList = new ArrayList();
        for (Map MemoMap : Memo) {
            String GetID = MapUtils.getString(MemoMap, "MEMO");
            IDList.add(StringUtils.substring(GetID, StringUtils.indexOf(GetID, ":") + 1, StringUtils.indexOf(GetID, ";")));
        }
        String KIND = MapUtils.getString(reqMap, "KIND");
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");

        List<Map> Employee_Problem = new ArrayList();

        if ("1".equals(KIND)) {
            Employee_Problem = new XX_QK0100().queryEmployeeList(DIV_NO, EMP_ID);
        } else {
            Employee_Problem = new XX_QK0100().queryGroupEmpList(DIV_NO, GRP_ID, "");
        }

        for (Map empMap : Employee_Problem) {
            for (String ID : IDList) {
                if (MapUtils.getString(empMap, "EMP_ID").equals(ID)) {
                    empMap.put("PB_NUM", MapUtils.getIntValue(empMap, "PB_NUM") + 1);
                }

            }
        }
        return Employee_Problem;
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
