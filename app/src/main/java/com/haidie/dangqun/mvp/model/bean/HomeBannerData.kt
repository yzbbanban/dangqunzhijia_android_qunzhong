package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/13 08:50
 * description
 */
data class HomeBannerData(
        val list: List<ListBean>) {
    data class ListBean(
            val id: Int,
            val title: String,
            val update_time: String,
            val cover_one: String)
}