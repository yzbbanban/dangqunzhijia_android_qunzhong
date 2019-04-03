package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/11 09:30
 * description
 */
data class PointsMallListData(
        val list: MutableList<ListBean>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int,
        val score: Int,
        val avatar: String) {
    data class ListBean(
            val id: Int,
            val title: String,
            val pic: String,
            val score: Int
    )
}