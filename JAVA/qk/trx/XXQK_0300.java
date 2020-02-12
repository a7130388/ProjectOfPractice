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
 * 程式功能    例外時間登記
 * 程式名稱    XXQK_0300.java
 * 作業方式    ONLINE
 * </pre>
 * @author 陳正穎
 * @since 2019/06/21
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@TxBean
public class XXQK_0300 extends UCBean {

    /** log */
    private static final Logger log = Logger.getLogger(XXQK_0300.class);

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    private UserObject user;

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
     * 初始查詢例外登記資料
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

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "查詢完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error(dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
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
    * 修改查詢資料格式
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
    * 查詢人員檔資料
    * @param req
    * @return
    */
    @CallMethod(action = "QueryEmployee")
    public ResponseContext doQueryEmployee(RequestContext req) {
        try {
            String DIV_NO = user.getDivNo();
            List<Map> rtnEMP = new XX_QK0100().queryEmployeeList(DIV_NO, "");
            resp.addOutputData("rtnEMP", rtnEMP);

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "查詢完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error(dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
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
     * 新增例外登記資料
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
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增完成");
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
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "新增失敗", e, req);
        }

        return resp;
    }

    /**
     * 修改例外登記資料
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
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "更新完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "更新失敗", me, req);
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "更新失敗", e, req);
        }
        return resp;
    }

    /**
     * 刪除例外登記資料
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
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "刪除完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "刪除失敗", me, req);
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "刪除失敗", e, req);
        }

        return resp;
    }
}
