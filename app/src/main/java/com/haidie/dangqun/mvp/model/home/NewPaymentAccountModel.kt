package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.*
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/11/27 10:07
 * description
 */
class NewPaymentAccountModel {
    fun getBuildingUnitAndRoomNumberData(uid: Int, token: String): Observable<BaseResponse<BuildingUnitAndRoomNumberData>> {
        return RetrofitManager.service.getBuildingUnitAndRoomNumberData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getBlockInfoData(uid: Int,  token: String): Observable<BaseResponse<ArrayList<BlockInfoData>>>{
        return RetrofitManager.service.getBlockInfoData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getUnitListData(uid: Int, token: String, title: String): Observable<BaseResponse<ArrayList<UnitListData>>> {
        return RetrofitManager.service.getUnitListData(uid, token, title)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getHouseListData(uid: Int,token: String,title: String,unit: String): Observable<BaseResponse<ArrayList<HouseListData>>>{
        return RetrofitManager.service.getHouseListData(uid, token, title, unit)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getRoomNumberUserInfoData( uid: Int,  token: String, house_id: String): Observable<BaseResponse<RoomNumberUserInfoData>>{
        return RetrofitManager.service.getRoomNumberUserInfoData(uid, token, house_id)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getAddPaymentAccountResultData(uid: Int, token: String, house_id: Int): Observable<BaseResponse<Boolean>> {
        return RetrofitManager.service.getAddPaymentAccountResultData(uid, token,house_id)
                .compose(SchedulerUtils.ioToMain())
    }
}