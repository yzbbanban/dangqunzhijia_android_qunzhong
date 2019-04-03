package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/13 11:19
 * description
 */
data class OnlineHelpHistoryReplayData(
        val list: List<ListBean>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int
) {
    data class ListBean(
            val id: Int,
            val type: Int,
            val content: String,
            val pic1: Any,
            val pic2: Any,
            val pic3: Any,
            val create_time: String,
            val username: String
    )
}