package com.cathay.xx.qk.trx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cathay.common.bo.ReturnMessage;
import com.cathay.common.exception.DataNotFoundException;
import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.exception.OverCountLimitException;
import com.cathay.common.im.util.MessageUtil;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.message.MessageHelper;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.trx.UCBean;
import com.cathay.common.util.IConstantMap;
import com.cathay.util.ReturnCode;
import com.cathay.util.Transaction;
import com.cathay.xx.qk.module.XX_QK0100;
import com.cathay.xx.qk.module.XX_QK0200;
import com.igsapp.common.trx.ServiceException;
import com.igsapp.common.trx.TxException;
import com.igsapp.common.util.annotation.CallMethod;
import com.igsapp.common.util.annotation.TxBean;
import com.igsapp.wibc.dataobj.Context.RequestContext;
import com.igsapp.wibc.dataobj.Context.ResponseContext;

/**
 * <pre> 
 * �{���\��    KPI�зǬd�ߤκ��@
 * �{���W��    XXQK_0200.java
 * �@�~�覡    ONLINE
 * ���n����    (1) �d�ߧ@�~ �w �ھڨϥΪ̿�ܬ�O�B�էO�A���KPI�зǸ��
 * (2) �s�W�@�~ �w �s�WKPI�зǸ�ơC
 * (3) �ק�@�~ �w �ק�KPI�зǸ�ơC
 * (4) ���P�@�~ �w ���PKPI�зǸ�ơC
 * </pre>
 * @author ���W��
 * @since 2019/05/09
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
@TxBean
public class XXQK_0200 extends UCBean {

    /** log */
    private static final Logger log = Logger.getLogger(XXQK_0200.class);

    /** �� TxBean �{���X�@�Ϊ� ResponseContext */
    private ResponseContext resp;

    /** �� TxBean �{���X�@�Ϊ� ReturnMessage */
    private ReturnMessage msg;

    private UserObject user;

    /** ���uID */
    private String EMP_ID;

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
     * 
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url = "/XX/QK/XXQK_0200/XXQK0200.jsp")
    public ResponseContext doPrompt(RequestContext req) {

        try {
            List<Map> rtnDIVList = new ArrayList();
            String DIV_NO = user.getDivNo();
            Map rtnMap = new HashMap();
            rtnMap.put("DIV_NO", DIV_NO);
            rtnMap.put("DIV_NM", user.getUserDivName());
            rtnDIVList.add(rtnMap);

            XX_QK0100 the_XX_QK0100 = new XX_QK0100();

            //�P�_�v���O�@��ϥΪ̵���EMP_ID = user.getEmpID()�ѼơA��l����
            int verifynum = VerifyUser(user);
            if (verifynum == 2) {
                EMP_ID = user.getEmpID();
            }

            //�v���P�_
            if (verifynum == 0) {
                rtnDIVList = the_XX_QK0100.queryDIV();
            }

            List<Map> rtnGroupList = null;
            try {
                rtnGroupList = the_XX_QK0100.queryGroupInfo(DIV_NO, "", EMP_ID);
            } catch (ModuleException me) {
                log.error("�L�էO");
            }

            resp.addOutputData("rtnDIVList", rtnDIVList);
            resp.addOutputData("rtnGroupList", rtnGroupList);
        } catch (Exception e) {
            log.error("���J����", e);
            MessageUtil.setReturnMessage(msg, e, req, ReturnCode.ERROR, "���J����");
        }

        return resp;
    }

    /**
     * �d��KPI�з�
     * @param req
     * @return
     */
    @CallMethod(action = "query")
    public ResponseContext doQuery(RequestContext req) {
        try {
            Map reqMap = VOTool.jsonToMap(req.getParameter("reqMap"));

            String DIV_NO = MapUtils.getString(reqMap, "DIV_NO");
            String GRP_ID = MapUtils.getString(reqMap, "GRP_ID");
            String TYPE_ID = StringUtils.isNotBlank(GRP_ID) ? GRP_ID : DIV_NO;

            //�P�_�v���O�@��ϥΪ̵���EMP_ID = user.getEmpID()�ѼơA��l����
            if (VerifyUser(user) == 2) {
                EMP_ID = user.getEmpID();
            }

            // �d��KPI�з�
            List<Map> rtnKPI_StandardList = new XX_QK0200().queryStandardByTYPE_ID(TYPE_ID);
            XX_QK0100 the_XX_QK0100 = new XX_QK0100();
            List<Map> rtnKPI_DetailList = the_XX_QK0100.queryDetailByTYPE_ID(TYPE_ID);
            List<Map> groupList = new ArrayList();
            try {
                groupList = the_XX_QK0100.queryGroupInfo(DIV_NO, GRP_ID, EMP_ID);
            } catch (ModuleException me) {
                log.error("�էO���o���~");
            }

            resp.addOutputData("rtnKPI_StandardList", rtnKPI_StandardList);
            resp.addOutputData("rtnKPI_DetailList", rtnKPI_DetailList);
            resp.addOutputData("groupList", groupList);
            resp.addOutputData("DIV_NO", DIV_NO);
            resp.addOutputData("EMP_ID", EMP_ID);
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�d�ߧ���");
        } catch (DataNotFoundException dnfe) {
            log.error("", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
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
     * �ק�KPI�з�
     * @param req
     * @return
     */
    @CallMethod(action = "update")
    public ResponseContext doUpdate(RequestContext req) {
        try {

            Map reqMap = VOTool.requestToMap(req);

            Transaction.begin();
            try {

                new XX_QK0200().updateStandardSTD(reqMap, user);

                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�ק�KPI�зǧ���");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�ק異��", me, req);
            }
        } catch (Exception e) {
            log.error("�ק異��", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�ק異��", e, req);
        }
        return resp;
    }

    /**
     * �ק�KPI�Ӷ�
     * @param req
     * @return
     */
    @CallMethod(action = "updateDetail")
    public ResponseContext doUpdateDetail(RequestContext req) {
        try {

            Map reqMap = VOTool.requestToMap(req);

            Transaction.begin();
            try {

                new XX_QK0200().updateStandardDetail(reqMap);

                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�ק�KPI�Ӷ�����");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�ק異��", me, req);
            }
        } catch (Exception e) {
            log.error("�ק異��", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�ק異��", e, req);
        }
        return resp;
    }

    /**
     * �s�WKPI�Ӷ�
     * @param req
     * @return
     */
    @CallMethod(action = "insertDetail")
    public ResponseContext doInsertDetail(RequestContext req) {
        try {
            Map reqMap = VOTool.requestToMap(req);

            Transaction.begin();
            try {

                new XX_QK0200().insertKPIDetail(reqMap);

                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�WKPI�Ӷ�����");
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
            log.error("�ק異��", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�s�W����", e, req);
        }
        return resp;
    }

    /**
     * �s�WKPI�з�
     * @param req
     * @return
     */
    @CallMethod(action = "insertSTD")
    public ResponseContext doInsertSTD(RequestContext req) {
        try {
            Map reqMap = VOTool.requestToMap(req);

            Transaction.begin();
            try {

                new XX_QK0200().insertKPISTD(reqMap, user);

                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�WKPI�зǧ���");
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
            log.error("�s�W����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�s�W����", e, req);
        }
        return resp;
    }

    /**
     * ���o�էO���
     * @param req
     * @return
     */
    @CallMethod(action = "getGRP")
    public ResponseContext getGRP(RequestContext req) {
        try {
            String DIV_NO = req.getParameter("DIV_NO");
            resp.addOutputData("rtnGroupList", new XX_QK0100().queryGroupInfo(DIV_NO, "", ""));

        } catch (Exception e) {
            log.error("�էO��Ƹ��J����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�էO��Ƹ��J����");
        }

        return resp;
    }

    /**
     * �ϥΪ��v���P�_ 
     * @param user
     * @return verifynum
     */
    private int VerifyUser(UserObject user) {
        String VERIFY = MapUtils.getString(user.getRoles(), "RLZZ0XX");
        int verifynum = 2;//�@��ϥΪ�  
        if (StringUtils.isNotBlank(VERIFY)) {
            verifynum = 0;//�t�κ޲z��
        } else if (user.isManager()) { //TODO
            verifynum = 1;//�D��
        }
        return verifynum;
    }

}
