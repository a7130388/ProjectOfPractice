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
 * �ҲզW��    ����KPI�p��Ҳ�
 * �Ҳ�ID    XX_QKK999
 * ���n����    ����KPI�p��Ҳ�
 * </pre>
 *
 * @author i9400562 ���W��
 * @since  2019/05/08
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XX_QKK999 {

    /** log */
    private static final Logger log = Logger.getLogger(XX_QKK999.class);

    /**
     * �p�����ӤHKPI�ƾ�
     * @param reqMap 
     * @param DIV_NO 
     * @param EMP_ID
     * @param workingDay �u�@��
     * @param standardList KPI�з�
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
                //�d�߿��~ �~�򩹤U
                log.error("�d��" + MapUtils.getString(standardMap, "KPI_NAME") + "���`", me);
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
     * ����
     * 
     * @param RULE_CLS String ����ҲզW��
     * @param RULE_MTD String ����Ҳդ�k
     * @param params Object[] ����Ѽ�
     * @throws Exception 
     */
    private Object runClassMethod(String RULE_CLS, String RULE_MTD, Object[] params) throws Exception {
        ErrorInputException eie = null;

        if (StringUtils.isBlank(RULE_CLS)) {
            eie = getEie(eie, "����ҲզW�٤��o����");
        }
        if (StringUtils.isBlank(RULE_MTD)) {
            eie = getEie(eie, "����Ҳդ�k���o����");
        }

        if (eie != null) {
            throw eie;
        }

        // �ϥ�invoke����������Ҳդ�k
        Class targetClass = Class.forName(RULE_CLS);
        Object targetMethodCaller = targetClass.newInstance();

        // �I�s�{��
        return this.getCallingMethod(targetClass, RULE_MTD, params).invoke(targetMethodCaller, params);
    }

    /**
     * �̷ӶǤJclass�Pmethod�W�٩M�Ѽ�, ���o�����I�s��k
     * 
     * @param targetClass Class �ؼ�class
     * @param RULE_MTD String ���I�smethod�W��
     * @param params Object[] �Ѽ�
     * @return Method
     * @throws NoSuchMethodException
     */
    private Method getCallingMethod(Class targetClass, String RULE_MTD, Object[] params) throws NoSuchMethodException {
        // ���o�ؼ�class�ۦPfunction�W�٪��ѼƦC��

        Method[] methods = targetClass.getMethods();
        List<Method> methodMethodList = new ArrayList();
        List<Class[]> methodParamsList = new ArrayList();
        for (Method method : methods) {
            // �u�ݧP�_RULE_MTD method
            if (RULE_MTD.equals(method.getName())) {
                methodMethodList.add(method);
                methodParamsList.add(method.getParameterTypes());
            }
        }

        // �إ�params Class���List
        List<Set<Class>> paramsCalzzList = new ArrayList();
        if (params != null && params.length != 0) {
            for (Object obj : params) {
                // ���o������class
                Set<Class> subList = new HashSet();

                // �p�G�ѼƬ�null
                if (obj == null) {
                    subList.add(null);
                } else {
                    // ��L���p�~�����O
                    Class clazz = obj.getClass();

                    subList.add(clazz);
                    subList.addAll(Arrays.asList(clazz.getInterfaces()));
                }

                paramsCalzzList.add(subList);
            }
        }

        // �Hmethod���D��, ���paramsClazzList���S�������Ѽ�
        for (int i = 0; i < methodParamsList.size(); i++) {
            Class[] clazzs = methodParamsList.get(i);

            // �p�G����method params�P�ǤJ�ѼƭӼƤ��۲�, �h�~��U�@��loop
            if (clazzs.length != paramsCalzzList.size()) {
                continue;
            }

            boolean methodIsMatch = true;
            for (int j = 0; j < clazzs.length; j++) {
                Class clazz = clazzs[j];

                Set<Class> paramsCalzzs = paramsCalzzList.get(j);
                // �ǤJ�Ѽƥi��Onull
                if (!paramsCalzzs.contains(null) && !paramsCalzzs.contains(clazz)) {
                    methodIsMatch = false;
                    break;
                }
            }

            // �p�G���ŦX, �h��ܸ�method�ŦX, �^��method
            if (methodIsMatch) {
                return methodMethodList.get(i);
            }
        }

        // ��NoSuchMethodException���~�T��
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
     * �]�w���~�T��
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
