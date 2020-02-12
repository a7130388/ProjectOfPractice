package com.cathay.xx.qk.module;

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
 * �ҲզW��    ���ŦX�}�o�W�dKPI 
 * �Ҳ�ID    XX_QKK003
 * ���n����    ���ŦX�}�o�W�dKPI
 *</pre>
 *
 * @author i9400562 ���W��
 * @since  2019/05/07
 *
 */
@SuppressWarnings({ "rawtypes" })
public class XX_QKK003 {

    private static final String SQL_querryProgramError_001 = "com.cathay.xx.qk.module.XX_QKK003.SQL_querryProgramError_001";
    
    /**
     * �p�⤣�ŦX�}�o�W�d
     * @param reqMap,EMP_ID
     * @return List<Map>
     * @throws ModuleException
     */
    public List<Map> querryProgramError(Map reqMap, String EMP_ID) throws ModuleException {
        ErrorInputException eie = null;

        String DIV_NO = MapUtils.getString(reqMap, "DIV_NO");
        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "��O���i����");
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
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("KIND", KIND);
        ds.setField("START_DATE", START_DATE);
        ds.setField("END_DATE", END_DATE);

        return VOTool.findToMaps(ds, SQL_querryProgramError_001);

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
