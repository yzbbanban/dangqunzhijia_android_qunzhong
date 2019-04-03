package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.*

/**
 * Create by   Administrator
 *      on     2018/11/26 20:41
 * description
 */
class NewPaymentAccountContract {
    interface View : IBaseView {
        fun setBuildingUnitAndRoomNumberData(buildingUnitAndRoomNumberData: BuildingUnitAndRoomNumberData)
        fun showError(msg: String, errorCode: Int)
        fun setBlockInfoData(blockInfoData: ArrayList<BlockInfoData>?,msg: String)
        fun setUnitListData(unitListData: ArrayList<UnitListData>?,msg: String)
        fun setHouseListData(houseListData: ArrayList<HouseListData>?,msg: String)
        fun setRoomNumberUserInfoData(roomNumberUserInfoData: RoomNumberUserInfoData?,msg: String)
        fun setAddPaymentAccountResultData(isSuccess: Boolean, msg: String)
    }
    interface Presenter : IPresenter<View> {
        fun getBuildingUnitAndRoomNumberData(uid: Int, token: String)
        fun getBlockInfoData(uid: Int,  token: String)
        fun getUnitListData(uid: Int, token: String, title: String)
        fun getHouseListData(uid: Int,token: String,title: String,unit: String)
        fun getRoomNumberUserInfoData( uid: Int,  token: String, house_id: String)
        fun getAddPaymentAccountResultData(uid: Int, token: String, house_id: Int)
    }
}