package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/10 11:31
 * description
 */
data class ServiceListData(
        val list: MutableList<ServiceListItemData>) {
    data class ServiceListItemData(
            val id: Int,
            val title: String?,
            val pic: String,
            val area: String,
            val bus_name: String,
            val phone: String)
}