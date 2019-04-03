package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/08/30 16:27
 * description
 */
data class HistoricalBillData(val list: List<HistoricalBillItemData>,
                              val total: Int,
                              val pages: Int,
                              val current: Int,
                              val size: Int) {
    data class HistoricalBillItemData(val id: Int,
                                      val orderNo: String,
                                      val year: String,
                                      val month: String,
                                      val username: String,
                                      val need_pay: String,
                                      val create_time: String,
                                      val pay_status: Int
    )
}