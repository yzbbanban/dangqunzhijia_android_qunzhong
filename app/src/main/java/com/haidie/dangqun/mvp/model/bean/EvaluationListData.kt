package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/12 12:01
 * description
 */
data class EvaluationListData(
        val list: MutableList<ListBean>) {
    data class ListBean(
            val id: Int,
            val type: Int,
            val title: String,
            val num: Int,
            val view: Int,
            val start_time: String,
            val end_time: String,
            val create_time: String,
            val pic: String,
            val content: String,
            val category: Int)
}