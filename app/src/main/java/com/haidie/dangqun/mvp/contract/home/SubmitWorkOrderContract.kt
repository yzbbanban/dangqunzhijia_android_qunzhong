package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/08 08:37
 * description
 */
class SubmitWorkOrderContract {
    interface View : IBaseView {
        fun setServiceCategoryData(serviceCategoryData: ServiceCategoryData)
        fun showError(msg: String, errorCode: Int)
        fun setToWorkOrderData(isSuccess: Boolean,msg: String)
    }

    interface Presenter : IPresenter<View> {
        fun getServiceCategoryData(id: String)
        fun getToWorkOrderData(uid: RequestBody, cid: RequestBody, title: RequestBody, content: RequestBody, token: RequestBody, parts: List<MultipartBody.Part>)

    }
}