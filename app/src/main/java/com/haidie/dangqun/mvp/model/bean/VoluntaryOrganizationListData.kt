package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/11 15:31
 * description
 */
data class VoluntaryOrganizationListData(
        val list: MutableList<VoluntaryOrganizationListItemData>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class VoluntaryOrganizationListItemData(
            val id: Int,
            val title: String,
            val pic: String,
            val num: Int,
            val create_time: String,
            val man: Int,
            val woman: Int)
}