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
 * <pre>
 * �ҲզW��    ����KPI�зǬd�߼Ҳ�
 * �Ҳ�ID    XX_QK0200
 * ���n����    ����KPI�d�߼Ҳ�
 *</pre>
 *
 * @author i9400562 ���W��
 * @since  2019/05/07
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XX_QK0200 {

    private static final String SQL_queryStandardByTYPE_ID_001 = "com.cathay.xx.qk.module.XX_QK0200.SQL_queryStandardByTYPE_ID_001";

    private static final String SQL_updateStandardSTD_001 = "com.cathay.xx.qk.module.XX_QK0200.SQL_updateStandardSTD_001";

    private static final String SQL_insertKPIDetail_001 = "com.cathay.xx.qk.module.XX_QK0200.SQL_insertKPIDetail_001";

    private static final String SQL_updateStandardDetail_001 = "com.cathay.xx.qk.module.XX_QK0200.SQL_updateStandardDetail_001";

    private static final String SQL_insertKPISTD_001 = "com.cathay.xx.qk.module.XX_QK0200.SQL_insertKPISTD_001";

    private static final String SQL_querryKPIDetailSNO_001 = "com.cathay.xx.qk.module.XX_QK0200.SQL_querryKPIDetailSNO_001";

    private static final String SQL_querryKPISNO_001 = "com.cathay.xx.qk.module.XX_QK0200.SQL_querryKPISNO_001";

    /**
     * �d��KPI�з�By��O
     * @param TYPE_ID
     * @return rtnList
     * @throws ModuleException
     */
    public List<Map> queryStandardByTYPE_ID(String TYPE_ID) throws ModuleException {

        if (StringUtils.isBlank(TYPE_ID)) {
            throw new ErrorInputException("���O���i����");
        }

        DataSet ds = Transaction.getDataSet(); //���o�s�u
        ds.setField("TYPE_ID", TYPE_ID);

        return VOTool.findToMaps(ds, SQL_queryStandardByTYPE_ID_001);
    }

    /**
     * �ק�KPI�зǤW�U��By TYPE_ID
     * @param reqMap
     * @param user
     * @throws ModuleException
     */
    public void updateStandardSTD(Map reqMap, UserObject user) throws ModuleException {

        ErrorInputException eie = null;

        String KPI_NAME = MapUtils.getString(reqMap, "KPI_NAME");
        if (StringUtils.isBlank(KPI_NAME)) {
            eie = this.getEie(eie, "�зǦW�٤��i����");
        }
        String STD_LOW = MapUtils.getString(reqMap, "STD_LOW");
        if (StringUtils.isBlank(STD_LOW)) {
            eie = this.getEie(eie, "�U�����i����");
        }
        String STD_UP = MapUtils.getString(reqMap, "STD_UP");
        if (StringUtils.isBlank(STD_UP)) {
            eie = this.getEie(eie, "�W�����i����");
        }
        String KPI_ID = MapUtils.getString(reqMap, "KPI_ID");
        if (StringUtils.isBlank(KPI_ID)) {
            eie = this.getEie(eie, "KPI_ID���i����");
        }
        String TYPE_ID = MapUtils.getString(reqMap, "TYPE_ID");
        if (StringUtils.isBlank(TYPE_ID)) {
            eie = this.getEie(eie, "���O���i����");
        }
        String UNIT = MapUtils.getString(reqMap, "UNIT");
        if (StringUtils.isBlank(UNIT)) {
            eie = this.getEie(eie, "��줣�i����");
        }
        String PERIOD = MapUtils.getString(reqMap, "PERIOD");
        if (StringUtils.isBlank(PERIOD)) {
            eie = this.getEie(eie, "����ԭz���i����");
        }
        String JUDGE = MapUtils.getString(reqMap, "JUDGE");
        if (StringUtils.isBlank(JUDGE)) {
            eie = this.getEie(eie, "�ƭȷN�q���i����");
        }
        String ACORD_ID = MapUtils.getString(reqMap, "ACORD_ID");
        if (StringUtils.isBlank(ACORD_ID)) {
            eie = this.getEie(eie, "�P�_�̾ڤ��i����");
        }
        String TYPE = MapUtils.getString(reqMap, "TYPE");
        if (StringUtils.isBlank(TYPE)) {
            eie = this.getEie(eie, "�������i����");
        }
        String MODULE_CLASS = MapUtils.getString(reqMap, "MODULE_CLASS");
        if (StringUtils.isBlank(MODULE_CLASS)) {
            eie = this.getEie(eie, "�Ҳդ��i����");
        }
        String MODULE_METHOD = MapUtils.getString(reqMap, "MODULE_METHOD");
        if (StringUtils.isBlank(MODULE_METHOD)) {
            eie = this.getEie(eie, "��k���i����");
        }
        String MODULE_PARAM = MapUtils.getString(reqMap, "MODULE_PARAM");
        if (StringUtils.isBlank(MODULE_PARAM)) {
            eie = this.getEie(eie, "�ѼƤ��i����");
        }
        if (user == null) {
            eie = this.getEie(eie, "UserObject���i����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();

        ds.setField("KPI_NAME", KPI_NAME);
        ds.setField("STD_LOW", STD_LOW);
        ds.setField("STD_UP", STD_UP);
        ds.setField("KPI_ID", KPI_ID);
        ds.setField("TYPE_ID", TYPE_ID);
        ds.setField("UNIT", UNIT);
        ds.setField("PERIOD", PERIOD);
        ds.setField("JUDGE", JUDGE);
        ds.setField("ACORD_ID", ACORD_ID);
        ds.setField("TYPE", TYPE);
        ds.setField("MODULE_CLASS", MODULE_CLASS);
        ds.setField("MODULE_METHOD", MODULE_METHOD);
        ds.setField("MODULE_PARAM", MODULE_PARAM);
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", DATE.currentTime());

        DBUtil.executeUpdate(ds, SQL_updateStandardSTD_001); //��ƳB�z

    }

    /**
     * �ק�KPI�зǲӶ�
     * @param reqMap
     * @throws ModuleException
     */
    public void updateStandardDetail(Map reqMap) throws ModuleException {

        ErrorInputException eie = null;

        String KPI_DETAIL = MapUtils.getString(reqMap, "KPI_DETAIL");
        if (StringUtils.isBlank(KPI_DETAIL)) {
            eie = this.getEie(eie, "�Ӷ��W�٤��i����");
        }
        String KPI_DETAIL_ID = MapUtils.getString(reqMap, "KPI_DETAIL_ID");
        if (StringUtils.isBlank(KPI_DETAIL_ID)) {
            eie = this.getEie(eie, "�Ӷ�ID���i����");
        }
        String KPI_ID = MapUtils.getString(reqMap, "KPI_ID");
        if (StringUtils.isBlank(KPI_ID)) {
            eie = this.getEie(eie, "KPI_ID���i����");
        }
        String ORI_ID = MapUtils.getString(reqMap, "ORI_ID");
        if (StringUtils.isBlank(KPI_ID)) {
            eie = this.getEie(eie, "��l�Ӷ�ID���i����");
        }
        String TYPE_ID = MapUtils.getString(reqMap, "TYPE_ID");
        if (StringUtils.isBlank(TYPE_ID)) {
            eie = this.getEie(eie, "���O���i����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();

        ds.setField("KPI_DETAIL", KPI_DETAIL);
        ds.setField("KPI_DETAIL_ID", KPI_DETAIL_ID);
        ds.setField("KPI_ID", KPI_ID);
        ds.setField("TYPE_ID", TYPE_ID);
        ds.setField("ORI_ID", ORI_ID);

        DBUtil.executeUpdate(ds, SQL_updateStandardDetail_001); //��ƳB�z

    }

    /**
     * �s�WKPI�зǲӶ�
     * @param reqMap
     * @throws ModuleException
     */
    public void insertKPIDetail(Map reqMap) throws ModuleException {

        ErrorInputException eie = null;

        String KPI_DETAIL = MapUtils.getString(reqMap, "KPI_DETAIL");
        if (StringUtils.isBlank(KPI_DETAIL)) {
            eie = this.getEie(eie, "�Ӷ��W�٤��i����");
        }
        String KPI_DETAIL_ID = MapUtils.getString(reqMap, "KPI_DETAIL_ID");
        if (StringUtils.isBlank(KPI_DETAIL_ID)) {
            eie = this.getEie(eie, "�Ӷ�ID���i����");
        }
        String TYPE_ID = MapUtils.getString(reqMap, "TYPE_ID");
        if (StringUtils.isBlank(TYPE_ID)) {
            eie = this.getEie(eie, "���O���i����");
        }
        String KPI_ID = MapUtils.getString(reqMap, "KPI_ID");
        if (StringUtils.isBlank(KPI_ID)) {
            eie = this.getEie(eie, "KPI�з�ID���i����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();

        ds.setField("SNO", querryKPIDetailSNO(TYPE_ID, KPI_ID));
        ds.setField("KPI_DETAIL", KPI_DETAIL);
        ds.setField("KPI_DETAIL_ID", KPI_DETAIL_ID);
        ds.setField("TYPE_ID", TYPE_ID);
        ds.setField("KPI_ID", KPI_ID);

        DBUtil.executeUpdate(ds, SQL_insertKPIDetail_001); //��ƳB�z

    }

    /**
     * �s�WKPI�з�
     * @param reqMap
     * @param user
     * @throws ModuleException
     */
    public void insertKPISTD(Map reqMap, UserObject user) throws ModuleException {

        ErrorInputException eie = null;

        String KPI_NAME = MapUtils.getString(reqMap, "KPI_NAME");
        if (StringUtils.isBlank(KPI_NAME)) {
            eie = this.getEie(eie, "�зǦW�٤��i����");
        }
        String STD_LOW = MapUtils.getString(reqMap, "STD_LOW");
        if (StringUtils.isBlank(STD_LOW)) {
            eie = this.getEie(eie, "�U�����i����");
        }
        String STD_UP = MapUtils.getString(reqMap, "STD_UP");
        if (StringUtils.isBlank(STD_UP)) {
            eie = this.getEie(eie, "�W�����i����");
        }
        String TYPE_ID = MapUtils.getString(reqMap, "TYPE_ID");
        if (StringUtils.isBlank(TYPE_ID)) {
            eie = this.getEie(eie, "���O���i����");
        }
        String UNIT = MapUtils.getString(reqMap, "UNIT");
        if (StringUtils.isBlank(UNIT)) {
            eie = this.getEie(eie, "��줣�i����");
        }
        String PERIOD = MapUtils.getString(reqMap, "PERIOD");
        if (StringUtils.isBlank(PERIOD)) {
            eie = this.getEie(eie, "����ԭz���i����");
        }
        String JUDGE = MapUtils.getString(reqMap, "JUDGE");
        if (StringUtils.isBlank(JUDGE)) {
            eie = this.getEie(eie, "�ƭȷN�q���i����");
        }
        String ACORD_ID = MapUtils.getString(reqMap, "ACORD_ID");
        if (StringUtils.isBlank(ACORD_ID)) {
            eie = this.getEie(eie, "�P�_�̾ڤ��i����");
        }
        String TYPE = MapUtils.getString(reqMap, "TYPE");
        if (StringUtils.isBlank(TYPE)) {
            eie = this.getEie(eie, "�������i����");
        }
        String MODULE_CLASS = MapUtils.getString(reqMap, "MODULE_CLASS");
        if (StringUtils.isBlank(MODULE_CLASS)) {
            eie = this.getEie(eie, "�Ҳդ��i����");
        }
        String MODULE_METHOD = MapUtils.getString(reqMap, "MODULE_METHOD");
        if (StringUtils.isBlank(MODULE_METHOD)) {
            eie = this.getEie(eie, "��k���i����");
        }
        String MODULE_PARAM = MapUtils.getString(reqMap, "MODULE_PARAM");
        if (StringUtils.isBlank(MODULE_PARAM)) {
            eie = this.getEie(eie, "�ѼƤ��i����");
        }
        if (user == null) {
            eie = this.getEie(eie, "UserObject���i����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();

        Map SNOMap = querryKPISNO(TYPE_ID);
        ds.setField("STD_LOW", STD_LOW);
        ds.setField("KPI_NAME", KPI_NAME);
        ds.setField("STD_UP", STD_UP);
        ds.setField("KPI_ID", SNOMap.get("KPI_ID"));
        ds.setField("TYPE_ID", TYPE_ID);
        ds.setField("UNIT", UNIT);
        ds.setField("PERIOD", PERIOD);
        ds.setField("JUDGE", JUDGE);
        ds.setField("ACORD_ID", ACORD_ID);
        ds.setField("TYPE", TYPE);
        ds.setField("SNO", SNOMap.get("SNO"));
        ds.setField("MODULE_CLASS", MODULE_CLASS);
        ds.setField("MODULE_METHOD", MODULE_METHOD);
        ds.setField("MODULE_PARAM", MODULE_PARAM);
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", DATE.currentTime());

        DBUtil.executeUpdate(ds, SQL_insertKPISTD_001); //��ƳB�z

    }

    /**
     * �d�߲Ӷ�SNO�Ǹ�
     * @param TYPE_ID
     * @param KPI_ID
     * @return SNO
     * @throws ModuleException
     */
    private int querryKPIDetailSNO(String TYPE_ID, String KPI_ID) throws ModuleException {

        DataSet ds = Transaction.getDataSet();
        ds.setField("TYPE_ID", TYPE_ID);
        ds.setField("KPI_ID", KPI_ID);
        return MapUtils.getIntValue(VOTool.findOneToMap(ds, SQL_querryKPIDetailSNO_001, false), "SNO", 1);
    }

    /**
     * �d��KPI�з�SNO�Ǹ�
     * @param TYPE_ID
     * @return SNOMap
     * @throws ModuleException
     */

    private Map querryKPISNO(String TYPE_ID) throws ModuleException {

        DataSet ds = Transaction.getDataSet();
        ds.setField("TYPE_ID", TYPE_ID);
       
        Map SNOMap = VOTool.findOneToMap(ds, SQL_querryKPISNO_001);
        SNOMap.put("SNO", MapUtils.getInteger(SNOMap, "SNO", 1));
        return SNOMap;
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
