package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/08/29 11:33
 * description
 */
data class LivingPaymentData(
        val list: List<LivingPaymentItemData>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class LivingPaymentItemData(
            val id: Int,
            val orderNo: String,
            val stop_date: String,
            val power_fee: String,
            val wuye_fee: String,
            val need_pay: String,
            val pay_status: Int
    )
}