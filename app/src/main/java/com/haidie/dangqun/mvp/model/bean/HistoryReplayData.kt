package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/08 13:46
 * description
 */
data class HistoryReplayData(
        val list: List<HistoryReplayItemData>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class HistoryReplayItemData(
            val id: Int,
            val type: Int,
            val content: String,
            val pic1: Any,
            val pic2: Any,
            val pic3: Any,
            val create_time: String,
            val username: String)
}