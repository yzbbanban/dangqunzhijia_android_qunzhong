package com.haidie.dangqun.mvp.model.bean

/**
 * Create by   Administrator
 *      on     2018/11/27 09:50
 * description
 */
data class BuildingUnitAndRoomNumberData(
        val spaceList: List<Space>,
        val unitList: List<Unit>,
        val houseList: List<House>) {
    data class Unit(
            val id: Int,
            val unit: String)

    data class House(
            val id: Int,
            val roomNo: String)

    data class Space(
            val id: Int,
            val title: String)
}