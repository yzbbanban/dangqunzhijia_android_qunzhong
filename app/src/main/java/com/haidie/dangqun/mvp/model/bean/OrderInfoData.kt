package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/08 11:35
 * description
 */
data class OrderInfoData(
        val id: Int,
        val cid: Int,
        val type: String,
        val username: String,
        val title: String,
        val pic1: String?,
        val pic2: String?,
        val pic3: String?,
        val content: String?,
        val status: Int,
        val worker: String?,
        val is_change: String?,
        val remark: String?,
        val create_time: String,
        val update_time: String,
        val worker_group: String?,
        val status_value: String
)