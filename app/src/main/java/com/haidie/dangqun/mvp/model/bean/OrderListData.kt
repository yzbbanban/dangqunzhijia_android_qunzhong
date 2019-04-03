package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/08 10:03
 * description
 */
data class OrderListData (
        val list: MutableList<OrderListItemData>,
        val group_id: Int,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int){
    data class OrderListItemData(
            val id: Int,
            val title: String,
            val username: String,
            val create_time: String,
            val status: String,
            val type: Int,
            val is_change: String?,
            val worker: String,
            val category: String,
            val worker_group: String?
    )
}