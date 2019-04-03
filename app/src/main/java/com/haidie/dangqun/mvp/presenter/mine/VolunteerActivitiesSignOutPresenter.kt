package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.VolunteerActivitiesSignOutContract
import com.haidie.dangqun.mvp.model.mine.VolunteerActivitiesSignOutModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/11/06 11:49
 * description
 */
class VolunteerActivitiesSignOutPresenter : BasePresenter<VolunteerActivitiesSignOutContract.View>(),VolunteerActivitiesSignOutContract.Presenter{
    private val volunteerActivitiesSignOutModel by lazy { VolunteerActivitiesSignOutModel() }
    override fun getVolunteerActivitiesSignOutData(uid: RequestBody, id: RequestBody, token: RequestBody, content: RequestBody, part: MultipartBody.Part) {
        checkViewAttached()
        val disposable = volunteerActivitiesSignOutModel.getVolunteerActivitiesSignOutData(uid, id, token, content, part)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("签退失败") {
                    override fun onNext(t: Boolean) {
                        mRootView?.setVolunteerActivitiesSignOutData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.setVolunteerActivitiesSignOutData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}