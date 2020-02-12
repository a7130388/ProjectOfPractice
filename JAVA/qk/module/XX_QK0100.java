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
* �ҲզW��    ����KPI�d�߼Ҳ�
* �Ҳ�ID    XX_QK0100
* ���n����    ����KPI�d�߼Ҳ�
 *</pre>
 *
 * @author i9400562 ���W��
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
     * �d��KPI�Ӷ�By��O
     * @param DIV_NO
     * @return rtnList
     * @throws ModuleException
     */
    public List<Map> queryDetailByTYPE_ID(String TYPE_ID) throws ModuleException {

        if (StringUtils.isBlank(TYPE_ID)) {
            throw new ErrorInputException("���O���i����");
        }

        DataSet ds = Transaction.getDataSet(); //���o�s�u
        ds.setField("TYPE_ID", TYPE_ID);

        return VOTool.findToMaps(ds, SQL_queryDetailByTYPE_ID_001);
    }

   /**
    * �d�߲էO
    * @param DIV_NO
    * @param GRP_ID
    * @param EMP_ID
    * @return
    * @throws ModuleException
    */
    public List<Map> queryGroupInfo(String DIV_NO, String GRP_ID, String EMP_ID) throws ModuleException {

        if (StringUtils.isBlank(DIV_NO)) {
            throw new ErrorInputException("��O���i����");
        }

        DataSet ds = Transaction.getDataSet(); //���o�s�u

        if (StringUtils.isNotBlank(GRP_ID)) {
            ds.setField("GRP_ID", GRP_ID);
        }

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }

        ds.setField("DIV_NO", DIV_NO); //�]�w�Ѽ�

        return VOTool.findToMaps(ds, SQL_queryGroupInfo_001);
    }

    /**
     * �d�߬�O
     * @return
     * @throws ModuleException
     */
    public List<Map> queryDIV() throws ModuleException {

        List<Map> rtnList = new ArrayList();//TODO �ثe�u�|���T�Ӷ}�o��
        Map Map = new HashMap();
        Map.put("DIV_NO", "9400500");
        Map.put("DIV_NM", "�x���}�o��");
        rtnList.add(Map);
        Map = new HashMap();
        Map.put("DIV_NO", "9300600");
        Map.put("DIV_NM", "���{�]��");
        rtnList.add(Map);
        Map = new HashMap();
        Map.put("DIV_NO", "9200800");
        Map.put("DIV_NM", "���I�{�]��");
        rtnList.add(Map);

        return rtnList;
    }

    /**
     * Ū������H���ɸ��
     * @param DIV_NO,EMP_ID
     * @param EMP_ID
     * @return
     * @throws ModuleException
     */
    public List<Map> queryEmployeeList(String DIV_NO, String EMP_ID) throws ModuleException {

        if (StringUtils.isBlank(DIV_NO)) {
            throw new ErrorInputException("��O���i����");
        }

        DataSet ds = Transaction.getDataSet();

        ds.setField("DIV_NO", DIV_NO);

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID);
        }

        return VOTool.findToMaps(ds, SQL_queryEmployee_001);
    }

    /**
     * Ū���էO�H���ɸ��
     * @param DIV_NO,EMP_ID
     * @param EMP_ID
     * @return
     * @throws ModuleException
     */
    public List<Map> queryGroupEmpList(String DIV_NO, String GRP_ID, String EMP_ID) throws ModuleException {

        ErrorInputException eie = null;

        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "��O���i����");
        }
        if (StringUtils.isBlank(GRP_ID)) {
            eie = this.getEie(eie, "�էO���i����");
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
