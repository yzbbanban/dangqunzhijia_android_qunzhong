package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/10 09:53
 * description
 */
data class ArticleDetailData (
        val id: Int,
        val title: String,
        val pic: String,
        val content: String,
        val view: Int,
        val author: String,
        val create_time: String
)