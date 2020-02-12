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
 * 模組名稱    全科KPI計算模組
 * 模組ID    XX_QK0101
 * 概要說明    全科KPI計算模組
 *</pre>
 *
 * @author i9400562 黃名玄
 * @since  2019/05/08
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XX_QK0101 {

    private static final String SQL_querryProblemHandleTime_001 = "com.cathay.xx.qk.module.XX_QK0101.SQL_querryProblemHandleTime_001";

    private static final String SQL_queryEmployee_001 = "com.cathay.xx.qk.module.XX_QK0101.SQL_queryEmployee_001";

    private static final String SQL_querryProgramProduce_001 = "com.cathay.xx.qk.module.XX_QK0101.SQL_querryProgramProduce_001";

    private static final String SQL_querryProgramOverdue_001 = "com.cathay.xx.qk.module.XX_QK0101.SQL_querryProgramOverdue_001";

    private static final String SQL_querryProblem_001 = "com.cathay.xx.qk.module.XX_QK0101.SQL_querryProblem_001";

    private static final String SQL_querryProblem_002 = "com.cathay.xx.qk.module.XX_QK0101.SQL_querryProblem_002";

    private static final String SQL_querryProgramError_001 = "com.cathay.xx.qk.module.XX_QK0101.SQL_querryProgramError_001";

    private static final String SQL_queryGroupEmp_001 = "com.cathay.xx.qk.module.XX_QK0101.SQL_queryGroupEmp_001";

    private static final String SQL_querryProgramProduce_002 = "com.cathay.xx.qk.module.XX_QK0101.SQL_querryProgramProduce_002";

    /**
     * 計算全科個人KPI數據
     * @param DIV_NO
     * @return rtnList
     * @throws ModuleException
     */
    public List<Map> calDataByDIV_NO(Map reqMap, String DIV_NO, String EMP_ID, Integer workingDay, List<Map> standardList)
            throws ModuleException {
        
        String KIND = MapUtils.getString(reqMap, "KIND");
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
        List<Map> rtnList = new ArrayList();
        
        if("1".equals(KIND)){
            rtnList = queryEmployeeList(DIV_NO, EMP_ID);
        }else{
            rtnList = queryGroupEmpList(DIV_NO, GRP_ID,EMP_ID);
        }  
       
        List<Map> PBPassTimeList = querryProblemHandleTime(reqMap, DIV_NO, EMP_ID);
        List<Map> ProgramProduceList = queryProgramProduce(reqMap, DIV_NO, EMP_ID, workingDay, standardList);
        List<Map> ProgramOverdueList = querryProgramOverdue(reqMap, DIV_NO, EMP_ID);
        List<Map> ProblemList = new ArrayList();
        try {
            ProblemList = querryProblem(reqMap, DIV_NO, EMP_ID);
        } catch (ModuleException me) {
        }
        List<Map> ProgramErrorList = querryProgramError(reqMap, DIV_NO, EMP_ID);

        for (Map rtnMap : rtnList) {
            for (Map PBPassTimeMap : PBPassTimeList) {
                if (MapUtils.getString(rtnMap, "EMP_ID").equals(MapUtils.getString(PBPassTimeMap, "EMP_ID"))) {
                    rtnMap.putAll(PBPassTimeMap);
                }
            }
            for (Map ProgramProduceMap : ProgramProduceList) {
                if (MapUtils.getString(rtnMap, "EMP_ID").equals(MapUtils.getString(ProgramProduceMap, "EMP_ID"))) {
                    rtnMap.putAll(ProgramProduceMap);
                }
            }
            for (Map ProgramOverdueMap : ProgramOverdueList) {
                if (MapUtils.getString(rtnMap, "EMP_ID").equals(MapUtils.getString(ProgramOverdueMap, "EMP_ID"))) {
                    rtnMap.putAll(ProgramOverdueMap);
                }
            }
            for (Map ProblemMap : ProblemList) {
                if (MapUtils.getString(rtnMap, "EMP_ID").equals(MapUtils.getString(ProblemMap, "EMP_ID"))) {
                    rtnMap.putAll(ProblemMap);
                }
            }
            for (Map ProgramErrorMap : ProgramErrorList) {
                if (MapUtils.getString(rtnMap, "EMP_ID").equals(MapUtils.getString(ProgramErrorMap, "EMP_ID"))) {
                    rtnMap.putAll(ProgramErrorMap);
                }
            }
        }
       
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
    public List<Map> queryGroupEmpList(String DIV_NO, String GRP_ID,String EMP_ID) throws ModuleException {

        DataSet ds = Transaction.getDataSet();
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("GRP_ID", GRP_ID);
        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }  
        return VOTool.findToMaps(ds, SQL_queryGroupEmp_001);
    }
    
    /**
     * 計算程式產量
     * @param standardList,detailList,rtnMap,workingDay,dataMap
     * @return void
     * @throws ModuleException
     */
    public List<Map> queryProgramProduce(Map reqMap, String DIV_NO, String EMP_ID, Integer workingDay, List<Map> standardList)
            throws ModuleException {

        ErrorInputException eie = null;

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
        String KIND = MapUtils.getString(reqMap, "KIND");
        if (StringUtils.isBlank(KIND)) {
            eie = this.getEie(eie, "科組判別不得為空");
        }        

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet(); //取得連線

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }
        
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
        if (StringUtils.isNotBlank(GRP_ID)) {
            ds.setField("GRP_ID", GRP_ID);
        }
        
        ds.setField("KIND", KIND);
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);

        List<Map> Program = VOTool.findToMaps(ds, SQL_querryProgramProduce_001);
        ds = Transaction.getDataSet();
        
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);
        List<Map> workout = new ArrayList();
        try{
            workout = VOTool.findToMaps(ds, SQL_querryProgramProduce_002);
        }catch(ModuleException me){
        }
        
        for (Map PMap : Program) {
            PMap.put("A1", MapUtils.getFloat(standardList.get(0), "STD_LOW") * workingDay / 5);
            PMap.put("A2", MapUtils.getFloat(standardList.get(0), "STD_UP") * workingDay / 5);
            for(Map WMap : workout){
                if(MapUtils.getString(PMap, "EMP_ID").equals(MapUtils.getString(WMap, "EMP_ID"))){
                    Float workoutDay =  workingDay - MapUtils.getFloatValue(WMap, "TOTAL_DAYS");
                    Float LOWNUM = (float)(Math.round(MapUtils.getFloat(standardList.get(0), "STD_LOW") * workoutDay / 5 *10))/10;
                    Float UPNUM = (float)(Math.round(MapUtils.getFloat(standardList.get(0), "STD_UP") * workoutDay / 5 *10))/10;
                    PMap.put("A1", LOWNUM);
                    PMap.put("A2", UPNUM);                  
                }
            }           
        }

        return Program;
    }

    /**
     * 計算程式逾期量
     * @param detailList,rtnMap, dataMap
     * @return void
     * @throws ModuleException
     */
    public List<Map> querryProgramOverdue(Map reqMap, String DIV_NO, String EMP_ID) throws ModuleException {
        ErrorInputException eie = null;

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
        
        String KIND = MapUtils.getString(reqMap, "KIND");
        if (StringUtils.isBlank(KIND)) {
            eie = this.getEie(eie, "科組判別不得為空");
        }    

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet(); //取得連線

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
        if (StringUtils.isNotBlank(GRP_ID)) {
            ds.setField("GRP_ID", GRP_ID);
        }
        
        ds.setField("KIND", KIND);
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);

        return VOTool.findToMaps(ds, SQL_querryProgramOverdue_001);

    }

    /**
     * 計算不符合開發規範
     * @param detailList,rtnMap,dataMap
     * @return void
     * @throws ModuleException
     */
    public List<Map> querryProgramError(Map reqMap, String DIV_NO, String EMP_ID) throws ModuleException {
        ErrorInputException eie = null;

        String START_DATE = MapUtils.getString(reqMap, "START_DATE");
        if (StringUtils.isBlank(START_DATE)) {
            eie = this.getEie(eie, "起始日不可為空");
        }

        String END_DATE = MapUtils.getString(reqMap, "END_DATE");
        if (StringUtils.isBlank(END_DATE)) {
            eie = this.getEie(eie, "結束日不可為空");
        }
        String KIND = MapUtils.getString(reqMap, "KIND");
        if (StringUtils.isBlank(KIND)) {
            eie = this.getEie(eie, "科組判別不得為空");
        }    

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet(); //取得連線

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }
        if (StringUtils.isNotBlank(DIV_NO)) {
            ds.setField("DIV_NO", DIV_NO);
        }
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
        if (StringUtils.isNotBlank(GRP_ID)) {
            ds.setField("GRP_ID", GRP_ID);
        }
        
        ds.setField("KIND", KIND);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);

        return VOTool.findToMaps(ds, SQL_querryProgramError_001);

    }

    /**
     * 計算發生問題率
     * @param detailList,rtnMap,dataMap
     * @return void
     * @throws ModuleException
     */
    public List<Map> querryProblem(Map reqMap, String DIV_NO, String EMP_ID) throws ModuleException {

        ErrorInputException eie = null;

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
        List<Map> Memo = VOTool.findToMaps(ds, SQL_querryProblem_001);
        List<String> IDList = new ArrayList();
        for (Map MemoMap : Memo) {
            String GetID = MapUtils.getString(MemoMap, "MEMO");
            IDList.add(StringUtils.substring(GetID, StringUtils.indexOf(GetID, ":") + 1, StringUtils.indexOf(GetID, ";")));
        }
        ds = Transaction.getDataSet(); //取得連線
        String KIND = MapUtils.getString(reqMap, "KIND");
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("GRP_ID", GRP_ID);
        List<Map> Employee_Problem = new ArrayList();
        
        if("1".equals(KIND)){
            Employee_Problem = this.queryEmployeeList(DIV_NO,EMP_ID);//VOTool.findToMaps(ds, SQL_querryProblem_002);
        }else{
            Employee_Problem = VOTool.findToMaps(ds, SQL_querryProblem_002);
        }
        

        for (Map empMap : Employee_Problem) {
            empMap.put("PB_NUM", null);
            for (String ID : IDList) {
                if ("L224151293".equals(ID)) {
                    ID = "L28071785F";
                }
                if (MapUtils.getString(empMap, "EMP_ID").equals(ID)) {
                    if (!MapUtils.getBooleanValue(empMap, "PB_NUM")) {
                        empMap.put("PB_NUM", 0);
                    }
                    empMap.put("PB_NUM", MapUtils.getInteger(empMap, "PB_NUM") + 1);
                }

            }
        }
        return Employee_Problem;
    }

    /**
     * 查詢問題處理時效
     * @param reqMap,DIV_NO,EMP_ID
     * @return List<Map>
     * @throws ModuleException
     */
    public List<Map> querryProblemHandleTime(Map reqMap, String DIV_NO, String EMP_ID) throws ModuleException {

        ErrorInputException eie = null;

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

        String KIND = MapUtils.getString(reqMap, "KIND");
        if (StringUtils.isBlank(KIND)) {
            eie = this.getEie(eie, "科組判別不得為空");
        }    
        
        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet(); //取得連線

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
        if (StringUtils.isNotBlank(GRP_ID)) {
            ds.setField("GRP_ID", GRP_ID);
        }
        
        ds.setField("KIND", KIND);
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);

        return VOTool.findToMaps(ds, SQL_querryProblemHandleTime_001);
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
