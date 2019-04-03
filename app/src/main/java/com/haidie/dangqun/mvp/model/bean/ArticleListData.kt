package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/08 17:30
 * description
 */
data class ArticleListData(
        val list: MutableList<ArticleListItemData>) {
    data class ArticleListItemData(
            val id: Int,
            val title: String,
            val pic: String,
            val view: Int,
            val create_time: String
    )
}