package com.cathay.xx.qk.module;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * �ҲզW��    �{�����qKPI 
 * �Ҳ�ID    XX_QKK001
 * ���n����    �{�����qKPI
 *</pre>
 *
 * @author i9400562 ���W��
 * @since  2019/05/07
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XX_QKK001 {

    private static final String SQL_querryProgramProduce_001 = "com.cathay.xx.qk.module.XX_QKK001.SQL_querryProgramProduce_001";

    private static final String SQL_querryWorkOutDay_001 = "com.cathay.xx.qk.module.XX_QKK001.SQL_querryWorkOutDay_001";

    /**
     * �p��{�����q
     * @param reqMap
     * @param DIV_NO
     * @param EMP_ID,workingDay,standardList
     * @return List<Map>
     * @throws ModuleException
     */
    public List<Map> queryProgramProduce(Map reqMap, String DIV_NO, String EMP_ID, Integer workingDay, List<Map> standardList)
            throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "��O���i����");
        }

        if (standardList == null) {
            eie = this.getEie(eie, "KPI�зǰ}�C���i����");
        }

        String START_DATE = MapUtils.getString(reqMap, "START_DATE");
        if (StringUtils.isBlank(START_DATE)) {
            eie = this.getEie(eie, "�_�l�餣�i����");
        }

        String END_DATE = MapUtils.getString(reqMap, "END_DATE");
        if (StringUtils.isBlank(END_DATE)) {
            eie = this.getEie(eie, "�����餣�i����");
        }
        String KIND = MapUtils.getString(reqMap, "KIND");
        if (StringUtils.isBlank(KIND)) {
            eie = this.getEie(eie, "��էP�O���o����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet(); //���o�s�u

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

        List<Map> Program = VOTool.findToMaps(ds, SQL_querryProgramProduce_001);//�˹�@�U �����O�_���n

        List<Map> workout = this.querryWorkOutDay(DIV_NO, START_DATE, END_DATE);

        Map standardMap = standardList.get(0);
        BigDecimal FIVE = new BigDecimal("5");
        BigDecimal BD_workingDay = new BigDecimal(workingDay);
        BigDecimal BD_STD_LOW = new BigDecimal(MapUtils.getString(standardMap, "STD_LOW")).multiply(BD_workingDay).divide(FIVE)
                .setScale(1, RoundingMode.HALF_UP);
        BigDecimal BD_STD_UP = new BigDecimal(MapUtils.getString(standardMap, "STD_UP")).multiply(BD_workingDay).divide(FIVE)
                .setScale(1, RoundingMode.HALF_UP);
        for (Map PMap : Program) {
            PMap.put("A1", BD_STD_LOW);
            PMap.put("A2", BD_STD_UP);
            if (workout != null && !workout.isEmpty()) {
                String PMap_ID = MapUtils.getString(PMap, "EMP_ID");
                for (Map WMap : workout) {
                    if (PMap_ID.equals(MapUtils.getString(WMap, "EMP_ID"))) {
                        BigDecimal workoutDay = BD_workingDay.subtract(new BigDecimal(MapUtils.getString(WMap, "TOTAL_DAYS")));
                        BigDecimal LOWNUM = new BigDecimal(MapUtils.getString(standardMap, "STD_LOW")).multiply(workoutDay).divide(FIVE)
                                .setScale(1, RoundingMode.HALF_UP);
                        BigDecimal UPNUM = new BigDecimal(MapUtils.getString(standardMap, "STD_UP")).multiply(workoutDay).divide(FIVE)
                                .setScale(1, RoundingMode.HALF_UP);
                        PMap.put("A1", LOWNUM);
                        PMap.put("A2", UPNUM);
                    }
                }
            } 
        }

        return Program;
    }

    /**
     * �d�߽а��Ѽ�
     * @param DIV_NO
     * @param START_DATE
     * @param END_DATE
     * @return
     * @throws ModuleException
     */
    private List<Map> querryWorkOutDay(String DIV_NO, String START_DATE, String END_DATE) throws ModuleException {
        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "��O���i����");
        }

        if (StringUtils.isBlank(START_DATE)) {
            eie = this.getEie(eie, "�_�l�餣�i����");
        }

        if (StringUtils.isBlank(END_DATE)) {
            eie = this.getEie(eie, "�����餣�i����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);
        return VOTool.findToMaps(ds, SQL_querryWorkOutDay_001, false);
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
