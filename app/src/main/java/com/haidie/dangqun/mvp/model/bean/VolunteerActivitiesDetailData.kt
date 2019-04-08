package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/11 13:39
 * description
 */
data class VolunteerActivitiesDetailData (
        val id: Int,
        val type: Int,
        val title: String,
        val pic: String,
        val content: String,
        val view: Int,
        val author: String,
        val status: Int,
        val start_time: String,
        val end_time: String,
        val area: String?,
        val address: String,
        val create_time: String,
        val num: Int,
        val need_num: Int,
        val phone: String,
        val points: Int,
        val is_limit: Int,
        val groupid: String,
        val author_id: String,
        val remark: String,
        val is_join: String?,
        val group: Array<String>?,
        val join_status: String?,
        val is_signout: Int,
        val is_signin: Int)