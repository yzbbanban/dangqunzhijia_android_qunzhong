package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/12 15:53
 * description
 */
data class OnlineHelpListData(
        val list: MutableList<ListBean>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class ListBean(
            val id: Int,
            val username: String,
            val title: String,
            val content: String?,
            val create_time: String,
            val check_status: Int,
            val status: Int,
            val pic1: String,
            val type: Int,
            val troubletype: Int,
            val worker: String
    )
}