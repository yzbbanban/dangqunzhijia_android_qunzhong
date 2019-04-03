package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/13 14:03
 * description
 */
data class GovernmentArticleListData(
        val list: List<ListBean>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class ListBean(
            val id: Int,
            val title: String,
            val pic: String,
            val view: Int,
            val author: String,
            val create_time: String)
}