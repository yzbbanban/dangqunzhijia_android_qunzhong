package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/12/11 14:23
 * description
 */
data class HalfPastFourClassDetailData(
        val id: Int,
        val cid: String,
        val pic: String,
        val start_time: String,
        val create_time: String,
        val content: String,
        val username: String,
        val phone: String,
        val sign_list: List<Sign>?) {
    data class Sign(
            val name: String)
}