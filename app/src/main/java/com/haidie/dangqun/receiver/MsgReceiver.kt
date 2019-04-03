package com.haidie.dangqun.receiver

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.haidie.dangqun.MyApplication.Companion.context
import com.haidie.dangqun.mvp.model.bean.xg.XGNotification
import com.haidie.dangqun.utils.LogHelper
import com.tencent.android.tpush.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/12/13 18:17
 * description
 */
class MsgReceiver : XGPushBaseReceiver() {
    private val intent = Intent("com.haidie.dangqun.activity.UPDATE_LISTVIEW")
    override fun onRegisterResult(p0: Context?, p1: Int, p2: XGPushRegisterResult?) {
        if (p0 == null || p2 == null) {
            return
        }
        val text: String = if ( p1== XGPushBaseReceiver.SUCCESS) {
            "${p2}注册成功"
            // 在这里拿token
            //   String token = message.getToken();
        } else {
            "${p2}注册失败，错误码：$p1"
        }
        LogHelper.d(text)
    }
    override fun onUnregisterResult(p0: Context?, p1: Int) {
        if (p0 == null) {
            return
        }
        val text: String = if (p1 == XGPushBaseReceiver.SUCCESS) {
            "反注册成功"
        } else {
            "反注册失败$p1"
        }
        LogHelper.d(text)
    }
    override fun onSetTagResult(p0: Context?, p1: Int, p2: String?) {
        if (p0 == null) {
            return
        }
        val text: String = if (p1 == XGPushBaseReceiver.SUCCESS) {
            "\"$p2\"设置成功"
        } else {
            "\"$p2\"设置失败,错误码：$p1"
        }
        LogHelper.d(text)
    }
    override fun onDeleteTagResult(p0: Context?, p1: Int, p2: String?) {
        if (p0 == null) {
            return
        }
        val text: String = if (p1 == XGPushBaseReceiver.SUCCESS) {
            "\"$p2\"删除成功"
        } else {
            "\"$p2\"删除失败,错误码：$p1"
        }
        LogHelper.d(text)
    }
    override fun onTextMessage(p0: Context?, p1: XGPushTextMessage?) {
        val text = "收到消息:" + p1.toString()
        // 获取自定义key-value
        val customContent = p1?.customContent
        if (customContent != null && customContent.isNotEmpty()) {
            try {
                val obj = JSONObject(customContent)
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    val value = obj.getString("key")
                    LogHelper.d("get custom value:$value")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        // APP自主处理消息的过程...
        LogHelper.d(text)
    }
    override fun onNotifactionClickedResult(p0: Context?, p1: XGPushClickedResult?) {
        if (p0 == null || p1 == null) {
            return
        }
        var text = ""
        if (p1.actionType == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE.toLong()) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            text = "通知被打开 :$p1"
            //            http://xg.qq.com/app/ctr_index/index#/app/2100300181/notice/create
            //            高级设置  点击打开 应用内页面  com.haidie.dangqun.ui.main.activity.MainActivity
        } else if (p1.actionType == XGPushClickedResult.NOTIFACTION_DELETED_TYPE.toLong()) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :$p1"
        }
        LogHelper.d("============\n$text")
        // 获取自定义key-value
        val customContent = p1.customContent
        if (customContent != null && customContent.isNotEmpty()) {
            try {
                val obj = JSONObject(customContent)
                // key为前台 http://xg.qq.com/app/ctr_index/index#/app/2100300181/notice/create 附加参数 配置的key
                val value = obj.optString("key")
                val argValue = obj.optString("arg")
                LogHelper.d("============\nget custom value:" + value +
                        "\nget custom arg:" + argValue)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        // APP自主处理的过程。。。
        LogHelper.d("============\n$text")
    }
    @SuppressLint("SimpleDateFormat")
    override fun onNotifactionShowedResult(p0: Context?, p1: XGPushShowedResult?) {
        if (p0 == null || p1 == null) {
            return
        }
        val notification = XGNotification()
        notification.msg_id = p1.msgId
        notification.title = p1.title
        notification.content = p1.content
        // notificationActionType==1为Activity，2为url，3为intent
        notification.notificationActionType = p1
                .notificationActionType
        // Activity,url,intent都可以通过getActivity()获得
        notification.activity = p1.activity
        notification.update_time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Calendar.getInstance().time)
        context.sendBroadcast(intent)
    }
}