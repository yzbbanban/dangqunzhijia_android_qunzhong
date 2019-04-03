package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/12 14:25
 * description
 */
data class VoteDetailData(
        val info: Info,
        val list: MutableList<VoteDetailListItemData>?) {
    data class Info(
            val id: Int,
            val type: Int,
            val title: String,
            val num: Int,
            val view: Int,
            val start_time: String,
            val end_time: String,
            val create_time: String,
            val pic: String,
            val content: String)
    data class VoteDetailListItemData(
            val id: Int,
            val content: String,
            val num: Int)
}