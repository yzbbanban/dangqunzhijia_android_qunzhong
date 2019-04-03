package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/11 14:16
 * description
 */
data class VolunteersParticipateActivitiesListData(
        val list: MutableList<ListBean>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class ListBean(
            val id: Int,
            val type: Int,
            val uid: Int,
            val name: String,
            val phone: String,
            val avator: String)
}