package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/14 15:52
 * description
 */
data class IReleasedData(
        val list: MutableList<ListBean>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class ListBean(
            val id: Int,
            val title: String,
            val type: Int,
            val status: Int,
            val create_time: String)
}