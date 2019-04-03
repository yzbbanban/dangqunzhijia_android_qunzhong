package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/13 09:16
 * description
 */
data class HomeNewsData(
        val list: MutableList<ListBean>) {
    data class ListBean(
            val id: Int,
            val toutiao_article_type: Int,
            val toutiao_article_type_name: String,
            val title: String,
            val cover_one: String,
            val update_time: String)
}