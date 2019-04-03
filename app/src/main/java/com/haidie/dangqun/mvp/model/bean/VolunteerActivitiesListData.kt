package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/11 11:29
 * description
 */
data class VolunteerActivitiesListData(
        val list: MutableList<VolunteerActivitiesListItemData>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class VolunteerActivitiesListItemData(
            val id: Int,
            val title: String,
            val pic: String,
            val view: Int,
            val status: String,
            val start_time: String,
            val end_time: String,
            val area: String,
            val address: String,
            val create_time: String,
            val num: Int,
            val need_num: Int,
            val groupid: String
    )
}