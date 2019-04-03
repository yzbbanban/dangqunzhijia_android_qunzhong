package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/12 18:25
 * description
 */
data class OnlineHelpDetailsData (
        val id: Int,
        val type: Int,
        val troubletype: Int,
        val username: String,
        val phone: String,
        val gender: Int,
        val identity: String,
        val title: String,
        val content: String,
        val address: String,
        val check_status: Int,
        val status: Int,
        val help_desc: String?,
        val pic1: String?,
        val pic2: String?,
        val pic3: String?,
        val create_time: String,
        val update_time: String)