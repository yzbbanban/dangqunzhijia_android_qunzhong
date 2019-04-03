package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/09/08 08:35
 * description
 */
data class ServiceCategoryData(
        val list: MutableList<ServiceCategoryItemData>) {
    data class ServiceCategoryItemData(
            val id: Int,
            val name: String,
            val image: String
    )
}

