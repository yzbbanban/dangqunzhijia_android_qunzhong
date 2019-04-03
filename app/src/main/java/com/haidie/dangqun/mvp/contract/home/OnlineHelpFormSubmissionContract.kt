package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.BlockInfoData
import com.haidie.dangqun.mvp.model.bean.HouseListData
import com.haidie.dangqun.mvp.model.bean.UnitListData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

/**
 * Create by   Administrator
 *      on     2018/09/12 16:36
 * description
 */
class OnlineHelpFormSubmissionContract {
    interface View : IBaseView{
        fun setOnlineHelpFormSubmissionData( isSuccess : Boolean,msg : String)
        fun setBlockListData(blockInfoData: ArrayList<BlockInfoData>)
        fun setUnitListData(unitListData: ArrayList<UnitListData>)
        fun setHouseListData(houseListData: ArrayList<HouseListData>)
        fun showError(msg: String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getBlockData(uid: Int,token: String)
        fun getBlockListData(uid: Int,token: String)
        fun getUnitListData(uid: Int, token: String, title: String)
        fun getHouseListData(uid: Int,token: String,title: String,unit: String)
        fun getOnlineHelpFormSubmissionData(uid: RequestBody, token: RequestBody, type: RequestBody?, troubletype: RequestBody,
                                            is_online: RequestBody, username: RequestBody, gender: RequestBody, phone: RequestBody, identity: RequestBody,
                                            address: RequestBody?, title: RequestBody, content: RequestBody, @Part parts: List<MultipartBody.Part>?)
    }
}