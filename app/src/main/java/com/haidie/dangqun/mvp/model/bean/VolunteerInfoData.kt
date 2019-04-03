package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/10/22 14:17
 * description
 */
data class VolunteerInfoData (
        val id: Int,
        val username: String,
        val pic: String,
        val score: Int,
        val rank: Int,
        val title: String,
        val score2: String?,
        val group_name: String,
        val activity: Int)