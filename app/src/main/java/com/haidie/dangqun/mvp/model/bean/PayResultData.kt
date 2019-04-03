package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/05 12:00
 * description
 */
data class PayResultData(
        val id: Int,
        val out_trade_no: String,
        val transaction_id: String,
        val return_code: String,
        val result_code: String,
        val openid: String,
        val trade_type: String,
        val bank_type: String,
        val pay_style: String,
        val total_fee: String,
        val cash_fee: String,
        val time_end: String,
        val create_time: String,
        val update_time: String
)