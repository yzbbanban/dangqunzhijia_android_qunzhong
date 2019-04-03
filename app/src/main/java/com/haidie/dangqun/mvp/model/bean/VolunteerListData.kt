package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/11 16:55
 * description
 */
data class VolunteerListData(
        val list: MutableList<VolunteerListItemData>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class VolunteerListItemData(
            val id: Int,
            val username: String,
            val is_leader: String,
            val create_time: String,
            val avator: String
    )
}