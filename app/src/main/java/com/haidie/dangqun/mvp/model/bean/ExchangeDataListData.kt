package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/13 15:59
 * description
 */
data class ExchangeDataListData(
        val list: MutableList<ListBean>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class ListBean(
            val id: Int,
            val orderNo: String,
            val title: String,
            val pic: String,
            val user_score: Int,
            val status: String,
            val phone: String,
            val create_time: String)
}