package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/11 16:05
 * description
 */
data class VoluntaryOrganizationDetailData(
        val id: Int,
        val title: String,
        val pic: String,
        val number: String,
        val content: String,
        val address: String,
        val num: Int,
        val create_time: String,
        val man: Int,
        val woman: Int,
        val total_num: Int,
        val wait_man: Int,
        val username: String,
        val phone: String,
        val activity: Int,
        val is_volunteer: Int?
)