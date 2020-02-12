package com.cathay.xx.qk.trx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.cathay.common.bo.ReturnMessage;
import com.cathay.common.exception.DataNotFoundException;
import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.exception.OverCountLimitException;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.message.MessageHelper;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.trx.UCBean;
import com.cathay.common.util.IConstantMap;
import com.cathay.util.ReturnCode;
import com.cathay.util.Transaction;
import com.cathay.xx.qk.module.XX_QK0100;
import com.cathay.xx.qk.module.XX_QK0300;
import com.igsapp.common.trx.ServiceException;
import com.igsapp.common.trx.TxException;
import com.igsapp.common.util.annotation.CallMethod;
import com.igsapp.common.util.annotation.TxBean;
import com.igsapp.wibc.dataobj.Context.RequestContext;
import com.igsapp.wibc.dataobj.Context.ResponseContext;

/**
 * <pre> 
 * �{���\��    �ҥ~�ɶ��n�O
 * �{���W��    XXQK_0300.java
 * �@�~�覡    ONLINE
 * </pre>
 * @author �����o
 * @since 2019/06/21
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@TxBean
public class XXQK_0300 extends UCBean {

    /** log */
    private static final Logger log = Logger.getLogger(XXQK_0300.class);

    /** �� TxBean �{���X�@�Ϊ� ResponseContext */
    private ResponseContext resp;

    /** �� TxBean �{���X�@�Ϊ� ReturnMessage */
    private ReturnMessage msg;

    private UserObject user;

    /** 
     * �мg�����O�� start() �H�j���C�� Dispatcher �I�s method �ɳ�����{���۩w����l�ʧ@
     */
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        //�@�w�n invoke super.start() �H�����v���ˮ�
        super.start(req);
        //�I�s�۩w����l�ʧ@
        initApp(req);
        return null;
    }

    /**
     * �{���۩w����l�ʧ@�A�q�`�����X ResponseContext, UserObject, 
     * �γ]�w ReturnMessage �� response code.
     */
    private void initApp(RequestContext req) throws TxException {
        // �إߦ� TxBean �q�Ϊ�����
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        user = this.getUserObject(req);

        // ���N ReturnMessage �� reference �[�� response context
        resp.addOutputData(IConstantMap.ErrMsg, msg);

        // �b Cathay �q�`�u���@�� page �b�e�� display�A�ҥH�i�H���]�w
        resp.setResponseCode("success");
    }

    /**
     * ��l�d�ߨҥ~�n�O���
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url = "/XX/QK/XXQK_0300/XXQK0300.jsp")
    public ResponseContext doPrompt(RequestContext req) {

        try {
            String DIV_NO = user.getDivNo();
            List<Map> rtnlowerException = new XX_QK0300().queryExcepttime(null, DIV_NO, "");
            List<Map> rtnException = LowerCase(rtnlowerException);
            resp.addOutputData("rtnException", VOTool.toJSON(rtnException));

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�d�ߧ���");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error(dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                if (me.getRootException() instanceof OverCountLimitException) {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�d�ߵ��ƶW�X�t�έ���A���Y�p�d�߽d��");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�d�ߥ���");
                }
            }
        } catch (Exception e) {
            log.error("�d�ߥ���", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�d�ߥ���", e, req);
        }

        return resp;
    }

    /**
    * �ק�d�߸�Ʈ榡
    * @param list
    * @return
    */
    private List<Map> LowerCase(List<Map> list) {
        List<Map> rtnException = new ArrayList();
        for (Map EXPMAP : list) {

            Map<String, Object> obdmap = new HashMap();
            Set<String> se = EXPMAP.keySet();
            for (String set : se) {
                obdmap.put(set.toLowerCase(), EXPMAP.get(set));
            }
            rtnException.add(obdmap);
        }
        return rtnException;
    }

    /**
    * �d�ߤH���ɸ��
    * @param req
    * @return
    */
    @CallMethod(action = "QueryEmployee")
    public ResponseContext doQueryEmployee(RequestContext req) {
        try {
            String DIV_NO = user.getDivNo();
            List<Map> rtnEMP = new XX_QK0100().queryEmployeeList(DIV_NO, "");
            resp.addOutputData("rtnEMP", rtnEMP);

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�d�ߧ���");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error(dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                if (me.getRootException() instanceof OverCountLimitException) {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�d�ߵ��ƶW�X�t�έ���A���Y�p�d�߽d��");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�d�ߥ���");
                }
            }
        } catch (Exception e) {
            log.error("�d�ߥ���", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�d�ߥ���", e, req);
        }

        return resp;
    }

    /**
     * �s�W�ҥ~�n�O���
     * @param req
     * @return
     */
    @CallMethod(action = "insertEXP")
    public ResponseContext doInsertEXP(RequestContext req) {
        try {
            Map reqMap = VOTool.requestToMap(req);
            String DIV_NO = user.getDivNo();
            String[] EMP_NM_spl = MapUtils.getString(reqMap, "EMP_NM").split(",");
            String[] EMP_ID_spl = MapUtils.getString(reqMap, "EMP_ID").split(",");
            XX_QK0300 the_XX_QK0300 = new XX_QK0300();
            Transaction.begin();
            try {
                for (int i = 0; i < EMP_NM_spl.length; i++) {
                    reqMap.put("EMP_NM", EMP_NM_spl[i]);
                    reqMap.put("EMP_ID", EMP_ID_spl[i]);
                    the_XX_QK0300.insertExcepttime(reqMap, DIV_NO, user);
                }
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            try {

                List<Map> rtnlowerException = new XX_QK0300().queryExcepttime(null, DIV_NO, "");
                List<Map> rtnException = LowerCase(rtnlowerException);
                resp.addOutputData("rtnException", VOTool.toJSON(rtnException));

            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�W����");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�s�W����", me, req);
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�s�W����", e, req);
        }

        return resp;
    }

    /**
     * �ק�ҥ~�n�O���
     * @param req
     * @return
     */
    @CallMethod(action = "updateEXP")
    public ResponseContext doUpdateEXP(RequestContext req) {
        try {
            Map reqMap = VOTool.requestToMap(req);
            String DIV_NO = user.getDivNo();

            Transaction.begin();
            try {

                new XX_QK0300().updateExcepttime(reqMap, DIV_NO, user);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }

            try {
                List<Map> rtnlowerException = new XX_QK0300().queryExcepttime(null, DIV_NO, "");
                List<Map> rtnException = LowerCase(rtnlowerException);
                resp.addOutputData("rtnException", VOTool.toJSON(rtnException));
            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "��s����");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "��s����", me, req);
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "��s����", e, req);
        }
        return resp;
    }

    /**
     * �R���ҥ~�n�O���
     * @param req
     * @return
     */
    @CallMethod(action = "deleteEXP")
    public ResponseContext doDeleteEXP(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);
            String DIV_NO = user.getDivNo();

            Transaction.begin();
            try {

                new XX_QK0300().deleteExcepttime(reqMap, DIV_NO);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }

            try {
                List<Map> rtnlowerException = new XX_QK0300().queryExcepttime(null, DIV_NO, "");
                List<Map> rtnException = LowerCase(rtnlowerException);
                resp.addOutputData("rtnException", VOTool.toJSON(rtnException));
            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�R������");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�R������", me, req);
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�R������", e, req);
        }

        return resp;
    }
}
