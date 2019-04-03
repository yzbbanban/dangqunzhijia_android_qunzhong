package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/12/11 13:35
 * description
 */
data class HalfPastFourClassData(
        val list: MutableList<HalfPastFourClassItemData>,
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int) {
    data class HalfPastFourClassItemData(
            val id: Int,
            val cid: String,
            val pic: String,
            val start_time: String,
            val create_time: String,
            val content: String
    )
}