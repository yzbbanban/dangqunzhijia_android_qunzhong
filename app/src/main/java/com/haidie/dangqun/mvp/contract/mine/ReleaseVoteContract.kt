package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/17 14:19
 * description
 */
class ReleaseVoteContract {
    interface View : IBaseView{
        fun setReleaseVoteData(isSuccess : Boolean,msg : String)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getReleaseVoteData(uid: RequestBody, token: RequestBody, type: RequestBody, title: RequestBody,
                               content: RequestBody, start_time: RequestBody, end_time: RequestBody, choose: RequestBody,
                               part: MultipartBody.Part)
    }
}