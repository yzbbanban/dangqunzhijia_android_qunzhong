package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/08/29 16:51
 * description
 */
data class BillDetailData(
        val id: Int,
        val orderNo: String,
        val apartment_id: Int,
        val start_date: String,
        val stop_date: String,
        val power_fee: String,
        val wuye_fee: String,
        val need_pay: String,
        val msg: String?,
        val pay_status: Int,
        val pay_type: String?,
        val thirdNo: String?,
        val author: String?,
        val pay_time: String?,
        val create_time: String,
        val update_time: String,
        val delete_tag: Int
)