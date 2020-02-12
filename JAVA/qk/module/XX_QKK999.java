package com.cathay.xx.qk.module;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;

/**
 * 
 * <pre>
 * 模組名稱    全科KPI計算模組
 * 模組ID    XX_QKK999
 * 概要說明    全科KPI計算模組
 * </pre>
 *
 * @author i9400562 黃名玄
 * @since  2019/05/08
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XX_QKK999 {

    /** log */
    private static final Logger log = Logger.getLogger(XX_QKK999.class);

    /**
     * 計算全科個人KPI數據
     * @param reqMap 
     * @param DIV_NO 
     * @param EMP_ID
     * @param workingDay 工作天
     * @param standardList KPI標準
     * @return rtnList
     * @throws ModuleException
     */
    public List<Map> calKPIData(Map reqMap, String DIV_NO, String EMP_ID, int workingDay, List<Map> standardList) throws ModuleException {

        if (StringUtils.isBlank(DIV_NO)) {

        }
        if (StringUtils.isBlank(EMP_ID)) {

        }
        if (standardList == null || standardList.isEmpty()) {

        }

        //TODO VALID

        Map tempMap = new HashMap();
        tempMap.put("reqMap", reqMap);
        tempMap.put("DIV_NO", DIV_NO);
        tempMap.put("EMP_ID", EMP_ID);
        tempMap.put("workingDay", workingDay);
        tempMap.put("standardList", standardList);

        List<Object[]> params = new ArrayList<Object[]>();
        for (Map standardMap : standardList) {
            String[] paramsStrArr = StringUtils.split(MapUtils.getString(standardMap, "MODULE_PARAM"), ',');
            ArrayList<Object> paramsArray = new ArrayList<Object>();
            for (String str : paramsStrArr) {
                paramsArray.add(MapUtils.getObject(tempMap, str));
            }
            params.add(paramsArray.toArray());
        }

        List<Map> rtnList = new ArrayList();
        String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
        String KIND = MapUtils.getString(reqMap, "KIND");
        if ("1".equals(KIND)) {
            rtnList = new XX_QK0100().queryEmployeeList(DIV_NO, EMP_ID);
        } else {
            rtnList = new XX_QK0100().queryGroupEmpList(DIV_NO, GRP_ID, EMP_ID);
        }

        List<List> allstandardList = new ArrayList();
        for (int i = 0; i < standardList.size(); i++) {
            Object[] param = params.get(i);
            Map standardMap = standardList.get(i);

            try {
                allstandardList.add(cast(this.runClassMethod(MapUtils.getString(standardMap, "MODULE_CLASS"),
                    MapUtils.getString(standardMap, "MODULE_METHOD"), param)));
            } catch (Exception me) {
                //查詢錯誤 繼續往下
                log.error("查詢" + MapUtils.getString(standardMap, "KPI_NAME") + "異常", me);
            }
        }

        for (Map rtnMap : rtnList) {
            for (List<Map> list : allstandardList) {
                for (Map map : list) {
                    if (MapUtils.getString(rtnMap, "EMP_ID").equals(MapUtils.getString(map, "EMP_ID"))) {
                        rtnMap.putAll(map);
                    }
                }
            }
        }

        return rtnList;
    }

    /**
     * 執行
     * 
     * @param RULE_CLS String 執行模組名稱
     * @param RULE_MTD String 執行模組方法
     * @param params Object[] 執行參數
     * @throws Exception 
     */
    private Object runClassMethod(String RULE_CLS, String RULE_MTD, Object[] params) throws Exception {
        ErrorInputException eie = null;

        if (StringUtils.isBlank(RULE_CLS)) {
            eie = getEie(eie, "執行模組名稱不得為空");
        }
        if (StringUtils.isBlank(RULE_MTD)) {
            eie = getEie(eie, "執行模組方法不得為空");
        }

        if (eie != null) {
            throw eie;
        }

        // 使用invoke執行對應的模組方法
        Class targetClass = Class.forName(RULE_CLS);
        Object targetMethodCaller = targetClass.newInstance();

        // 呼叫程式
        return this.getCallingMethod(targetClass, RULE_MTD, params).invoke(targetMethodCaller, params);
    }

    /**
     * 依照傳入class與method名稱和參數, 取得對應呼叫方法
     * 
     * @param targetClass Class 目標class
     * @param RULE_MTD String 欲呼叫method名稱
     * @param params Object[] 參數
     * @return Method
     * @throws NoSuchMethodException
     */
    private Method getCallingMethod(Class targetClass, String RULE_MTD, Object[] params) throws NoSuchMethodException {
        // 取得目標class相同function名稱的參數列表

        Method[] methods = targetClass.getMethods();
        List<Method> methodMethodList = new ArrayList();
        List<Class[]> methodParamsList = new ArrayList();
        for (Method method : methods) {
            // 只需判斷RULE_MTD method
            if (RULE_MTD.equals(method.getName())) {
                methodMethodList.add(method);
                methodParamsList.add(method.getParameterTypes());
            }
        }

        // 建立params Class對照List
        List<Set<Class>> paramsCalzzList = new ArrayList();
        if (params != null && params.length != 0) {
            for (Object obj : params) {
                // 取得本身的class
                Set<Class> subList = new HashSet();

                // 如果參數為null
                if (obj == null) {
                    subList.add(null);
                } else {
                    // 其他情況才為型別
                    Class clazz = obj.getClass();

                    subList.add(clazz);
                    subList.addAll(Arrays.asList(clazz.getInterfaces()));
                }

                paramsCalzzList.add(subList);
            }
        }

        // 以method為主體, 比對paramsClazzList有沒有對應參數
        for (int i = 0; i < methodParamsList.size(); i++) {
            Class[] clazzs = methodParamsList.get(i);

            // 如果此次method params與傳入參數個數不相符, 則繼續下一個loop
            if (clazzs.length != paramsCalzzList.size()) {
                continue;
            }

            boolean methodIsMatch = true;
            for (int j = 0; j < clazzs.length; j++) {
                Class clazz = clazzs[j];

                Set<Class> paramsCalzzs = paramsCalzzList.get(j);
                // 傳入參數可能是null
                if (!paramsCalzzs.contains(null) && !paramsCalzzs.contains(clazz)) {
                    methodIsMatch = false;
                    break;
                }
            }

            // 如果都符合, 則表示該method符合, 回傳method
            if (methodIsMatch) {
                return methodMethodList.get(i);
            }
        }

        // 拼NoSuchMethodException錯誤訊息
        StringBuilder errMsg = new StringBuilder(targetClass.getName());
        errMsg.append(" ").append(RULE_MTD).append("(");
        if (params != null && params.length > 0) {
            errMsg.append(params[0] == null ? null : params[0].getClass().getName());
            for (int i = 1; i < params.length; i++) {
                errMsg.append(", ").append(params[i] == null ? null : params[i].getClass().getName());
            }
        }
        throw new NoSuchMethodException(errMsg.append(")").toString());
    }

    private static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
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
