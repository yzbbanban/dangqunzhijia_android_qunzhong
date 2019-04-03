package com.haidie.dangqun.mvp.model.bean

import com.google.gson.annotations.SerializedName

/**
 * Create by   Administrator
 *      on     2018/09/04 13:30
 * description
 */
class PrepaidOrderData(
        val return_code: String,
        val return_msg: String,
        val order_arr: OrderArr?) {
    data class OrderArr(
            val appid: String,
            val partnerid: String,
            val prepayid: String,
            val noncestr: String,
            val timestamp: String,
            @SerializedName("package")
            val packages: String,
            val sign: String
    )
}