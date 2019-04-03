package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/12 09:23
 * description
 */
data class VolunteerDetailData (
        val id: Int,
        val group_id: Int,
        val is_leader: Int,
        val status: Int,
        val rank: Int,
        val username: String,
        val gender: String,
        val phone: String,
        val nation: String,
        val birthday: String,
        val face: String,
        val study: String,
        val hobby: String,
        val skill: String,
        val score: String?,
        val experience: String,
        val message: String,
        val create_time: String,
        val avator: String,
        val leader: String,
        val stat: String,
        val group_name: String,
        val activity: Int)