package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by Administrator
 * on 2018/08/27 15:51
 */
class ModifyInformationContract {
    interface View : IBaseView {
        fun modifyUserInfoSuccess()
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getModifyUserInfoData(uid: RequestBody, token: RequestBody,
                                  gender: RequestBody, nickname: RequestBody,birthday: RequestBody,
                                  part: MultipartBody.Part?)
    }
}