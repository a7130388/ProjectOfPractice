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
 * 程式功能    KPI標準查詢及維護
 * 程式名稱    XXQK_0200.java
 * 作業方式    ONLINE
 * 概要說明    (1) 查詢作業 ─ 根據使用者選擇科別、組別，顯示KPI標準資料
 * (2) 新增作業 ─ 新增KPI標準資料。
 * (3) 修改作業 ─ 修改KPI標準資料。
 * (4) 註銷作業 ─ 註銷KPI標準資料。
 * </pre>
 * @author 黃名玄
 * @since 2019/05/09
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
@TxBean
public class XXQK_0200 extends UCBean {

    /** log */
    private static final Logger log = Logger.getLogger(XXQK_0200.class);

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    private UserObject user;

    /** 員工ID */
    private String EMP_ID;

    /** 
     * 覆寫父類別的 start() 以強制於每次 Dispatcher 呼叫 method 時都執行程式自定的初始動作
     */
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        //一定要 invoke super.start() 以執行權限檢核
        super.start(req);
        //呼叫自定的初始動作
        initApp(req);
        return null;
    }

    /**
     * 程式自定的初始動作，通常為取出 ResponseContext, UserObject, 
     * 及設定 ReturnMessage 及 response code.
     */
    private void initApp(RequestContext req) throws TxException {
        // 建立此 TxBean 通用的物件
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        user = this.getUserObject(req);

        // 先將 ReturnMessage 的 reference 加到 response context
        resp.addOutputData(IConstantMap.ErrMsg, msg);

        // 在 Cathay 通常只有一個 page 在前面 display，所以可以先設定
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

            //判斷權限是一般使用者給予EMP_ID = user.getEmpID()參數，其餘為空
            int verifynum = VerifyUser(user);
            if (verifynum == 2) {
                EMP_ID = user.getEmpID();
            }

            //權限判斷
            if (verifynum == 0) {
                rtnDIVList = the_XX_QK0100.queryDIV();
            }

            List<Map> rtnGroupList = null;
            try {
                rtnGroupList = the_XX_QK0100.queryGroupInfo(DIV_NO, "", EMP_ID);
            } catch (ModuleException me) {
                log.error("無組別");
            }

            resp.addOutputData("rtnDIVList", rtnDIVList);
            resp.addOutputData("rtnGroupList", rtnGroupList);
        } catch (Exception e) {
            log.error("載入失敗", e);
            MessageUtil.setReturnMessage(msg, e, req, ReturnCode.ERROR, "載入失敗");
        }

        return resp;
    }

    /**
     * 查詢KPI標準
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

            //判斷權限是一般使用者給予EMP_ID = user.getEmpID()參數，其餘為空
            if (VerifyUser(user) == 2) {
                EMP_ID = user.getEmpID();
            }

            // 查詢KPI標準
            List<Map> rtnKPI_StandardList = new XX_QK0200().queryStandardByTYPE_ID(TYPE_ID);
            XX_QK0100 the_XX_QK0100 = new XX_QK0100();
            List<Map> rtnKPI_DetailList = the_XX_QK0100.queryDetailByTYPE_ID(TYPE_ID);
            List<Map> groupList = new ArrayList();
            try {
                groupList = the_XX_QK0100.queryGroupInfo(DIV_NO, GRP_ID, EMP_ID);
            } catch (ModuleException me) {
                log.error("組別取得錯誤");
            }

            resp.addOutputData("rtnKPI_StandardList", rtnKPI_StandardList);
            resp.addOutputData("rtnKPI_DetailList", rtnKPI_DetailList);
            resp.addOutputData("groupList", groupList);
            resp.addOutputData("DIV_NO", DIV_NO);
            resp.addOutputData("EMP_ID", EMP_ID);
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "查詢完成");
        } catch (DataNotFoundException dnfe) {
            log.error("", dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
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
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "查詢筆數超出系統限制，請縮小查詢範圍");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "查詢失敗");
                }
            }
        } catch (Exception e) {
            log.error("查詢失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "查詢失敗", e, req);
        }
        return resp;
    }

    /**
     * 修改KPI標準
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
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "修改KPI標準完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "修改失敗", me, req);
            }
        } catch (Exception e) {
            log.error("修改失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "修改失敗", e, req);
        }
        return resp;
    }

    /**
     * 修改KPI細項
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
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "修改KPI細項完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "修改失敗", me, req);
            }
        } catch (Exception e) {
            log.error("修改失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "修改失敗", e, req);
        }
        return resp;
    }

    /**
     * 新增KPI細項
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
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增KPI細項完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "新增失敗", me, req);
            }
        } catch (Exception e) {
            log.error("修改失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "新增失敗", e, req);
        }
        return resp;
    }

    /**
     * 新增KPI標準
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
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增KPI標準完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "新增失敗", me, req);
            }
        } catch (Exception e) {
            log.error("新增失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "新增失敗", e, req);
        }
        return resp;
    }

    /**
     * 取得組別資料
     * @param req
     * @return
     */
    @CallMethod(action = "getGRP")
    public ResponseContext getGRP(RequestContext req) {
        try {
            String DIV_NO = req.getParameter("DIV_NO");
            resp.addOutputData("rtnGroupList", new XX_QK0100().queryGroupInfo(DIV_NO, "", ""));

        } catch (Exception e) {
            log.error("組別資料載入失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "組別資料載入失敗");
        }

        return resp;
    }

    /**
     * 使用者權限判斷 
     * @param user
     * @return verifynum
     */
    private int VerifyUser(UserObject user) {
        String VERIFY = MapUtils.getString(user.getRoles(), "RLZZ0XX");
        int verifynum = 2;//一般使用者  
        if (StringUtils.isNotBlank(VERIFY)) {
            verifynum = 0;//系統管理者
        } else if (user.isManager()) { //TODO
            verifynum = 1;//主管
        }
        return verifynum;
    }

}
