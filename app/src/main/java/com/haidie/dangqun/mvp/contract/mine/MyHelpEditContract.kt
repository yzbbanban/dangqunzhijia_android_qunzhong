package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/17 14:19
 * description
 */
class MyHelpEditContract {
    interface View : IBaseView{
        fun setOnlineHelpDetailsData(onlineHelpDetailsData: OnlineHelpDetailsData)
        fun setMyHelpEditData(isSuccess : Boolean,msg : String)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getOnlineHelpDetailsData(uid: Int,  id: Int,token: String,page: Int,size: Int)
        fun getMyHelpEditData(uid: RequestBody, token: RequestBody, id: RequestBody, type: RequestBody?, troubletype: RequestBody,
                              is_online: RequestBody, username: RequestBody, gender: RequestBody, phone: RequestBody, identity: RequestBody,
                              address: RequestBody?, title: RequestBody, content: RequestBody,  pic1: RequestBody?,  pic2: RequestBody?,
                              pic3: RequestBody?,  part1: MultipartBody.Part?,  part2: MultipartBody.Part?,
                              part3: MultipartBody.Part?)
    }
}