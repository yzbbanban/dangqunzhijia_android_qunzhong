package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.ModifyInformationContract
import com.haidie.dangqun.mvp.model.mine.ModifyInformationModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by Administrator
 * on 2018/08/27 15:52
 */
class ModifyInformationPresenter : BasePresenter<ModifyInformationContract.View>(), ModifyInformationContract.Presenter{
    private val modifyInformationModel by lazy { ModifyInformationModel() }
    override fun getModifyUserInfoData(uid: RequestBody, token: RequestBody, gender: RequestBody, nickname: RequestBody,
                                       birthday: RequestBody, part: MultipartBody.Part?) {
        checkViewAttached()
        val disposable = modifyInformationModel.getModifyUserInfoData(uid, token, gender, nickname, birthday, part)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<String>("修改失败"){
                    override fun onNext(t: String) {
                        mRootView!!.modifyUserInfoSuccess()
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.apply {
                            if (e.errorCode == ApiErrorCode.SUCCESS) {
                                modifyUserInfoSuccess()
                            }else{
                                showError(e.mMessage,e.errorCode)
                            }
                        }
                    }
                })

        addSubscription(disposable)
    }
}