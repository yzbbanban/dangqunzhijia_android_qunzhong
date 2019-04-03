package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.BlockInfoData
import com.haidie.dangqun.mvp.model.bean.HouseListData
import com.haidie.dangqun.mvp.model.bean.UnitListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

/**
 * Create by   Administrator
 *      on     2018/09/12 16:40
 * description
 */
class OnlineHelpFormSubmissionModel {
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
    fun getOnlineHelpFormSubmissionData( uid: RequestBody, token: RequestBody, type: RequestBody?, troubletype: RequestBody,
                                        is_online: RequestBody, username: RequestBody,  gender: RequestBody, phone: RequestBody, identity: RequestBody,
                                         address: RequestBody?,  title: RequestBody, content: RequestBody, @Part parts: List<MultipartBody.Part>?)
            : Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getOnlineHelpFormSubmissionData(uid, token, type, troubletype, is_online, username, gender, phone, identity,
                address, title, content, parts)
                .compose(SchedulerUtils.ioToMain())
    }
}