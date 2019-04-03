package com.haidie.dangqun.mvp.contract.mine

import android.content.Context
import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ProvinceCityAreaData
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/14 16:21
 * description
 */
class ReleaseActivitiesContract {
    interface View : IBaseView{
         fun setJson(provinceCityAreaData: ProvinceCityAreaData)

        fun setReleaseActivitiesData(isSuccess : Boolean,msg : String)

        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getJson(context: Context)

        fun getReleaseActivitiesData(uid: RequestBody, type: RequestBody, title: RequestBody, content: RequestBody, start_time: RequestBody,
                                     end_time: RequestBody, area: RequestBody, address: RequestBody, token: RequestBody,
                                     part: MultipartBody.Part)
    }
}