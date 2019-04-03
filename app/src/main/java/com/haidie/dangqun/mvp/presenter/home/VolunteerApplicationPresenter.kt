package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VolunteerApplicationContract
import com.haidie.dangqun.mvp.model.home.VolunteerApplicationModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 18:29
 * description
 */
class VolunteerApplicationPresenter : BasePresenter<VolunteerApplicationContract.View>(),VolunteerApplicationContract.Presenter{
    private val volunteerApplicationModel by lazy { VolunteerApplicationModel() }
    override fun getVolunteerApplicationData(uid: Int, group_id: Int, username: String, gender: Int, phone: String, nation: String, birthday: String,
                                             face: Int, study: Int, identity: String, company: String, address: String, hobby: String, skill: String,
                                             experience: String, token: String) {
        checkViewAttached()
        val disposable = volunteerApplicationModel.getVolunteerApplicationData(uid, group_id, username, gender, phone, nation, birthday, face, study, identity, company,
                address, hobby, skill, experience, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("提交失败"){

                    override fun onNext(t: Boolean) {
                        mRootView!!.setVolunteerApplicationData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setVolunteerApplicationData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}